<%-- 
    Document   : buoni
    Created on : 23-lug-2019, 14.26.15
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.mo.Buono"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    ArrayList<Buono> buoni = (ArrayList<Buono>) request.getAttribute("buoni");
    int numBuoni;
    if(buoni == null){
        numBuoni = 0;
    }else{
        numBuoni = buoni.size();
    }
    
    String mod = (String) request.getAttribute("mod");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Buoni";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/HeaderAdmin.inc"%>
        </header>
        
        <hr>
        
        <main>
            <div class="nomeBuono" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>
            
            <section>
                <!--BOTTONE INSERISCI BUONO-->
                <div style="float: left">
                    <form name="inserisciBuono" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="BuonoManagement.inserisciBuonoView"/>
                        <input type="submit" value="Nuovo buono" class="mainButton">
                    </form>
                </div>
            
                <!--FORM PER VISUALIZZARE TUTTI I BUONI RAGGRUPPATI PER NOME-->
                <%if(mod.equals("NomeBuono")){%>
                    <div style="float: left; margin-left: 5px;">
                        <form name="goBack" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="BuonoManagement.view"/>
                            <input type="submit" value="Indietro" class="mainButton">
                        </form>
                    </div>
                <%}%>
            </section>
            
            <div style="clear: both"></div>
            
            <!--SEZIONE PER LA VISUALIZZAZIONE DEI BUONI-->
            <section id="box" class="clearfix">
                <%for(i=0 ; i < numBuoni ; i++){%>           
                    <article>
                        <div>
                            <div>
                                <h3><%=buoni.get(i).getNomeBuono()%></h3>
                                <br>
                                <p>Data di scadenza: <%=buoni.get(i).getDataScadenzaString()%></p>
                                <p>Sconto: <%=buoni.get(i).getSconto()%>%</p>
                                <%if(mod.equals("NomeBuono")){%>
                                    <%if(buoni.get(i).isUsato()){%>
                                        <p>Usato</p>
                                    <%}else{%>
                                        <p>Non usato</p>
                                    <%}%>
                                    <%if(buoni.get(i).isEliminato()){%>
                                        <p style="color: red">Eliminato</p>
                                    <%}%>
                                <%}%>
                            </div>
                        
                            <div>
                                <div style="float: left; margin-right: 5px;">
                                    
                                    <!--FORM PER VISUALIZZARE TUTTI I BUONI CON IL NOME SPECIFICATO-->
                                    <%if(mod.equals("All")){%>
                                        <form name="seeAll<%=i%>" action="Dispatcher" method="post">
                                            <input type="hidden" name="nomeBuono" value="<%=buoni.get(i).getNomeBuono()%>"/>
                                            <input type="hidden" name="controllerAction" value="BuonoManagement.viewByName"/>
                                            <input type="submit" value="Vedi tutti" class="button">
                                        </form>
                                    <%}%>
                                </div>
                        
                                <%if(!buoni.get(i).isEliminato()){%>
                                    <div style="float: left;">
                                        <!--FORM PER LA CANCELLAZIONE DEI BUONI-->
                                        <form name="cancella<%=i%>" action="Dispatcher" method="post">
                                            <%if(mod.equals("All")){%>
                                                <input type="hidden" name="nomeBuono" value="<%=buoni.get(i).getNomeBuono()%>"/>
                                                <input type="hidden" name="controllerAction" value="BuonoManagement.cancellaByName"/>
                                            <%}else{%>
                                                <input type="hidden" name="codiceBuono" value="<%=buoni.get(i).getCodiceBuono()%>"/>
                                                <input type="hidden" name="controllerAction" value="BuonoManagement.cancellaByKey"/>
                                            <%}%>
                                            <input type="submit" value="Cancella" class="button">
                                        </form>
                                    </div>
                                 <%}%>
                            </div>
                        </div>
                    </article>
                <%}%>
            </section>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
