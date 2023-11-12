package controller;

import services.config.Configuration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.JDBC;
import model.dao.ProdottoDAO;
import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;

import model.session.dao.*;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.*;

import model.mo.Prodotto;
import model.mo.Utente;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class LogOn {
    
    /*
    * Classe per gestire il login e la registrazione di un utente
    * Usata per logging.jsp e loggingAdmin.jsp
    */
    
    /*Mi servono 2 metodi diversi per la registrazione di un utente e per la 
    registrazione da parte di un admin perchè nel primo, dopo la registrazione
    devo settare la nuova sessione con il cookie, nel secondo no*/
    
    /*
    *
    * Metodo con cui carico i cookie e setto la jsp da visualizzare (logging.jsp
    * se si tratta di un cliente, loggingAdmin.jsp se si tratta di un admin
    */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();

        Logger logger = LogService.printLog();
    
        try {

            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();
            
            /*Mi salvo se sono qui per il login o per la registrazione*/
            String opzione = request.getParameter("opzione");

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            if(ul != null && ul.isAdmin()){
                request.setAttribute("viewUrl", "logon/loggingAdmin");
            }else{
                request.setAttribute("opzione", opzione);
                request.setAttribute("viewUrl", "logon/logging");
            }

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller LogOn", e);
            throw new RuntimeException(e);
        }
    }
    
    /*Metodo chiamato per loggare l'utente*/
    public static void logon(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        String applicationMessage = null;
        
        JDBC jdbc = null;

        Logger logger = LogService.printLog();
    
        try {
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Prelevo i valori inseriti dall'utente*/
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            /*Prelevo l'utente dal DB*/
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            Utente utente = utenteDAO.findByEmail(email);

            /*Se l'utente non esiste o la password non è giusta distruggo la sessione
            (anche se non c'è e setto applicationMessage oltre a settare a null il loggedUser*/
            if (utente == null) {
                ulDAO.elimina();
                applicationMessage = "errore!";
                ul=null;
            } else if( !utente.getPassword().equals(password)){
                ulDAO.elimina();
                applicationMessage = "utente o password errati!";
                ul=null;
            }else {
                /*Se va tutto bene prelevo i dati dell'utente e creo la sessione e il cookie*/
                ul = ulDAO.crea(utente.getEmail(), utente.getNomeUtente(), utente.getCognome(), utente.isAdmin());
                
                /*Metodo per caricare categorie, marche, generi e prodotti*/
                commonView(jdbc, sessionDAO, request);
            }

            /*Committo la transazione*/
            jdbc.commitTransaction();
            
            String pagina = null;

            /*Setto gli attributi del view model*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            if(ul == null){
                pagina = "logon/logging";
                request.setAttribute("opzione", "L");
            }else{
                if(ul != null && utente.isAdmin()){
                    pagina = "homeAdmin/homeAdmin";
                }else{
                    pagina = "catalogo/catalogo";
                    request.setAttribute("carrello", carrelli);
                }
            }
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", pagina);

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller LogOn", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }
    
    /*Metodo chiamato per registrare un cliente*/
    public static void registraUtenti(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String applicationMessage = null;
        
        JDBC jdbc = null;

        Logger logger = LogService.printLog();
    
        try {
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Non recupero il cookie utente perchè se arriva qui vuol dire
            che non è loggato*/
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            Utente utente = null;
            
            /*Prelevo i valori inseriti dall'utente*/
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String nomeUtente = request.getParameter("nomeUtente");
            String cognome = request.getParameter("cognome");
            String genere = request.getParameter("genere");
            String nazione = request.getParameter("nazione");
            String citta = request.getParameter("citta");
            String via = request.getParameter("via");
            String numeroCivico = request.getParameter("numeroCivico");
            int CAP = Integer.parseInt(request.getParameter("CAP"));
            
            /*INSERISCO IL NUOVO UTENTE NEL DB*/
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            try{
                utente = utenteDAO.registrati(email, nomeUtente, cognome, password, genere, nazione, citta, via, numeroCivico, CAP, false, false);
            }catch(DuplicatedObjectException doe){
                applicationMessage = "Utente già esistente";
                jdbc.rollbackTransaction();
                logger.log(Level.SEVERE, "Tentativo di inserimento di un utente già esistente");
            }
            
            /*CREO LA SESSIONE*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.crea(email, nomeUtente, cognome, false);
            
            /*Metodo per caricare categorie, marche, generi e prodotti*/
            commonView(jdbc, sessionDAO, request);
            
            /*Committo la transazione*/
            jdbc.commitTransaction();
            
            /*Setto gli attributi del view model*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "catalogo/catalogo");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller LogOn", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }
    
    /*Metodo chiamato per registrare un utente da parte di un admin*/
    public static void registraAdmin(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        String applicationMessage = null;
        
        JDBC jdbc = null;

        Logger logger = LogService.printLog();
    
        try {
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Non recupero il cookie carrello perchè un admin non ha il carrello*/

            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            Utente utente = null;
            
            /*Prelevo i valori inseriti dall'utente*/
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String nomeUtente = request.getParameter("nomeUtente");
            String cognome = request.getParameter("cognome");
            String genere = request.getParameter("genere");
            String nazione = request.getParameter("nazione");
            String città = request.getParameter("citta");
            String via = request.getParameter("via");
            String numeroCivico = request.getParameter("numeroCivico");
            int CAP = Integer.parseInt(request.getParameter("CAP"));
            boolean admin = false;
            if(request.getParameter("admin").equals("S")){
                admin = true;
            }else{
                admin = false;
            }
            boolean blocked = false;
            if(request.getParameter("blocked").equals("S")){
                blocked = true;
            }else{
                blocked = false;
            }

            /*INSERISCO IL NUOVO UTENTE NEL DB*/
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            try{
                utente = utenteDAO.registrati(email, 
                                              nomeUtente, 
                                              cognome, 
                                              password, 
                                              genere, 
                                              nazione, 
                                              città, 
                                              via, 
                                              numeroCivico, 
                                              CAP, 
                                              admin, 
                                              blocked);
            }catch(DuplicatedObjectException doe){
                applicationMessage = "Utente già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di un utente già esistente");
            }
            
            /*Metodo per caricare le iniziali degli utenti, gli utenti da
            visualizzare e il numero di ordini effettuato da ognuno*/
            HomeManagement.commonView(jdbc, request);
            
            /*Committo la transazione*/
            jdbc.commitTransaction();
            
            /*Setto gli attributi del view model*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller LogOn", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }
    
    /*
    *
    * Metodo per fare il logout. Recupero i cookie e li cancello
    */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        SessionDAOFactory sessionDAO;
        
        JDBC jdbc = null;

        Logger logger = LogService.printLog();
        
        try{
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            
            /*Distruggo la sessione*/
            ulDAO.elimina();
            carrelloDAO.elimina();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            
            jdbc.beginTransaction();
            
            /*Metodo per caricare tipi, rarità ed edizioni*/
            commonView(jdbc, sessionDAO, request);
            
            jdbc.commitTransaction();

            /*loggo tutto in caso di poblemi*/
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller LogOn", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    
        /*Forzo il loggedOn e il loggedUser a false perchè il cookie si aggiorna alla chiamata successiva*/
        request.setAttribute("loggedOn",false);
        request.setAttribute("loggedUser", null);
        request.setAttribute("carrello", null);
        request.setAttribute("viewUrl", "catalogo/catalogo");

    }
    
    /*
    *
    * Metodo per caricare tipi, rarità e edizioni e prodotti
    */
    private static void commonView(JDBC jdbc, SessionDAOFactory sessionDAO, HttpServletRequest request) {
        ArrayList<String> tipoCarte;
        ArrayList<String> rare;
        ArrayList<String> edizioni;
        ArrayList<Prodotto> prodotti = null;
            
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
        /*Estraggo tipi rarità e edizioni*/
        tipoCarte = prodottoDAO.trovaTipoCarte();
        rare = prodottoDAO.trovaRare();
        edizioni = prodottoDAO.trovaEdizioni();
            
        /*Estraggo i prodotti da visualizzare*/
        prodotti = prodottoDAO.findForPush();
                
        request.setAttribute("tipoCarte", tipoCarte);
        request.setAttribute("rare", rare);
        request.setAttribute("edizioni", edizioni);
        request.setAttribute("prodotti", prodotti);

    }
}
