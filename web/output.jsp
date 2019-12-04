<%--
  Created by IntelliJ IDEA.
  User: hp-pc
  Date: 17.11.2019
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Form</title>
    <link rel="stylesheet" href="style.css">
  </head>
  <body>

  <div class="container">
    <header>
      <h1>Search products</h1>
    </header>
    <main>
      
      <fieldset class="segment1">
        <legend>First best offer</legend>
          <div class="inputField">
                <%-- Lepiej będzie użyć CSS bądźbootstrapa, ale jak na razie na zwykłe wyświetlanie to daje radę :) --%>
            <p><b>Product name: </b> ${productName0_0} <b>Price with delivery: </b>${price0_0} <b>Sklep: </b><a href="${url0_0}">link</a></p>
            <p><b>Product name: </b> ${productName1_0} <b>Price: </b>${price1_0} <b>Sklep: </b><a href="${url1_0}">link</a></p>
            <p><b>Product name: </b> ${productName2_0} <b>Price: </b>${price2_0} <b>Sklep: </b><a href="${url2_0}">link</a></p>
            <p><b>Product name: </b> ${productName3_0} <b>Price: </b>${price3_0} <b>Sklep: </b><a href="${url3_0}">link</a></p>
            <p><b>Product name: </b> ${productName4_0} <b>Price: </b>${price4_0} <b>Sklep: </b><a href="${url4_0}">link</a></p>
            <p><b>Total price for whole set: ${setPrice0}</b></p>

          </div>
      </fieldset>

      <fieldset class="segment2">
         <legend>Second best offer</legend>
          <div class="inputField">

              <p><b>Product name: </b> ${productName0_1} <b>Price with delivery: </b>${price0_1} <b>Sklep: </b><a href="${url0_1}">link</a></p>
              <p><b>Product name: </b> ${productName1_1} <b>Price: </b>${price1_1} <b>Sklep: </b><a href="${url1_1}">link</a></p>
              <p><b>Product name: </b> ${productName2_1} <b>Price: </b>${price2_1} <b>Sklep: </b><a href="${url2_1}">link</a></p>
              <p><b>Product name: </b> ${productName3_1} <b>Price: </b>${price3_1} <b>Sklep: </b><a href="${url3_1}">link</a></p>
              <p><b>Product name: </b> ${productName4_1} <b>Price: </b>${price4_1} <b>Sklep: </b><a href="${url4_1}">link</a></p>
              <p><b>Total price for whole set: ${setPrice1}</b></p>

          </div>
        </fieldset>

      <fieldset class="segment3">
         <legend>Third best offer</legend>
          <div id="3" class="inputField">

              <p><b>Product name: </b> ${productName0_2} <b>Price with delivery: </b>${price0_2} <b>Sklep: </b><a href="${url0_2}">link</a></p>
              <p><b>Product name: </b> ${productName1_2} <b>Price: </b>${price1_2} <b>Sklep: </b><a href="${url1_2}">link</a></p>
              <p><b>Product name: </b> ${productName2_2} <b>Price: </b>${price2_2} <b>Sklep: </b><a href="${url2_2}">link</a></p>
              <p><b>Product name: </b> ${productName3_2} <b>Price: </b>${price3_2} <b>Sklep: </b><a href="${url3_2}">link</a></p>
              <p><b>Product name: </b> ${productName4_2} <b>Price: </b>${price4_2} <b>Sklep: </b><a href="${url4_2}">link</a></p>
              <p><b>Total price for whole set: ${setPrice2}</b></p>

          </div>
      </fieldset>

    </main>
  </div>
  <script>
      function hide() {
          var x = document.getElementById("3");
          if (x.style.display === "none") {
              x.style.display = "block";
          } else {
              x.style.display = "none";
          }
      }
  </script>
  </body>
</html>


