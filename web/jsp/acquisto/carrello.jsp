<%-- 
    Document   : carrello
    Created on : 21-lug-2019, 11.26.32
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.session.mo.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0, j = 1;

    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numProd;
    if (prodotti == null) {
        numProd = 0;
    } else {
        numProd = prodotti.size();
    }

     double prezzo = (double) request.getAttribute("prezzo");
    
    ArrayList<Boolean> disponibilita = (ArrayList<Boolean>) request.getAttribute("disponibilita");

    boolean loggedOn = (boolean) request.getAttribute("loggedOn");

    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");

    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Carrello";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function prodottoFormSubmit(index, codice) {
                var f = document.forms["prodottoForm" + index];
                f.codiceProdotto.value = codice;
                f.submit();
                return;
            }

            function quantitaProdotto(index) {
                var f = document.forms["quantitaProdotto" + index];
                f.submit();
                return;
            }
        </script>
        <style>
            #quantita{
                height: 22px;
                font-size: large;
            }

            #ordina{
                padding: 5px 10px;
                background-color: #228b22;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                width: 100%;
            }

            #cancella{
                padding: 5px 10px;
                background-color: #dc143c;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                width: 100%;
            }
        </style>

        <%@include file="/include/htmlHead.inc" %>

    </head>
    <body>
        <header>
            <%@include file="/include/HeaderUtente.inc" %>
        </header>

        <hr>

        <main>
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>

            <%if (numProd > 0) {%>
            <!--LISTA PRODOTTI DEL CARRELLO DA VISUALIZZARE-->
            <section>
                <div style="float: left; width: 75%; margin-right: 1%;">
                    <%for (i = 0; i < numProd; i++) {%>           
                    <article>
                        <hr>
                        <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">

                            <!--IMMAGINE E INFORMAZIONI FLOTTANTI SULLA SINISTRA-->
                            <div style="float: left; margin-right: 15px;">
                                <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="Catalogo.viewProdotto"/>
                                    <input type="hidden" name="codiceProdotto"/>
                                    <a href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                        <img src="images/<%=prodotti.get(i).getImmagine()%>" width="130" height="130" alt="Visualizza prodotto"/>
                                    </a>
                                </form>
                            </div>
                            <div style="float: left; width: 26%;">
                                <span><b><%=prodotti.get(i).getTipoCarta()%></b></span>
                                <br/>
                                <span><%=prodotti.get(i).getRarità()%></span>
                                <br/>
                                <%if (disponibilita.get(i)) {%>
                                <span style='color: green'>Disponibile</span>
                                <%} else {%>
                                <span style='color: red'>Non disponibile</span>
                                <%}%>
                                <br/>
                                <span><%=carrello.get(i).getQuantità()%></span>
                            </div>

                            <!--FORMS-->
                            <div style="float: left; width: 37%; text-align: center;">

                                <!--FORM PER IL CAMBIO DELLA QUANTITA'-->
                                <form name="quantitaProdotto<%=i%>" action="Dispatcher" method="post">
                                    <label for="quantita">Quantit&agrave;: </label> 
                                    <select id="quantita" name="quantita" onchange="quantitaProdotto(<%=i%>)"> 
                                        <% for (j = 1; j < 30; j++) {%>
                                        <option value="<%=j%>" <% if (j == carrello.get(i).getQuantità()) {%>selected="selected"<%}%>><%=j%></option>
                                        <%}%>
                                    </select>
                                    <input type="hidden" name="codiceProdotto" value="<%=carrello.get(i).getCodiceProdotto()%>"/>
                                    <input type="hidden" name="controllerAction" value="Acquisto.cambiaQuantita"/>
                                </form>

                                </br>

                                <!--FORM PER LA RIMOZIONE DAL CARRELLO-->
                                <form name="rimuoviProdotto<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="codiceProdotto" value="<%=carrello.get(i).getCodiceProdotto()%>"/>
                                    <input type="hidden" name="controllerAction" value="Acquisto.rimuovi"/>
                                    <input type="submit" value="Rimuovi" class="button">
                                </form>
                            </div>

                            <!--PREZZO DELL'ARTICOLO-->
                            <div style="float: left; text-align: right; width: 15%; padding-right: 15px;">
                                <p><b>€<%=prodotti.get(i).getPrezzo()%></b></p>       
                            </div>        
                        </div>
                    </article>
                    <%}%>
                </div>
            </section>

            <!--FORM ORDINA E CANCELLA FLOTTANTI SULLA DESTRA-->
            <div style="float: left; width: 24%; text-align: center;">
                <h2>Dettagli ordine</h2>
                </br>
                <p style="font-size: large;">Totale: €<%=prezzo%></p>
                </br>
                <!--FORM PER ORDINARE-->
                <form name="ordina" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="Acquisto.ordina"/>
                    <input type="submit" value="Ordina" id="ordina">
                </form>
                </br>
                <!--FORM PER CANCELLARE IL CARRELLO-->
                <form name="cancella" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="Acquisto.cancella"/>
                    <input type="submit" value="Cancella" id="cancella">
                </form>
            </div>
            <%} else {%>
            <div style='margin-top: 20px;'>
                <h2>Carrello vuoto</h2>
                <p>Il tuo carrello &eacute; vuoto. Per aggiungere articoli al tuo carrello naviga su <a href="Dispatcher?controllerAction=Catalogo.view">Tana del goblin</a> e, quando trovi un articolo che ti interessa, clicca sul "Aggiungi al carrello"</p>
            </div>
            <%}%>

            <div style="clear: both;"></div>

        </main>

        <%@include file="/include/footer.inc" %>

    </body>
</html>
