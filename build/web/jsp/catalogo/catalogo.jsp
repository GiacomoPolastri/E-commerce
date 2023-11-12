<%-- 
    Document   : catalogo
    Created on : 5-mar-2020, 14.54.40
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.session.mo.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    ArrayList<String> tipoCarte = (ArrayList<String>) request.getAttribute("tipoCarte");
    int numTipi;
    if(tipoCarte == null){
        numTipi = 0;
    }else{
        numTipi = tipoCarte.size();
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
    
    String menuActiveLink = "Catalogo";
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
            
            function searchProdoctByStringSubmit(){
                var f = document.searchProdoctByString;
                f.submit();
                return;
            }
            
            function TipoCartaFormSubmit(index){
                var f = document.forms["tipoCartaForm" + index];
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
        </script>
        
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
                        <%for(i = 0; i < numTipi; i++){%>
                            <li>
                                <form name="tipoCartaForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.view"/>
                                        <input type="hidden" name="searchType" value="tipoCarta"/>
                                        <input type="hidden" name="searchName" value="<%=tipoCarte.get(i)%>"/>
                                        <a href="javascript:TipoCartaFormSubmit(<%=i%>);"><%=tipoCarte.get(i)%></a>
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
                                        <input type="hidden" name="searchType" value="edizione"/>
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
                
            <div style="clear: right;"></div>
            
            <!--LISTA PRODOTTI DA VISUALIZZARE-->
            <%if(numProdotto == 0){%>
            <div style='margin-top: 20px; margin-left: 163px;'>
                <h2>Attenzione</h2>
                <p>La ricerca effettuata non ha prodotto risultati. Per visionare il catalogo degli articoli naviga su <a href="Dispatcher?controllerAction=Catalogo.view">Tana del goblin</a> e, quando trovi un articolo che ti interessa, clicca su "Aggiungi al carrello"</p>
            </div>
            <%}else{%>
            <section class="clearfix">
                <div style="float: left; width: 88%;">
                    <%for (i = 0; i < numProdotto; i++) {%>           
                        <article>
                            <div class="right_content">
                                <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="Catalogo.viewProdotto"/>
                                    <input type="hidden" name="codiceProdotto"/>
                                    <a href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                        <img src="images/<%=prodotti.get(i).getImmagine()%>" width="230" height="260" alt="Visualizza prodotto"/>
                                    </a>
                                </form>
                                <span><b><%=prodotti.get(i).getNomeCarta()%></b></span>
                                <br/>
                                <span>€<%=prodotti.get(i).getPrezzo()%></span>
                            </div>
                        </article>
                    <%}%>
                </div>
            </section>
            <%}%>
               
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
    
</html>
