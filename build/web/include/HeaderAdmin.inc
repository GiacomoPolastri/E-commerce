
<header class="clearfix"><!-- Defining the header section of the page -->

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="LogOn.logout"/>
    </form>

    <div style="height: 126px;">
        <nav class="clearfix">
            <div class="logo">
                <a href="Dispatcher?controllerAction=HomeAdmin.view">
                    <img id="LogoWeb" src="images/tanadeigoblin.jpg" width="370" height="105" alt="Torna alla Home Page"/>
                </a>
            </div>
            <div class="barra">
                <div class="admin">
                    <a href="Dispatcher?controllerAction=Ordini.view">ORDINI</a>
                </div>
                <div class="admin">
                    <a href="Dispatcher?controllerAction=HomeManagement.view">UTENTI</a>
                </div>
                <div class="admin">
                    <a href="Dispatcher?controllerAction=ProdottoManagement.view">MAGAZZINO</a>
                </div>
                <div class="admin">
                    <a href="Dispatcher?controllerAction=BuonoManagement.view">BUONI</a>
                </div>
                <div class="admin">
                    <a href="javascript:logoutForm.submit()">LOGOUT</a>
                </div>
            </div>
        </nav>
    </div>
    
</header>
