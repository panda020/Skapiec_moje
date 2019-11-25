package com.lifeAleksandra;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
//jeszcze potrzeba - pobrac nazwe XD, koszty dostawy,ocene
//CENE SPRAWDZAĆ W PASKU WYSZUKIWAŃ BO JAK BEDZIE SIE WCHODZIĆ W STRONĘ TO ZAJMIE CIUT WIECEJ NIZ 15 SEKUND XD
// warunki! czyli zakres cen, minalna ilosć opini 50
//jak brak opinii/kosztów to ignorujemy
//jezeli brak wyników to wyszukuje bez zakresu cenowego jesli dalej nic -> alert (jak bedzie stronka)
//jesli dwa produkty ta sama cena to bierzemy ten o lepszej opini sprzedawcy
//brak licytacji

//wynik nazwa produktu wraz z linkiem

public class Controller {

    //Skąpiec.pl
    private ArrayList<Product> products = new ArrayList<>(); //lista produktow
    private ArrayList<String> links = new ArrayList<>(); //lista linków do kupienia

    public void Search(Product product) throws IOException {

        Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.name);
        Document document ;
        Elements search_site ;//strony z wynikami wyszukiwania
        do {
            document = connect.get();//łączenie - strona z wyszukiwaniami
            search_site = document.select("a.pager-btn.arrow.right");
            Elements more_info = document.select("a.more-info"); //strona z produktem na Skapiec
            Elements compare_link = document.select("a.compare-link-1"); //strona z wieloma produktami w jednym sklepie

            for (Element alem : more_info) {
                connect = Jsoup.connect("https://www.skapiec.pl" + alem.attr("href"));
                document = connect.get();//łączenie
                Elements price = document.select("span.price.gtm_or_price");
                Elements shipping = document.select("a.delivery-cost.link.gtm_oa_shipping");
                Elements link = document.select("a.offer-row-item.gtm_or_row");

                for (Element eh : price)
                {
                    System.out.println(eh.text());
                }
                for (Element eh : shipping )
                {
                    System.out.println(eh.text());//tutaj trzeba link i wziąć przesyłkę itd
                }
                for (Element eh : link )
                {
                    System.out.println(eh.text());//link do produktu w sklepie
                }

            }
//            for (Element alem : compare_link) {
//                connect = Jsoup.connect("https://www.skapiec.pl" + alem.attr("href"));
//                document = connect.get();//łączenie
//                Elements price = document.select("span.price.gtm_or_price");
//                Elements dostawa = document.select("a.delivery-cost.link.gtm_oa_shipping");
//                Elements link = document.select("a.offer-row-item.gtm_or_row");
//                for (Element eh : price )
//                {
//                    System.out.println("koszt produktu: ");
//                    System.out.println(eh.text()+"DUZOOOO");
//                }
//                for (Element eh : dostawa )
//                {
//                    System.out.println("koszt dostawy: ");
//                    System.out.println(eh.text());//tutaj trzeba link i wziąć przesyłkę itd
//                }
//                for (Element eh : link )
//                {
//                    System.out.println("link to : ");
//                    System.out.println(eh.text());//link do produktu w sklepie
//                }
//
//            }

            for (Element elem : search_site) {
                connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
            }

        }while(search_site.size()==1);

    }


    public static void main(String[] args) throws IOException {
        Double[] range = new Double[2];
        range[0] = 10.0;
        range[1] = 20.0;
        Product product = new Product("zasłona wisan", 1, 150, 250,2);
        Controller controller = new Controller();
        controller.Search(product);
    }
}