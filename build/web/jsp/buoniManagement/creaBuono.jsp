<%-- 
    Document   : creaBuono
    Created on : 5-mar-2020, 14.54.22
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Crea Buoni";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function validateAndSubmit(){
                var nomeBuono = document.creaBuono.nomeBuono.value;
                var sconto = document.creaBuono.sconto.value;
                var dataScadenza = document.creaBuono.dataScadenza.value;
                var quantita = document.creaBuono.quantita.value;
                
                /*CONTROLLO IL CAMPO NOME*/
                if ((nomeBuono == "") || (nomeBuono == "undefined")) {
                    alert("Il campo Nome è obbligatorio");
                    document.creaBuono.nomeBuono.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO SCONTO*/
                else if ((sconto == "") || (sconto == "undefined")) {
                    alert("Il campo Sconto è obbligatorio");
                    document.creaBuono.sconto.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO DATA*/
                else if ((dataScadenza == "") || (dataScadenza == "undefined")) {
                    alert("Il campo Data è obbligatorio");
                    document.creaBuono.dataScadenza.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO QUANTITA*/
                else if ((quantita == "") || (quantita == "undefined")) {
                    alert("Il numero di buoni è obbligatorio");
                    document.creaBuono.quantita.focus();
                    return false;
                }
                
                /*INVIO IL MODULO*/
                else{
                    
                    document.creaBuono.submit();
                }
            }
            
            function goBack(){
                document.backForm.submit();
            }

            function mainOnLoadHandler(){
                document.creaBuono.backButton.addEventListener("click", goBack);
                document.creaBuono.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 40%;
                margin-left: 30%;
            }
            
            #nome{
                width: 86%;
                height: 30px;
                font-size: large;
            }
            
            #sconto{
                width: 79%;
                height: 30px;
                font-size: large;
            }
            
            #data{
                width: 66%;
                height: 30px;
                font-size: large;
            }
            
            #quantita{
                width: 46%;
                height: 30px;
                font-size: large;
            }
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/HeaderAdmin.inc"%>
        </header>
        
        <hr>
        
        <main>
            <div class='nome'>
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>
            
            <div class="content">
                <div>
                    <h2>CREA BUONO</h2>
                </div>
            
                <!--FORM PER LA CREAZIONE DI NUOVI BUONI-->
                <section>
                    <div>
                        <form name="creaBuono" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="BuonoManagement.inserisciBuono"/>
                            <div class="form">
                                <label for="nomeBuono">Nome: </label>
                                <input type="text" id="nomeBuono" name="nomeBuono" value="" required maxlength="20"/>                        
                            </div>
                            <div class="form">
                                <label for="sconto">Sconto: </label>
                                <input type="text" id="sconto" name="sconto" value="" required/> %                      
                            </div>
                            <div class="form">
                                <label for="dataScadenza">Data di Scadenza: </label>
                                <input type="date" id="dataScadenza" name="dataScadenza" max="2020-12-31"/>
                            </div> 
                            <div class="form">
                                <label for="quantita">Numero di buoni da generare: </label>
                                <input type="text" id="quantita" name="quantita" value="" required/>
                            </div>
                            <div>
                                <input type="button" name="submitButton" value="Genera" class="button">
                                <input type="button" name="backButton" value="Annulla" class="button">
                            </div>
                        </form>
                    
                        <!--FORM PER ANNULLARE-->
                        <form name="backForm" method="post" action="Dispatcher"> 
                            <input type="hidden" name="controllerAction" value="BuonoManagement.view"/>
                        </form>
                    </div>
                </section>
            </div>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
