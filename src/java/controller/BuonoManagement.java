package controller;

import services.config.Configuration;
import static java.sql.Date.valueOf;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.BuonoDAO;
import model.dao.JDBC;

import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.LoggedUser;

import model.mo.Buono;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class BuonoManagement {
    
    /*Classe per visualizzare i vari buoni presenti nel DB e gestirli
    buoni.jsp e creaBuono.jsp*/
    
    /*Metodo per eseguire la view di buoni.jsp mostrando */
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

            /*Chiamo il metodo uguale a tutte le chiamate per caricare tutti i
            buoni presenti nel DB raggruppandoli per nome*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "buoniManagement/buoni");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller BuonoManagement", e);
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

    /**
     *
     * @param request
     * @param response
     *
     * Metodo per la cancellazione logica di tutti i buoni aventi il nome passato
     * come parametro
     */
    public static void cancellaByName(HttpServletRequest request, HttpServletResponse response){
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
            
            BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            
            /*Prelevo il nome dei buoni da cancellare*/
            String nomeBuono = request.getParameter("nomeBuono");
            
            /*Elimino i buoni*/
            buonoDAO.eliminaByName(nomeBuono);

            /*Chiamo il metodo uguale a tutte le chiamate per caricare tutti i
            buoni presenti nel DB raggruppandoli per nome*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "buoniManagement/buoni");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller BuonoManagement", e);
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
    * Metodo per fare la view di buoni.jsp ma con tutti i buoni di un solo gruppo
    */
    public static void viewByName(HttpServletRequest request, HttpServletResponse response){
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

            ArrayList<Buono> buoni;

            BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            
            /*Recupero il nome dei buoni*/
            String nomeBuono = request.getParameter("nomeBuono");

            /*Carico i buoni*/
            buoni = buonoDAO.findBuonoByName(nomeBuono);

            jdbc.commitTransaction();

            /*Setto gli attributi del view model*/
            request.setAttribute("mod", "NomeBuono");
            request.setAttribute("buoni", buoni);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "buoniManagement/buoni");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller BuonoManagement", e);
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
    
    /**
     *
     * @param request
     * @param response
     * 
     * Metodo per cancellare logicamente dalla base di dati un buono identificato
     * dal suo codice
     */
    public static void cancellaByKey(HttpServletRequest request, HttpServletResponse response){
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
            
            BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            
            /*Prelevo il codice del buono da cancellare*/
            long codiceBuono = Long.parseLong(request.getParameter("codiceBuono"));
            
            /*Elimino il buono*/
            buonoDAO.eliminaByKey(codiceBuono);

            /*Chiamo il metodo uguale a tutte le chiamate per caricare tutti i
            buoni presenti nel DB raggruppandoli per nome*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "buoniManagement/buoni");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller BuonoManagement", e);
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
    * Metodo per fare la view di creaBuono.jsp, la quale contiene la form per
    * generare un nuovo insieme di buoni
    */
    public static void inserisciBuonoView(HttpServletRequest request, HttpServletResponse response){
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
            request.setAttribute("viewUrl", "buoniManagement/creaBuono");

        }catch(Exception e){
            logger.log(Level.SEVERE, "BuonoManagement Controller Error", e);
            throw new RuntimeException(e);
        }
    }
    
    /*
    *
    * Metodo per inserire i buoni nel DB e fare la view di buoni.jsp
    */
    public static void inserisciBuono(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            Buono buono = null;
            
            /*Prelevo i valori inseriti*/
            String nomeBuono = request.getParameter("nomeBuono");
            java.util.Date dataScadenza = valueOf(request.getParameter("dataScadenza"));
            int sconto = Integer.parseInt(request.getParameter("sconto"));
            int numero = Integer.parseInt(request.getParameter("quantita"));
            
            /*INSERISCO IL NUOVO BUONO NEL DB*/
            for(int i=0 ; i<numero ; i++){
                buono = buonoDAO.creaBuono(nomeBuono, sconto, dataScadenza);
            }

            /*Chiamo il metodo uguale a tutte le chiamate per caricare tutti i
            buoni presenti nel DB raggruppandoli per nome*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("mod", "All");
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "buoniManagement/buoni");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller BuonoManagement", e);
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
    * Metodo uguale a tutte le chiamate per caricare tutti i buoni presenti nel 
    * DB raggruppandoli per nome
    */
    private static void commonView(JDBC jdbc, SessionDAOFactory sessionDAO, HttpServletRequest request) {
        ArrayList<Buono> buoni;

        BuonoDAO buonoDAO = jdbc.getBuonoDAO();

        /*Carico tutti i buoni nel DB raggruppati per nome*/
        buoni = buonoDAO.recuperaBuoni();

        /*Setto gli attributi del view model*/
        request.setAttribute("buoni", buoni);
        request.setAttribute("mod", "All");

    }
    
}
