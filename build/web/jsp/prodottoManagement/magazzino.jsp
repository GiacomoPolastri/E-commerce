<%-- 
    Document   : magazzino
    Created on : 5-mar-2020, 15.00.11
    Author     : Utente
--%>
<%@page session = "false"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numeroProdotti;
    if(prodotti == null){
        numeroProdotti = 0;
    }else{
        numeroProdotti = prodotti.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Magazzino";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function prodottoFormSubmit(index, codice){
                var f = document.forms["prodottoForm" + index];
                f.codiceProdotto.value = codice;
                f.submit();
                return;
            }
        </script>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/HeaderAdmin.inc"%>
        </header>
        
        <hr>
        
        <main>
            
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>
            
            <!--FORM PER PASSARE ALLA PAGINA DI INSERIMENTO DI UN NUOVO PRODOTTO-->
            <form name="inserisciProdotto" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="ProdottoManagement.inserisciProdottoView"/>
                <input type="submit" value="Nuovo prodotto" class="mainButton">
            </form>
            
            <!--LISTA PRODOTTI NEL MAGAZZINO-->
            <section class="clearfix" id="boxMagazzino">
                <%for(i=0 ; i<numeroProdotti ; i++){%>           
                    <article>
                        
                        <div>
                            <!--IMMAGINE SULLA SINISTRA-->
                            <div style="float: left; margin-right: 50px;">
                                <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="ProdottoManagement.modificaProdottoView"/>
                                    <input type="hidden" name="codiceProdotto"/>
                                    <a href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                        <img id="ProdImage" src="images/<%=prodotti.get(i).getImmagine()%>" width="170" height="170" alt="Visualizza prodotto"/>
                                    </a>
                                </form>
                            </div>
                                    
                            <!--INFORMAZIONI E FORM DEL PRODOTTO-->
                            <div style="float: left; margin-top: 2%;">
                                <!--INFORMAZIONI PRODOTTO-->
                                <div>
                                    <p><b>NomeCarta:</b> <%=prodotti.get(i).getNomeCarta()%></p>
                                    <p><b>Tipocarta:</b> <%=prodotti.get(i).getTipoCarta()%></p>
                                    <p><b>Edizione:</b> <%=prodotti.get(i).getEdizione()%></p>
                                    <p><b>Rarità:</b> <%=prodotti.get(i).getRarità()%></p>
                                    <p><b>Prezzo:</b> €<%=prodotti.get(i).getPrezzo()%></p>
    
                                </div>
                                
                                <!--FORM DEL PRODOTTO-->
                                <div>
                                    <div style="float: left;">
                                        <!--FORM PER IL BLOCCO/SBLOCCO DEL PRODOTTO-->
                                        <%if(prodotti.get(i).isBlocked()){%>
                                            <form name="sbloccaProdotto<%=i%>" action="Dispatcher" method="post">
                                                <input type="hidden" name="codiceProdotto" value="<%=prodotti.get(i).getCodiceProdotto()%>"/>
                                                <input type="hidden" name="controllerAction" value="ProdottoManagement.sbloccaProdotto"/>
                                                <input type="submit" value="Sblocca" class="button">
                                            </form>
                                        <%}else{%>
                                            <form name="bloccaProdotto<%=i%>" action="Dispatcher" method="post">
                                                <input type="hidden" name="codiceProdotto" value="<%=prodotti.get(i).getCodiceProdotto()%>"/>
                                                <input type="hidden" name="controllerAction" value="ProdottoManagement.bloccaProdotto"/>
                                                <input type="submit" value="Blocca" class="button">
                                            </form>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                               
                    </article>
                <%}%>
            </section>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
