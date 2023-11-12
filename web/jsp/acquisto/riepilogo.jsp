<%-- 
    Document   : riepilogo
    Created on : 5-mar-2020, 14.33.43
    Author     : Utente
--%>

<%@page import="model.mo.Buono"%>
<%@page session = "false"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    
    String nazione = (String) request.getAttribute("nazione");
    String citta = (String) request.getAttribute("citta");
    String via = (String) request.getAttribute("via");
    long numeroCivico = (long) request.getAttribute("numeroCivico");
    int CAP = (int) request.getAttribute("CAP");
    String cartaPagamento = (String) request.getAttribute("cartaPagamento");
    double prezzo = (double) request.getAttribute("prezzo");
    boolean buonoPresente = (boolean) request.getAttribute("buonoPresente");
    Buono buono = (Buono) request.getAttribute("buono");
    long codiceBuono = 0;
    if(buonoPresente){
        codiceBuono = buono.getCodiceBuono();
    }
    
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numProdotto;
    if(prodotti == null){
        numProdotto = 0;
    }else{
        numProdotto = prodotti.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Riepilogo ordine";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        
        <script language="javascript">
            function procedi(){
                var f = document.paga;
                f.submit();
            }
            
            function goBack(){
                var f = document.annulla;
                f.submit();
            }
            
            function mainOnLoadHandler(){
                document.paga.submitButton.addEventListener("click", procedi);
                document.paga.backButton.addEventListener("click", goBack);
            }
        </script>
        <style>
            .indirizzo{
                float: left;
                width: 46%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: steelblue;
                padding: 10px 14px 10px 14px;
                margin: 0 17px 16px 0;
                box-shadow: 0 3px 2px #777;
            }
            
            .pagamento{
                float: left;
                width: 46%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: green;
                padding: 10px 14px 10px 14px;
                margin: 0 17px 16px 0;
                box-shadow: 0 3px 2px #777;
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
            <div class='nome'>
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>
            
            <div style="margin-top: 15px; margin-bottom: 15px;">
                <h2>Step 2: Riepilogo pagamento</h2>
            </div>
            
            <div class="clearfix">
                
                <!--LATO SINISTRO CON LA CONFERMA DEI DATI PER L'ORDINE-->
                <div class="indirizzo">
                    <h3>Indirizzo di consegna</h3>
                    </br>
                    <address><%=via%> n. <%=numeroCivico%>, <%=citta%>, <%=nazione%></br>CAP: <%=CAP%></address>
                    
                    </br>
                    <h3>Dati carta</h3>
                    </br>
                    <p>Numero carta di credito: <%=cartaPagamento%></p>
                
                    <%if(buonoPresente){%>
                        </br>
                        <h3>Dati buono sconto</h3>
                        </br>
                        <p>Codice buono sconto: <%=buono.getCodiceBuono()%></p>
                        <p>Sconto applicato: <%=buono.getSconto()%>%</p>
                    <%}%>
                </div>
            
                <!--LATO DESTRO CON LA LISTA DEI PRODOTTI INCLUSI NELL'ORDINE-->
                <div class="pagamento">
                    <h3>Lista prodotti inclusi nell'ordine</h3>
                    </br>
                    <%for( i=0 ; i<numProdotto ; i++ ){%>
                        <p><b><%=prodotti.get(i).getNomeCarta()%></br>Quantit&agrave;: <%=carrello.get(i).getQuantità()%></br>Prezzo unitario: €<%=prodotti.get(i).getPrezzo()%></br></br></p>
                    <%}%>
                
                    <p><b>Prezzo finale:</b> €<%=prezzo%></p>
                </div>
            
                <!--BOTTONI-->
                <div style="clear: both;">
                    <div class="left">
                        <form name="paga" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="Acquisto.paga"/>
                            <input type="hidden" name="cartaPagamento" value="<%=cartaPagamento%>"/>
                            <input type="hidden" name="nazione" value="<%=nazione%>"/>
                            <input type="hidden" name="citta" value="<%=citta%>"/>
                            <input type="hidden" name="via" value="<%=via%>"/>
                            <input type="hidden" name="numeroCivico" value="<%=numeroCivico%>"/>
                            <input type="hidden" name="CAP" value="<%=CAP%>"/>
                            <input type="hidden" name="prezzo" value="<%=prezzo%>"/>
                            <%if(buonoPresente){%>
                                <input type="hidden" name="buonoPresente" value="S"/>
                                <input type="hidden" name="codiceBuono" value="<%=codiceBuono%>"/>
                            <%}else{%>
                                <input type="hidden" name="buonoPresente" value="N"/>
                            <%}%>
                            <input type="button" name="submitButton" value="Paga" class="button" style="font-size: medium;">
                            <input type="button" name="backButton" value="Annulla" class="button" style="font-size: medium;">
                        </form>
                    </div>
                </div>
                
            </div>
                            
            <form name="annulla" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="Acquisto.ordina"/>
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
