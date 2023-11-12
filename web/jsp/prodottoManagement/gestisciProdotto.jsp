<%-- 
    Document   : gestisciProdotto
    Created on : 5-mar-2020, 14.59.44
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.mo.Prodotto"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Gestisci prodotto";
    
    String action=(prodotto != null) ? "modify" : "insert";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            var action = "<%=action%>";
            
            function validateAndSubmit(){
                
                var nomeCarta = document.inserisciProdotto.nomeCarta.value;
                var tipoCarta = document.inserisciProdotto.tipoCarta.value;
                var rarita = document.inserisciProdotto.rarita.value;
                var edizione = document.inserisciProdotto.edizione.value;
                var prezzo = document.inserisciProdotto.prezzo.value;
                var immagine = document.inserisciProdotto.immagine.value;
                var testo = document.inserisciProdotto.testo.value;
                var quantita = document.inserisciProdotto.quantita.value;
                
                
                /*CONTROLLO IL CAMPO NOMECARTA*/
                if ((nomeCarta == "") || (nomeCarta == "undefined")) {
                    alert("Il campo nomeCarta è obbligatorio");
                    document.inserisciProdotto.nomeCarta.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO TIPOCARTA*/
                if ((tipoCarta == "") || (tipoCarta == "undefined")) {
                    alert("Il campo tipoCarta è obbligatorio");
                    document.inserisciProdotto.tipoCarta.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO RARITA*/
                if ((rarita == "") || (rarita == "undefined")) {
                    alert("Il campo rarità è obbligatorio");
                    document.inserisciProdotto.rarita.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO EDIZIONE*/
                if ((edizione == "") || (edizione == "undefined")) {
                    alert("Il campo edizione è obbligatorio");
                    document.inserisciProdotto.edizione.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO PREZZO*/
                if ((prezzo == "") || (prezzo == "undefined")) {
                    alert("Il campo Prezzo è obbligatorio");
                    document.inserisciProdotto.prezzo.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO IMMAGINE*/
                if ((immagine == "") || (immagine == "undefined")) {
                    alert("Il campo immagine è obbligatorio");
                   document.inserisciProdotto.immagine.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO QUANTITA*/
                if ((quantita == "") || (quantita == "undefined")) {
                    alert("Il campo immagine è obbligatorio");
                    document.inserisciProdotto.quantita.focus();
                    return false;
                }
                
                
                else{
                    if(action === "insert"){
                        document.inserisciProdotto.controllerAction.value = "ProdottoManagement.inserisciNelDB";
                    }else{
                        document.inserisciProdotto.controllerAction.value = "ProdottoManagement.modificaProdotto";
                    }
                    document.inserisciProdotto.submit();
                }
            }
            
            function goBack(){
                document.backForm.controllerAction.value = "ProdottoManagement.view";
                document.backForm.submit();
            }

            function mainOnLoadHandler(){
                document.inserisciProdotto.backButton.addEventListener("click", goBack);
                document.inserisciProdotto.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 61%;
                margin-left: 19%;
            }
            
            #nomeCarta{
                width: 75%;
                height: 22px;
                font-size: large;
            }
            
            #tipoCarta{
                width: 98%;
                height: 21px;
                font-size: large;
            }
            
            #rarita{
                width: 98%;
                height: 21px;
                font-size: large;
            }
            
            #prezzo{
                width: 98%;
                height: 21px;
                font-size: large;
            }
            
            #edizione{
                width: 75%;
                height: 22px;
                font-size: large;
            }
            
            #immagine{
                width: 75%;
                height: 22px;
                font-size: large;
            }
            
            #quantita{
                width: 75%;
                height: 22px;
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
            
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>
            
            <div class="content">
                <%if(action.equals("modify")){%>
                    <div>
                        <h2>MODIFICA PRODOTTO</h2>
                    </div>
                <%}else{%>
                    <div>
                        <h2>AGGIUNGI UN PRODOTTO</h2>
                    </div>
                <%}%>
            
                <!--FORM PER L'INSERIMENTO O LA MODIFICA DI UN PRODOTTO-->
                <form name="inserisciProdotto" action="Dispatcher" method="post">

                    <div class="form" id="left" style="width: 48%;">
                        <label for="nomeCarta">Nome della Carta: </label>
                        <input type="text" id="nomeCarta" name="nomeCarta" value="<%=(action.equals("modify")) ? prodotto.getNomeCarta() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label for="tipoCarta">Tipo di Carta: </label>
                        <input type="text" id="tipoCarta" name="tipoCarta" value="<%=(action.equals("modify")) ? prodotto.getTipoCarta() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label for="edizione">Edizione: </label>
                        <input type="text" id="edizione" name="edizione" value="<%=(action.equals("modify")) ? prodotto.getEdizione() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label for="rarita">Rarità: </label>
                        <input type="text" id="rarita" name="rarita" value="<%=(action.equals("modify")) ? prodotto.getRarità(): ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label for="quantita">Quantità: </label>
                        <input type="text" id="quantita" name="quantita" value="<%=(action.equals("modify")) ? prodotto.getQuantità() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label for="prezzo">Prezzo: </label>
                        <input type="text" id="prezzo" name="prezzo" value="<%=(action.equals("modify")) ? prodotto.getPrezzo() : ""%>" required maxlength="20"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label for="immagine">Nome immagine: </label>
                        <input type="text" id="immagine" name="immagine" value="<%=(action.equals("modify")) ? prodotto.getImmagine() : ""%>" required maxlength="100"/>
                    </div>
                    
                    <div class="form" id="left" style="width: 48%;">
                        <label>Push: </label>
                        <input type="radio" name="push" value="S" checked>S&iacute;
                        <input type="radio" name="push" value="N">No
                    </div>
                    
                    <div class="form" style="float: left; width: 48%;">
                        <label>Blocked: </label>
                        <input type="radio" name="blocked" value="S" checked>S&iacute;
                        <input type="radio" name="blocked" value="N">No
                    </div>
                    
                    <div style="clear: both"></div>
                    
                    <div class="form">
                        <label for="testo">Testo: </label>
                        <textarea id="testo" name="testo" cols="100" rows="10" wrap="soft" required><%=(action.equals("modify")) ? prodotto.getTesto() : ""%></textarea>
                    </div>
                    
                    <input type="hidden" name="controllerAction"/>
                    <%if(action.equals("modify")){%>
                        <input type="hidden" name="codiceProdotto" value="<%=prodotto.getCodiceProdotto()%>"/>
                    <%}%>
                    
                    <!--BOTTONI DI CONFERMA O DI ANNULLA-->
                    <input type="button" name="submitButton" value="Ok" class="button">
                    <input type="button" name="backButton" value="Annulla" class="button">
                    
                </form>
            </div>
                    
            <!--FORM PER ANNULLARE-->
            <form name="backForm" method="post" action="Dispatcher"> 
                <input type="hidden" name="controllerAction"/>
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
