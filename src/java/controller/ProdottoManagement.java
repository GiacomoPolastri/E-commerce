package controller;

import services.config.Configuration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.JDBC;
import model.dao.ProdottoDAO;
import model.dao.exception.DuplicatedObjectException;

import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.LoggedUser;

import model.mo.Prodotto;


import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class ProdottoManagement {
    
    /*Classe per visualizzare i prodotti presenti in magazzino e gestirli
    Usata per magazzino.jsp, gestisciProdotto.jsp*/
    
    /*Metodo per eseguire la view di magazzino.jsp*/
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller prodottoManagement", e);
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
    * Metodo per bloccare la vendita di un prodotto
    */
    public static void bloccaProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*Recupero il codice del prodotto da bloccare*/
            long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Blocco il prodotto*/
            prodottoDAO.blocca(codiceProdotto);

            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller prodottoManagement", e);
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
    * Metodo per sbloccare un prodotto bloccato per la vendita
    */
    public static void sbloccaProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*Recupero il codice del prodotto da sbloccare*/
            long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Sblocco il prodotto*/
            prodottoDAO.sblocca(codiceProdotto);

            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller prodottoManagement", e);
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
    * Metodo per fare la view di gestisciProdotto.jsp nel caso l'admin voglia
    * crearne uno nuovo
    */
    public static void inserisciProdottoView(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Setto gli attributi*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciProdotto");

        }catch(Exception e){
            logger.log(Level.SEVERE, "prodottoManagement Controller Error", e);
            throw new RuntimeException(e);
        }
    }
    
    /*
    *
    * Metodo con cui mi porto dietro i dati del prodotto inseriti e fare la view di gestisciMagazzino.jsp 
    *
    */
    public static void inserisciProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        String applicationMessage;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            Prodotto prodotto = new Prodotto();
            
            /*Prelevo i valori inseriti e mi porto dietro il prodotto*/
                        
            String nomeCarta = request.getParameter("nomeCarta");
            prodotto.setNomeCarta(nomeCarta);
            
            String tipoCarta = request.getParameter("tipoCarta");
            prodotto.setTipoCarta(tipoCarta);
            
            String rarita = request.getParameter("rarita");
            prodotto.setRarità(rarita);
            
            String edizione = request.getParameter("edizione");
            prodotto.setEdizione(edizione);
           
            String immagine = request.getParameter("immagine");
            prodotto.setImmagine(immagine);
            
            String testo = request.getParameter("testo");
            prodotto.setTesto(testo);
            
            Float prezzo = Float.parseFloat(request.getParameter("prezzo"));
            prodotto.setPrezzo(prezzo);
            
            Long quantita = Long.parseLong(request.getParameter("quantita"));
            prodotto.setQuantità(quantita);

            if(request.getParameter("blocked").equals("S")){
                prodotto.setBlocked(true);
            }else{
                prodotto.setBlocked(false);
            }
            
            if(request.getParameter("push").equals("S")){
                prodotto.setPush(true);
            }else{
                prodotto.setPush(false);
            }
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "ProdottoManagement Controller Error", e);
            throw new RuntimeException(e);
        }
    } 
    /*
    *
    * Metodo con cui prelevo i dati del prodotto e della giacenza in magazzino e aggiorno il DB.
    * Infine faccio la view di magazzino.jsp
    */
    public static void inserisciNelDB(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        String applicationMessage;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            Prodotto prodotto = null;
            
            /*Recupero i valori del prodotto*/
            String nomeCarta = request.getParameter("nomeCarta");
            String tipoCarta = request.getParameter("tipoCarta");
            String rarita = request.getParameter("rarita");
            String edizione = request.getParameter("edizione");
            String immagine = request.getParameter("immagine");
            String testo = request.getParameter("testo");
            Float prezzo = Float.parseFloat(request.getParameter("prezzo"));
            Long quantita = Long.parseLong(request.getParameter("quantita"));
            boolean blocked;
            if(request.getParameter("blocked").equals("S")){
                blocked = true;
            }else{
                blocked = false;
            }
            boolean push;
            if(request.getParameter("push").equals("S")){
                push = true;
            }else{
                push = false;
            }

            try{
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            prodotto = prodottoDAO.creaProdotto(nomeCarta, tipoCarta, rarita, edizione, testo,immagine, prezzo, quantita, blocked, push, null);
            }catch(DuplicatedObjectException doe){
            applicationMessage = "Prodotto già esistente";
            logger.log(Level.INFO, "Tentativo di inserimento di un prodotto già esistente");
            }
            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller ProdottoManagement", e);
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
    * Metodo per fare la view di gestisciProdotto.jsp nel caso in cui l'admin
    * voglia modificare qualche dato di un prodotto già esistente
    */
    public static void modificaProdottoView(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*Prelevo il codice del prodotto da modificare*/
            long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));
            
            /*Prelevo il prodotto da modificare dal DB*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProdotto);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciProdotto");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller ProdottoManagement", e);
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
    * Metodo per aggiornare il DB con le modifiche al prodotto apportate dall'admin
    * e fare la view di magazzino.jsp
    */
    public static void modificaProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        String applicationMessage;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            
            /*Stabilisco la connessione*/
            jdbc.beginTransaction();
            
            Prodotto prodotto = new Prodotto();
            
            /*Prelevo i valori inseriti*/
            prodotto.setCodiceProdotto(Long.parseLong(request.getParameter("codiceProdotto")));
            prodotto.setNomeCarta(request.getParameter("nomeCarta"));
            prodotto.setTipoCarta(request.getParameter("tipoCarta"));
            prodotto.setRarità(request.getParameter("rarita"));
            prodotto.setEdizione(request.getParameter("edizione"));
            prodotto.setImmagine(request.getParameter("immagine"));
            prodotto.setTesto(request.getParameter("testo"));
            prodotto.setPrezzo(Float.parseFloat(request.getParameter("prezzo")));
            prodotto.setQuantità(Long.parseLong(request.getParameter("quantita")));
            
            if(request.getParameter("blocked").equals("S")){
                prodotto.setBlocked(true);
            }else{
                prodotto.setBlocked(false);
            }
            boolean push = false;
            if(request.getParameter("push").equals("S")){
                prodotto.setPush(true);
            }else{
                prodotto.setPush(false);
            }
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*AGGIORNO IL PRODOTTO NEL DB*/
            try{
                prodottoDAO.aggiorna(prodotto);
            }catch(DuplicatedObjectException doe){
                applicationMessage = "Prodotto già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di un prodotto già esistente");
            }

            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller ProdottoManagement", e);
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

    private static void commonView(JDBC jdbc, SessionDAOFactory sessionDAO, HttpServletRequest request) {
        ArrayList<Prodotto> prodotti;

        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();

        prodotti = prodottoDAO.trovaProdotti();

        /*Setto gli attributi del view model*/
        request.setAttribute("prodotti", prodotti);

    }
}
