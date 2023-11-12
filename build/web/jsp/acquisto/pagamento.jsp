<%-- 
    Document   : pagamento
    Created on : 5-mar-2020, 14.33.34
    Author     : Giacomo Polastri
--%>
<%@page session = "false"%>
<%@page import="model.session.mo.Carrello"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Modulo ordine";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        
        <script language="javascript">
            function procedi(){
                var campi = true;
                var f = document.pagamentoForm;
                
                var cartaPagamento = f.cartaPagamento.value;
                var nazione = f.nazione.value;
                var citta = f.citta.value;
                var via = f.via.value;
                var numeroCivico = f.numeroCivico.value;
                var CAP = f.CAP.value;
                    
                /*CONTROLLO IL CAMPO CARTA*/
                if ((cartaPagamento == "") || (cartaPagamento == "undefined")) {
                    alert("Il campo Carta è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO NAZIONE*/
                if ((nazione == "") || (nazione == "undefined")) {
                    alert("Il campo Nazione è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO CITTA*/
                if ((citta == "") || (citta == "undefined")) {
                    alert("Il campo Citta è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO VIA*/
                if ((via == "") || (via == "undefined")) {
                    alert("Il campo Via è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO NUMERO CIVICO*/
                if ((numeroCivico == "") || (numeroCivico == "undefined")) {
                    alert("Il campo Numero Civico è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO CAP*/
                if ((CAP == "") || (CAP == "undefined")) {
                    alert("Il campo CAP è obbligatorio");
                    campi = false;
                }
                
                if(campi){
                    f.controllerAction.value = "Acquisto.procedi";
                    if(f.codiceBuono.value === ""){
                        f.codiceBuono.value = 0;/*Codice di default se non ne è stato inserito nessuno*/
                    }
                    f.submit();
                }
            }
            
            function goBack(){
                var f = document.backForm;
                f.controllerAction.value = "Acquisto.view";
                f.submit();
            }

            function mainOnLoadHandler(){
                document.pagamentoForm.backButton.addEventListener("click", goBack);
                document.pagamentoForm.submitButton.addEventListener("click", procedi);
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
            
            #nazione{
                width: 350px;
                height: 20px;
                font-size: large;
            }
            
            #citta{
                width: 376px;
                height: 20px;
                font-size: large;
            }
            
            #via{
                width: 388px;
                height: 20px;
                font-size: large;
            }
            
            #nc{
                float: left;
                width: 48%;
                margin-right: 4%;
            }
            
            #numeroCivico{
                width: 98px;
                height: 20px;
                font-size: large;
            }
            
            #cap{
                float: left;
                width: 48%;
            }
            
            #CAP{
                width: 98px;
                height: 20px;
                font-size: large;
            }
            
            #carta{
                width: 300px;
                height: 20px;
                font-size: large;
            }
            
            #codiceBuono{
                width: 250px;
                height: 20px;
                font-size: large;
            }
            
            #clausola{
                font-size: smaller;
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
                <h2>Step 1: Inserimento dati</h2>
            </div>
            
            <!--FORM PER L'INSERIMENTO DEI DATI PER L'ORDINE-->
            <div class="clearfix">
                <form name="pagamentoForm" action="Dispatcher" method="post">
                    
                    <!--LATO DI SINISTRA DEDICATO ALL'INDIRIZZO DI CONSEGNA-->
                    <div class="indirizzo">
                        <h3>Indirizzo di consegna</h3>
                        </br>
                        
                        <div class="form">
                            <label for="nazione">Nazione:* </label>
                            <input type="text" id="nazione" name="nazione" value="" maxlength="20" required placeholder="Italia"/>
                        </div>
                
                        <div class="form">
                            <label for="citta">Citt&agrave;:* </label>
                            <input type="text" id="citta" name="citta" value="" maxlength="20" required placeholder="Roma"/>
                        </div>
                
                        <div class="form">
                            <label for="via">Via:* </label>
                            <input type="text" id="via" name="via" value="" maxlength="20" required placeholder="Via Garibaldi"/>
                        </div>
                
                        <div class="form" id="nc">
                            <label for="numeroCivico">Numero civico:* </label>
                            <input type="text" id="numeroCivico" name="numeroCivico" value="" maxlength="11" required placeholder="1"/>
                        </div>
                    
                        <div class="form" id="cap">
                            <label for="CAP">CAP:* </label>
                            <input type="text" id="CAP" name="CAP" value="" maxlength="5" required placeholder="12345"/>
                        </div>
                    </div>
                    
                    <!--LATO DI DESTRA DEDICATO ALLA CARTA E AL BUONO SCONTO-->
                    <div class="pagamento">
                        <h3>Dati carta di credito</h3>
                        </br>
                
                        <div class="form">
                            <label for="cartaPagamento">Numero carta:* </label>
                            <input type="text" id="cartaPagamento" name="cartaPagamento" value="" maxlength="16" required/>
                        </div>
                
                        </br>
                        <h3>Dati buono sconto</h3>
                        </br>
                
                        <div class="form">
                            <label for="codiceBuono">Codice buono sconto: </label>
                            <input type="text" id="codiceBuono" name="codiceBuono" value="" maxlength="11"/>
                        </div>
                
                        <input type="hidden" name="controllerAction"/>
                    </div>
                    <div style="clear: both">
                        <input type="button" name="submitButton" value="procedi" class="button" style="font-size: medium;">
                        <input type="button" name="backButton" value="Annulla" class="button" style="font-size: medium;">
                    </div>
                
                </form>
                
                <div style="margin-top: 20px;">
                    <p id="clausola">*: Campo obbligatorio</p>
                </div>
            </div>
                
            <!--FORM DI ANNULLA => TORNO NELLA VISTA DEL CARRELLO-->
            <form name="backForm" method="post" action="Dispatcher"> 
                <input type="hidden" name="controllerAction"/>
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
