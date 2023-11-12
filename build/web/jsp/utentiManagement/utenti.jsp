<%-- 
    Document   : utenti
    Created on : 5-mar-2020, 10.05.37
    Author     : Giacomo Polastri
--%>

<%@page session = "false"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    String selectedInitial = (String) request.getAttribute("selectedInitial");
    
    ArrayList<String> initials = (ArrayList<String>) request.getAttribute("initials");
    int numInitials;
    if(initials == null){
        numInitials = 0;
    }else{
        numInitials = initials.size();
    }
    
    ArrayList<Utente> utenti = (ArrayList<Utente>) request.getAttribute("utenti");
    int numUtenti;
    if(utenti == null){
        numUtenti = 0;
    }else{
        numUtenti = utenti.size();
    }
    
    ArrayList<Integer> numeroOrdini = (ArrayList<Integer>) request.getAttribute("numeroOrdini");
    int numOrdini;
    if(numeroOrdini == null){
        numOrdini = 0;
    }else{
        numOrdini = numeroOrdini.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    LoggedUser ul = (LoggedUser) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Utenti";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <style>
            .initial {  
                color: black;
            }

            .selectedInitial { 
                color: steelblue;
            }
            .email{
                font-size: 0.9em;
            }
        </style>
        <script language="javascript">
            function changeInitial(inital){
                document.changeInitialForm.selectedInitial.value = inital;
                document.changeInitialForm.submit();
            }
            
            function bloccaUtente(email){
                document.bloccaUtenteForm.email.value = email;
                document.bloccaUtenteForm.submit();
            }
            
            function sbloccaUtente(email){
                document.sbloccaUtenteForm.email.value = email;
                document.sbloccaUtenteForm.submit();
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
            
            <section>
                <!--FORM PER PASSARE ALLA PAGINA DI REGISTRAZIONE DI UN NUOVO UTENTE-->
                <form name="inserisciUtente" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="LogOn.view"/>
                    <input type="hidden" name="opzione" value="R"/>
                    <input type="submit" value="Nuovo utente" class="mainButton">
                </form>
            </section>
            
            <!--MINI MENU DI NAVIGAZIONE CON LE INIZIALI DEI NOMI DEGLI UTENTI-->
            <nav style="margin-top: 15px;">
                <%if(selectedInitial.equals("*")){%>
                    <span class="selectedInitial">*</span>
                <%}else{%>
                    <a class="initial" href="javascript:changeInitial('*');">*</a>
                <%}%>
                
                <%for(i=0 ; i<numInitials ; i++){
                    if(initials.get(i).equals(selectedInitial)){%>
                        <span class="selectedInitial"><%=initials.get(i)%></span>
                    <%}else{%>
                        <a class="initial" href="javascript:changeInitial('<%=initials.get(i)%>');"><%=initials.get(i)%></a>
                    <%}%>  
                <%}%>
            </nav>
            
            <section id="box" class="clearfix">
                
                <!--LISTA DEGLI UTENTI DA MOSTRARE-->
                <%for (i=0 ; i<numUtenti ; i++) {%>           
                    <article>
                        <h1><%=utenti.get(i).getNomeUtente()%> <%=utenti.get(i).getCognome()%></h1>
                        <span class="email"><%=utenti.get(i).getEmail()%></span>
                        <address>
                            <%=utenti.get(i).getVia()%> n.<%=utenti.get(i).getNumeroCivico()%><br/>
                            <%=utenti.get(i).getCittà()%>, <%=utenti.get(i).getNazione()%><br/>   
                        </address>
                        <%if(!utenti.get(i).isAdmin()){%>
                            <p>Ordini effettuati: <%=numeroOrdini.get(i)%></p>
                        <%}else{%>
                            <p>Admin</p>
                        <%}%>
                        
                        <!--BOTTONI PER BLOCCARE O SBLOCCARE UN UTENTE-->
                        <%if(!utenti.get(i).isBlocked()){%>
                            <a class="button" href="javascript:bloccaUtente('<%=utenti.get(i).getEmail()%>');">Blocca</a>
                        <%}else{%>
                            <a class="button" href="javascript:sbloccaUtente('<%=utenti.get(i).getEmail()%>');">Sblocca</a>
                        <%}%>
                    </article>
                <%}%>
            </section>
            
            <!--FORM PER CAMBIARE IL FILTRO DEGLI UTENTI IN BASE ALL'INIZIALE SELEZIONATA-->
            <form name="changeInitialForm" method="post" action="Dispatcher">
                <input type="hidden" name="selectedInitial"/>
                <input type="hidden" name="controllerAction" value="HomeManagement.view"/>      
            </form>
            
            <!--FORM PER BLOCCARE L'UTENTE SELEZIONATO-->
            <form name="bloccaUtenteForm" method="post" action="Dispatcher">
                <input type="hidden" name="selectedInitial" value="<%=selectedInitial%>"/>
                <input type="hidden" name="email"/>
                <input type="hidden" name="controllerAction" value="HomeManagement.bloccaUtente"/>      
            </form>
                
            <!--FORM PER SBLOCCARE L'UTENTE SELEZIONATO-->
            <form name="sbloccaUtenteForm" method="post" action="Dispatcher">
                <input type="hidden" name="selectedInitial" value="<%=selectedInitial%>"/>
                <input type="hidden" name="email"/>
                <input type="hidden" name="controllerAction" value="HomeManagement.sbloccaUtente"/>      
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
