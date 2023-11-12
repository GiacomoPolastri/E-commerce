<%-- 
    Document   : newjspviewProdotto
    Created on : 5-mar-2020, 14.54.59
    Author     : Utente
--%>
<%@page session = "false"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="model.mo.Prodotto"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    ArrayList<String> tipoCarte = (ArrayList<String>) request.getAttribute("tipoCarte");
    int numTipoCarte;
    if(tipoCarte == null){
        numTipoCarte = 0;
    }else{
        numTipoCarte = tipoCarte.size();
    }
    
    ArrayList<String> rare = (ArrayList<String>) request.getAttribute("rare");
    int numRare;
    if(rare == null){
        numRare = 0;
    }else{
        numRare = rare.size();
    }
    
    ArrayList<String> edizioni = (ArrayList<String>) request.getAttribute("edizioni");
    int numEdizioni;
    if(edizioni == null){
        numEdizioni = 0;
    }else{
        numEdizioni = edizioni.size();
    }
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Vista Prodotto";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            var logged = <%=loggedOn%>;
    
            function searchProdoctByStringSubmit(){
                var f = document.searchProdoctByString;
                f.submit();
                return;
            }
            
            function TipoCarteFormSubmit(index){
                var f = document.forms["tipoCarteForm" + index];
                f.submit();
                return;
            }
            
            function RareFormSubmit(index){
                var f = document.forms["rareForm" + index];
                f.submit();
                return;
            }
            
            function EdizioniFormSubmit(index){
                var f = document.forms["edizioniForm" + index];
                f.submit();
                return;
            }
            
            function carrelloSubmit(codiceProdotto){
                var f = document.carrelloForm;
                if(logged){
                    f.codiceProdotto.value = codiceProdotto;
                    alert("Prodotto inserito nel carrello");
                    f.submit();
                }else{
                    alert("Per inserire un prodotto nel carrello bisogna eseguire l'accesso");
                }
                return;
            }
        </script>
        
        <style>
            img {
                float: left;
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
                <%if(loggedOn){%>
                    <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
                <%}else{%>
                    <p>Benvenuto, fai il login per procedere all'acquisto dei tuoi prodotti</p>
                <%}%>
            </div>
            
            <!--BARRA LATERALE PER LA RICERCA DEI PRODOTTI-->
            <aside class="left_content">
                
                <section>
                    <h1>TipoCarte</h1>
                    <ul>
                        <%for(i = 0; i < numTipoCarte; i++){%>
                            <li>
                                <form name="tipoCarteForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="tipoCarta"/>
                                        <input type="hidden" name="searchName" value="<%=tipoCarte.get(i)%>"/>
                                        <a href="javascript:TipoCarteFormSubmit(<%=i%>);"><%=tipoCarte.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                    
                <hr>
                
                <section>
                    <h1>Rare</h1>
                    <ul>
                        <%for(i = 0; i < numRare; i++){%>
                            <li>
                                <form name="rareForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="rarita"/>
                                        <input type="hidden" name="searchName" value="<%=rare.get(i)%>"/>
                                        <a href="javascript:RareFormSubmit(<%=i%>);"><%=rare.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                    
                <hr>
                    
                <section>
                    <h1>Edizioni</h1>
                    <ul>
                        <%for(i = 0; i < numEdizioni; i++){%>
                            <li>
                                <form name="edizioniForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="edizioni"/>
                                        <input type="hidden" name="searchName" value="<%=edizioni.get(i)%>"/>
                                        <a href="javascript:EdizioniFormSubmit(<%=i%>);"><%=edizioni.get(i)%></a>
                                </form>
                            </li>
                        <%}%>
                    </ul>
                </section>
                            
            </aside>
                    
            <!--BARRA DI RICERCA LIBERA-->
            <section id="search">
                <div class="searchBar">
                    <form name="searchProdoctByString" action="Dispatcher" method="post">
                        <input type="text" id="searchName" name="searchName" maxlength="100" placeholder="Cerca...">
                        <input type="hidden" name="searchType" value="searchString"/>
                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                        <a href="javascript:searchProdoctByStringSubmit();">CERCA</a>
                    </form>
                </div>
            </section>
            
            <!--INFORMAZIONI PRODOTTO-->
            <div style="float: left; width: 88%;">
                <div style="float: left; margin-left: 70px;">
                    <img id="ProdImage" src="images/<%=prodotto.getImmagine()%>" width="400" height="400" alt="Visualizza prodotto"/>
                </div>
                
                <div style="float: left; margin-left: 70px;">
                    <span>tipoCarta: <%=prodotto.getTipoCarta()%></span>
                    <br/>
                    <span>rarita: <%=prodotto.getRarità()%></span>
                    <br/>
                    <span>Prezzo: €<%=prodotto.getPrezzo()%></span>
            
                    <!--FORM DI INSERIMENTO PRODOTTO NEL CARRELLO-->
                    <section>
                        <div>
                            <form name="carrelloForm" action="Dispatcher" method="post">
                                <input type="hidden" name="controllerAction" value="Catalogo.insert"/>
                                <input type="hidden" name="codiceProdotto"/>
                                    
                                </div> 
            
                                <div>
                                    <label for="quantita">Quantit&agrave: </label>
                                    <input type="number" id="quantita" name="quantita" value="1" min="1" max="30" step="1"/>
                                </div>
                            
                                <div style="margin-top: 70px;">
                                    <a href="javascript:carrelloSubmit(<%=prodotto.getCodiceProdotto()%>);" id="ordina">
                                        Aggiungi al carello
                                    </a>
                                </div>
                            </form>
                        </div>
                    </section>
                </div>
                
                <div class="descrizione">
                    <!--DESCRIZIONE PRODOTTO-->
                    <p><b>Descrizione prodotto</b></p>
                    </br>
                    <p><%=prodotto.getTesto()%></p>
                </div>
            </div>
            
            <div style="clear: both;">
            </div>
            
        </main>
        
            <%@include file="/include/footer.inc" %>
            
    </body>
</html>
