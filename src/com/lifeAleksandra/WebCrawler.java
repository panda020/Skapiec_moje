package com.lifeAleksandra;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.Instant;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebCrawler{



    public static void merge(FoundProduct[] a, FoundProduct[] l, FoundProduct[] r , int left, int right){
        int i=0, j=0, k=0;

        while(i<left && j<right){
            if(l[i].isItBetter(l[i],r[i])){ // l <= r
                a[k++] = l[i++];
            }
            else{
                a[k++] = r[j++];
            }
        }
        while (i < left){
            a[k++] = l[i++];
        }
        while(j < right){
            a[k++] = r[j++];
        }
    }


    public static void  mergeSort(FoundProduct[] a, int ammount){
        if(ammount < 2){
            return;
        }
        int mid= ammount/2;
        FoundProduct[] l = new FoundProduct[mid];
        FoundProduct[] r = new FoundProduct[ammount-mid];

        for( int i=0; i<mid; i++){
            l[i] = a[i];
        }
        for(int i=mid; i<ammount ; i++){
            r[i-mid]=a[i];
        }
        mergeSort(l,mid);
        mergeSort(r,ammount-mid);

        merge(a,l,r,mid,ammount-mid);
    }

    public static void buble(FoundProduct[] a, int ammount){
        int change = 1;
        FoundProduct temporaryProduct;
        while(change > 0){
            change = 0;
            for(int i=0; i<ammount-1; i++) {
                if(a[i] != null && a[i+1] != null){
                    if (!a[i].isItBetter(a[i], a[i + 1])) {
                        temporaryProduct = a[i + 1];
                        a[i + 1] = a[i];
                        a[i] = temporaryProduct;
                        change++;
                    }
                }
            }
        }
    }





    public FoundProduct[] Search(Product product, int howManyWebSites) throws IOException {

        // pobieranie odp rzeczy w ifach
        //spr ceny zanim wejdzie w link do "comparePriceLink"
        Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.getName()); //pobranie zrodla strony
        Elements webSites; //strony 1.2...3
        //Elements oneLink; //brak opcji porownaj ceny
        //ArrayList<FoundProduct> theBestProducts= new ArrayList<FoundProduct>(3);
        FoundProduct[] theBestProducts = new FoundProduct[5];
        int numberOfProducts =0;
        FoundProduct foundProduct2;
        int spr = 0;
        int numberOfWebSites = 0;

            do {
                Document document = connect.get();
                webSites = document.select("a.pager-btn.arrow.right"); //strong.price.gtm_sor_price //strony z wynikami wyszukiwania 1,2,3...
                Elements box = document.select("div.box-row.js");
                //System.out.println(box.size());
                    for (Element box1 : box) {
                        // System.out.println("DLa boxa nr"+box.size());
                        Float priceInBox = Float.parseFloat(box1.select("strong.price.gtm_sor_price").text().replace("od ", "").replace(" ", "").replace("zł", "").replace(",", "."));
                        if (priceInBox >= product.min_price-0.1*product.min_price && priceInBox <= product.max_price) {
                            Elements comparePriceLink = box1.select("a.compare-link-1");//strona porownaj cene- kilka ofert na Skapiec.pl
                            if (comparePriceLink.text().isEmpty()) {
                                comparePriceLink = box1.select("a.more-info"); //dla stron bez porownaj ceny
                            }
                            connect = Jsoup.connect("https://www.skapiec.pl" + comparePriceLink.attr("href"));
                            document = connect.get();
                            Elements rectangle = document.select("a.offer-row-item.gtm_or_row");
                            for (Element square1 : rectangle) {
                                float theBestDeliveryPrice = 1000;
                                Elements price = square1.select("span.price.gtm_or_price");
                                String productName = square1.select("span.description.gtm_or_name").text();
                                if (!price.text().isEmpty()) {
                                    float floatPrice = Float.parseFloat(price.text().replace(",", ".").replace(" zł", "").replace(" ", ""));
                                    if (product.min_price <= floatPrice && floatPrice <= product.max_price) {
                                        Elements numberOfOpinions = square1.select("span.counter");
                                        if (!numberOfOpinions.text().isEmpty()) {  //spr czy są "brak opinii"
                                            if (Integer.parseInt(numberOfOpinions.text()) >= 50) { //warunek na ilosc opinii
                                                Elements reputation = square1.select("span.stars.green");  //.span.stars.green
                                                float foundReputation = Float.parseFloat(reputation.attr("style").replace("width: ", "").replace("%", ""));
                                                if (foundReputation >= product.reputation * 100 / 5) {//gwiazdki
                                                    Elements url = square1.select("a.offer-row-item.gtm_or_row");
                                                    String foundUrl = "https://www.skapiec.pl" + url.attr("href");
                                                    //pobieranie kosztu dostawy dla każdego produktu "prostokąta na stronie skapiec -> porownaj ceny lub wiecej info jesli nie ma porownaj ceny"
                                                    Elements delivery = square1.select("a.delivery-cost.link.gtm_oa_shipping");
                                                    //darmowa dostawa
                                                    if (delivery.text().isEmpty()) {
                                                        theBestDeliveryPrice = 0;
                                                        //System.out.println("Najtansza dostawa: " + theBestDeliveryPrice);
                                                    } else {
                                                        //koszt dostawy != darmowa dostawa
                                                        String delivery1 = delivery.attr("href");
                                                        connect = Jsoup.connect("https://www.skapiec.pl" + delivery1);
                                                        document = connect.get();
                                                        //sprawdzanie ceny dostawy w zakladkach Poczta, Kurier itd (bez odbior osobisty)
                                                        String selector = "#product_content > ul > li:nth-child(0) > a";  //trzeba podmieniać (x) na 1,2,4,5 zeby przejsc po zakladkach
                                                        for (int i = 1; i < 6; i++) {
                                                            int previousI = i - 1;
                                                            if (i == 3) {
                                                                i++;
                                                            }
                                                            selector = selector.replace(String.valueOf(previousI), String.valueOf(i));
                                                            Elements sub = document.select(selector);
                                                            connect = Jsoup.connect("https://www.skapiec.pl/delivery.php" + sub.attr("href"));
                                                            //System.out.println("https://www.skapiec.pl/delivery.php" + sub.attr("href"));
                                                            document = connect.get();
                                                            Elements deliveryPrice = document.select("#deliveryRulesets > tbody > tr.even > td:nth-child(2) > div:nth-child(1) > b");
                                                            Elements deliveryPrice1 = document.select("#deliveryRulesets > tbody > tr.odd > td:nth-child(2) > div > b");
                                                            for (Element deliveryPrice2 : deliveryPrice) {
                                                                String p = deliveryPrice2.text().replace(" zł", "");
                                                                float actualDeliveryPrice = Float.parseFloat(p);
                                                                if (theBestDeliveryPrice > actualDeliveryPrice) {
                                                                    theBestDeliveryPrice = actualDeliveryPrice;
                                                                }
                                                            }
                                                            for (Element deliveryPrice2 : deliveryPrice1) {
                                                                String p = deliveryPrice2.text().replace(" zł", "");
                                                                float actualDeliveryPrice = Float.parseFloat(p);
                                                                if (theBestDeliveryPrice > actualDeliveryPrice) {
                                                                    theBestDeliveryPrice = actualDeliveryPrice;
                                                                }
                                                            }
                                                        }
                                                        //System.out.println("Najlepsza dostawa: "+ theBestDeliveryPrice);
                                                    }
                                                    //jesli nie darmowa dostawa i brak info o cenie za dostawe
                                                    if (theBestDeliveryPrice == 1000) {
                                                        break;
                                                    }
                                                    //pobieranie id sklepu potrzebne do spr czy produkty pochodza z jednego sklepu
                                                    String[] id = url.attr("href").split("/");
                                                    int shopId = Integer.parseInt(id[3]);
                                                    if (theBestProducts[0] == null || theBestProducts[1] == null || theBestProducts[2] == null || theBestProducts[3] == null || theBestProducts[4] == null) {
                                                        // FoundProduct foundProduct1 = new FoundProduct(productName, floatPrice, theBestDeliveryPrice, foundReputation,shopId);
                                                        theBestProducts[numberOfProducts] = new FoundProduct(productName, floatPrice, theBestDeliveryPrice, foundReputation, shopId, foundUrl);
                                                        numberOfProducts++;
                                                    } else {
                                                        foundProduct2 = new FoundProduct(productName, floatPrice, theBestDeliveryPrice, foundReputation, shopId, foundUrl);
                                                        for (int i = 0; i < 5; i++) {
                                                            if (foundProduct2.isItBetter(foundProduct2, theBestProducts[i]) == true) {
                                                                FoundProduct eliminatedOne = theBestProducts[i];
                                                                theBestProducts[i] = foundProduct2;
                                                                foundProduct2 = eliminatedOne;
                                                            }
                                                        }
                                                        numberOfProducts++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                for (Element elem : webSites) {
                    connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
                }

                numberOfWebSites++;
                if(numberOfWebSites>howManyWebSites){
                    break;
                }
        }while(webSites.size() == 1);

//        theBestProducts[4].buble(theBestProducts); // to dziala duzo wolniej

        Timestamp befor_sort = new Timestamp(System.currentTimeMillis());
        System.out.println("czas przed sortowaniem " + befor_sort);

        // za ammount wpisywac liczbe produktow ktore mamy w tym pobranym z crawlera zestawieniu
        // sortowanie megazordem XD szybsze o około 6/1000 sekundy...
        mergeSort(theBestProducts, 5);
        //sortowanie bombelkowo-to poczatkowe
        buble(theBestProducts, 5 );


        Timestamp after_sort = new Timestamp(System.currentTimeMillis());
        System.out.println("czas po posortowaniu " + after_sort);

//        for(int i=0; i<3; i++) {
//            System.out.println("POSORTOWANE");
//            if(theBestProducts[i] !=  null ) {
//                System.out.println("produkt" + i + ":");
//                System.out.println("Nazwa produktu: "+theBestProducts[i].getFoundProductName());
//                System.out.println("Cena produktu: "+theBestProducts[i].getFoundProductPrice());
//                System.out.println("Cena+wysyłka: "+theBestProducts[i].getFoundProductTotalPrice());
//                System.out.println("Url produktu: "+theBestProducts[i].getUrl());
//            }
//        }
        return theBestProducts;
    }

    public FoundProduct[] Test(Product product) throws IOException {
        FoundProduct[] f = new FoundProduct[3];
        f[0] = new FoundProduct(product.getName() + " pierwszy", 100, 10, 4, 404, "https://www.onet.pl/");
        f[1] = new FoundProduct(product.getName() + " drugi", 200, 10, 3, 405, "https://b");
        f[2] = new FoundProduct(product.getName() + " trzeci", 300, 20, 4, 406, "https://c");
        return f;
    }

    public static void main(String[] args) {
        Product p = new Product("iphone 6s", 1, 900, 10000, 4);
        //Product p1 = new Product("lenovo x1 i7 8GB", 1, 4000, 7000, 4);
        WebCrawler w = new WebCrawler();
        FoundProduct[] fp;
        FoundProduct[] fp1;
        try {
            fp = w.Search(p,5); //20boxow to 1 strona
        //    fp1 = w.Search(p1);
//            for(int i=0; i<3; i++) {
//                if (fp[i] != null) {
//                    System.out.println("produkt" + i + ":");
//                    System.out.println("Nazwa produktu: " + fp[i].getFoundProductName());
//                    System.out.println("Cena produktu: " + fp[i].getFoundProductPrice());
//                    System.out.println("Cena+wysyłka: " + fp[i].getFoundProductTotalPrice());
//                    System.out.println("Url produktu: " + fp[i].getUrl());
//                }
//            }

            for(int i=0; i<5; i++) {
                if(fp[i] !=  null) {
                    System.out.println("produkt" + i + ":");
                    System.out.println("Nazwa produktu: "+fp[i].getFoundProductName());
                    System.out.println("Cena produktu: "+fp[i].getFoundProductPrice());
                    System.out.println("Cena+wysyłka: "+fp[i].getFoundProductTotalPrice());
                    System.out.println("Url produktu: "+fp[i].getUrl());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
