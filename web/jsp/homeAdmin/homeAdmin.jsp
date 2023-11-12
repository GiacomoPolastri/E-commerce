<%-- 
    Document   : homeAdmin
    Created on : 5-mar-2020, 14.55.28
    Author     : Utente
--%>

<%@page session = "false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Home amministratore";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function UtentiManagementSubmit(){
                var f = document.utentiManagement;
                f.submit();
            }
            
            function ordiniSubmit(){
                var f = document.ordini;
                f.submit();
            }
            
            function magazzinoSubmit(){
                var f = document.magazzino;
                f.submit();
            }
            
            function buoniSubmit(){
                var f = document.buoni;
                f.submit();
            }
        </script>
        <style>
            .content{
                margin-top: 15px;
                margin-left: 5%;
                width: 90%;
            }
            
            .image{
                width: 25%;
                float: left;
            }
            
            #linkImage{
                width: 150px;
                height: 150px;
                margin-left: 22%;
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
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE ORDINI-->
                <div class="image">
                    <form name="ordini" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="Ordini.view"/>
                        <a href="javascript:ordiniSubmit();">
                            <img id="linkImage" src="images/Ordini.png" alt="Ordini"/></br><p style="text-align: center"><b>GESTIONE ORDINI</b></p>
                        </a>
                    </form>
                </div>
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE UTENTI-->
                <div class="image">
                    <form name="utentiManagement" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
                        <a href="javascript:UtentiManagementSubmit();">
                            <img id="linkImage" src="images/Utenti.png" alt="Utenti"/></br><p style="text-align: center"><b>GESTIONE UTENTI</b></p>
                        </a>
                    </form>
                </div>
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE PRODOTTI-->
                <div class="image">
                    <form name="magazzino" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="ProdottoManagement.view"/>
                        <a href="javascript:magazzinoSubmit();">
                            <img id="linkImage" src="images/Magazzino.png" alt="Magazzino"/></br><p style="text-align: center"><b>GESTIONE MAGAZZINO</b></p>
                        </a>
                    </form>
                </div>
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE BUONI-->
                <div class="image">
                    <form name="buoni" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="BuonoManagement.view"/>
                        <a href="javascript:buoniSubmit();">
                            <img id="linkImage" src="images/Buoni.png" alt="Buoni"/></br><p style="text-align: center"><b>GESTIONE BUONI</b></p>
                        </a>
                    </form>
                </div>
            </div>
            
            <div style="clear: both; margin-bottom: 15px;"></div>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
