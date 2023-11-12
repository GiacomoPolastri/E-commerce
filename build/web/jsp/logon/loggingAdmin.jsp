<%-- 
    Document   : loggingAdmin
    Created on : 5-mar-2020, 14.56.09
    Author     : Utente
--%>
<%@page session = "false"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="model.session.mo.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");

    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Logging";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>

        <script language="javascript">
            var loggedOn = <%=loggedOn%>;

            function validateAndSubmit() {
                var nome = document.registerForm.nome.value;
                var cognome = document.registerForm.cognome.value;
                var email = document.registerForm.email.value;
                var password = document.registerForm.password.value;
                var conferma = document.registerForm.passwordConf.value;
                var nazione = document.registerForm.nazione.value;
                var citta = document.registerForm.citta.value;
                var via = document.registerForm.via.value;
                var numeroCivico = document.registerForm.numeroCivico.value;
                var CAP = document.registerForm.CAP.value;

                /*CONTROLLO IL CAMPO NOME*/
                if ((nome == "") || (nome == "undefined")) {
                    alert("Il campo Nome è obbligatorio");
                    document.registerForm.nome.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO COGNOME*/
                else if ((cognome == "") || (cognome == "undefined")) {
                    alert("Il campo Cognome è obbligatorio");
                    document.registerForm.cognome.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO EMAIL*/
                else if ((email == "") || (email == "undefined")) {
                    alert("Il campo E-Mail è obbligatorio");
                    document.registerForm.email.focus();
                    return false;
                } else if (!checkEmail(email)) {
                    alert("Il formato dell'email inserita non è valido");
                    document.registerForm.email.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO PASSWORD*/
                else if ((password == "") || (password == "undefined")) {
                    alert("Il campo Password è obbligatorio");
                    document.registerForm.password.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CONFERMA PASSWORD*/
                else if ((conferma == "") || (conferma == "undefined")) {
                    alert("Il campo Conferma password è obbligatorio");
                    document.registerForm.conferma.focus();
                    return false;
                }

                /*CONTROLLO L'UGUAGLIANZA TRA PASSWORD E CONFERMA PASSWORD*/
                else if (password != conferma) {
                    alert("La password confermata è diversa da quella scelta");
                    document.registerForm.conferma.value = "";
                    document.registerForm.conferma.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO NAZIONE*/
                else if ((nazione == "") || (nazione == "undefined")) {
                    alert("Il campo Nazione è obbligatorio");
                    document.registerForm.nazione.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CITTA*/
                else if ((citta == "") || (citta == "undefined")) {
                    alert("Il campo Citta è obbligatorio");
                    document.registerForm.citta.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO VIA*/
                else if ((via == "") || (via == "undefined")) {
                    alert("Il campo Via è obbligatorio");
                    document.registerForm.via.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO NUMERO CIVICO*/
                else if ((numeroCivico == "") || (numeroCivico == "undefined")) {
                    alert("Il campo Numero civico è obbligatorio");
                    document.registerForm.numeroCivico.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CAP*/
                else if ((CAP == "") || (CAP == "undefined")) {
                    alert("Il campo CAP è obbligatorio");
                    document.registerForm.CAP.focus();
                    return false;
                }

                /*INVIO IL MODULO*/
                else {
                    document.registerForm.controllerAction.value = "LogOn.registraAdmin";
                    document.registerForm.submit();
                }
            }

            function validaEmail(email) {
                var regexp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return regexp.test(email);
            }

            function checkEmail(email) {
                if (validaEmail(email)) {
                    return true;
                } else {
                    return false;
                }
            }

            function goBack() {
                var f = document.backForm;
                f.controllerAction.value = "HomeAdmin.view";
                f.submit();
            }

            function mainOnLoadHandler() {
                document.registerForm.backButton.addEventListener("click", goBack);
                document.registerForm.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 56%; 
                margin-left: 22%;
            }

            #nome{
                width: 74%;
                height: 22px;
                font-size: large;
            }

            #cognome{
                width: 67%;
                height: 22px;
                font-size: large;
            }

            #email{
                width: 87%;
                height: 22px;
                font-size: large;
            }

            #password{
                width: 83%;
                height: 22px;
                font-size: large;
            }

            #passwordConf{
                width: 71%;
                height: 22px;
                font-size: large;
            }

            #nazione{
                width: 70%;
                height: 22px;
                font-size: large;
            }

            #citta{
                width: 77%;
                height: 22px;
                font-size: large;
            }

            #via{
                width: 82%;
                height: 22px;
                font-size: large;
            }

            #numeroCivico{
                width: 25%;
                height: 22px;
                font-size: large;
            }

            #CAP{
                width: 44%;
                height: 22px;
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
            <%@include file="/include/HeaderAdmin.inc"%>
        </header>

        <hr>

        <main>
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNomeUtente()%> <%=ul.getCognome()%></p>
            </div>

            <div class="content">
                <div>
                    <h2>CREA UN ACCOUNT</h2>
                </div>

                <!--FORM DI REGISTRAZIONE O DI LOGIN-->
                <form name="registerForm" action="Dispatcher" method="post">

                    <div class="form" id="left" style="width: 46%;">
                        <label for="nome">Nome:* </label>
                        <input type="text" id="nome" name="nome" value="" maxlength="20" required placeholder="Mario"/>
                    </div>

                    <div class="form" style="float: left; width: 50%;">
                        <label for="cognome">Cognome:* </label>
                        <input type="text" id="cognome" name="cognome" value="" maxlength="20" required placeholder="Rossi"/>
                    </div>

                    <div style="clear: both"></div>

                    <div class="form">
                        <label for="email">E-Mail:* </label>
                        <input type="email" id="email" name="email" autocomplete="username email" value="" maxlength="50" required placeholder="mario.rossi@gmail.com"/>
                    </div>

                    <div class="form">
                        <label for="password">Password:* </label>
                        <input type="password" id="password" name="password" autocomplete="new-password" maxlength="50" required/>
                    </div>

                    <div class="form">
                        <label for="passwordConf">Conferma Password:* </label>
                        <input type="password" id="passwordConf" name="passwordConf" autocomplete="new-password" maxlength="50" required/>
                    </div>

                    <div class="form">
                        <label>Sesso:* </label>
                        <input type="radio" name="genere" value="M" checked>Maschio
                        <input type="radio" name="genere" value="F">Femmina
                    </div>

                    <div class="form" id="left" style="width: 47%;">
                        <label for="nazione">Nazione:* </label>
                        <input type="text" id="nazione" name="nazione" value="" maxlength="20" required placeholder="Italia"/>
                    </div>

                    <div class="form" style="float: left; width: 49%;">
                        <label for="citta">Citt&agrave;:* </label>
                        <input type="text" id="citta" name="citta" value="" maxlength="20" required placeholder="Roma"/>
                    </div>

                    <div class="form" id="left" style="width: 45%;">
                        <label for="via">Via:* </label>
                        <input type="text" id="via" name="via" value="" maxlength="20" required placeholder="Via Garibaldi"/>
                    </div>

                    <div class="form" id="left" style="width: 29%;">
                        <label for="numeroCivico">Numero civico:* </label>
                        <input type="text" id="numeroCivico" name="numeroCivico" value="" maxlength="10" required placeholder="1"/>
                    </div>

                    <div class="form" style="float: left; width: 18%;">
                        <label for="CAP">CAP:* </label>
                        <input type="text" id="CAP" name="CAP" value="" maxlength="5" required placeholder="12345"/>
                    </div>

                    <div style="clear: both"></div>
                    <div class="form" style="float: left; width: 50%;">
                        <label>Admin:* </label>
                        <input type="radio" name="admin" value="S" checked>S&iacute
                        <input type="radio" name="admin" value="N">No
                    </div>

                    <div class="form" style="float: left">
                        <label>Bloccato:* </label>
                        <input type="radio" name="blocked" value="S" checked>S&iacute
                        <input type="radio" name="blocked" value="N">No
                    </div>

                    <input type="hidden" name="controllerAction"/>

                    <div style="clear: both"></div>

                    <div style="margin-top: 10px;">
                        <input type="button" name="submitButton" value="Ok" class="button">
                        <input type="button" name="backButton" value="Annulla" class="button">
                    </div>

                </form>

                <div style="margin-top: 20px;">
                    <p id="clausola">*: Campo obbligatorio</p>
                </div>
            </div>

            <!--FORM DI ANNULLA => TORNO NELLA HOME DEGLI UTENTI O DELL'ADMIN-->
            <form name="backForm" method="post" action="Dispatcher"> 
                <input type="hidden" name="controllerAction"/>
            </form>

        </main>

        <%@include file="/include/footer.inc" %>

    </body>
</html>

