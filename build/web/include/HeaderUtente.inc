<header class="clearfix"><!-- Defining the header section of the page -->

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="LogOn.logout"/>
    </form>
    
    <div style="height: 126px;">
        <nav class="clearfix">
            <div class="logo">
                <a href="Dispatcher?controllerAction=Catalogo.view">
                    <img id="LogoWeb" src="images/tanadeigoblin.jpg" width="370" height="105" alt="Torna alla Home Page"/>
                </a>
            </div>
            <div class="barra">
                <%if(loggedOn) {/*Se non sono loggato faccio vedere i tasti carrello, ordini e logout*/%>
                    <div class="menu">
                        <a href="Dispatcher?controllerAction=Acquisto.view">CARRELLO</a>
                    </div>
                    <div class="menu">
                        <a href="Dispatcher?controllerAction=Ordini.view">ORDINI</a>
                    </div>
                    <div class="menu">
                        <a href="javascript:logoutForm.submit()">LOGOUT</a>
                    </div>
                <%}else{%>
                    <div class="menu">
                        <a href="Dispatcher?controllerAction=LogOn.view&opzione=L">LOGIN</a>
                    </div>
                    <div class="menu">
                        <a href="Dispatcher?controllerAction=LogOn.view&opzione=R">REGISTRATI</a>
                    </div>
                <%}%>
            </div>
        </nav>
    </div>
    
</header>

