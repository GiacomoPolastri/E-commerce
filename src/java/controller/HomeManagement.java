package controller;

import services.config.Configuration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.JDBC;
import model.dao.OrdineDAO;
import model.dao.UtenteDAO;

import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.LoggedUser;

import model.mo.Utente;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class HomeManagement {
    
    /*Classe per gestire gli utenti (solo admin)
    Usata per utenti.jsp*/
    
    /*
    *
    * Metodo per eseguire la view di utenti.jsp
    */
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

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti
            da visualizzare in base all'iniziale selezionata e il numero di ordini
            effettuato da ognuno*/
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller HomeManagement", e);
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
    * Metodo per bloccare l'account di un utente e fare la view di utenti.jsp
    */
    public static void bloccaUtente(HttpServletRequest request, HttpServletResponse response){
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
            
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            
            /*Recupero l'email dell'utente da bloccare*/
            String email = request.getParameter("email");
            
            /*Blocco l'utente*/
            utenteDAO.bloccaUtente(email);

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti
            da visualizzare in base all'iniziale selezionata e il numero di ordini
            effettuato da ognuno*/
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller HomeManagement", e);
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
    * Metodo per sbloccare l'account di un utente e fare la view di utenti.jsp
    */
    public static void sbloccaUtente(HttpServletRequest request, HttpServletResponse response){
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
            
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            
            /*Recupero l'email dell'utente da sbloccare*/
            String email = request.getParameter("email");
            
            /*Sblocco l'utente*/
            utenteDAO.sbloccaUtente(email);

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti
            da visualizzare in base all'iniziale selezionata e il numero di ordini
            effettuato da ognuno*/
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller HomeManagement", e);
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
    * Metodo per caricare le iniziali degli utenti, gli utenti da visualizzare
    * in base alla lettera selezionata e il numero di ordini effettuato da ognuno
    */
    public static void commonView(JDBC jdbc, HttpServletRequest request) {
        ArrayList<String> initials;
        ArrayList<Utente> utenti;

        UtenteDAO utenteDAO = jdbc.getUtenteDAO();
        OrdineDAO ordineDAO = jdbc.getOrdineDAO();

        /*Carico nella lista initials le iniziali degli utenti registrati*/
        initials = utenteDAO.findInitialsUtenti();

        /*Recupero il parametro che contiene la lettera scelta da mostrare*/
        String selectedInitial = request.getParameter("selectedInitial");

        /*Se il parametro è nullo lo setto di default a '*'*/
        if (selectedInitial == null) {
            selectedInitial = "*";
        }

        /*Se selectedInitial vale '*' recupero tutti gli utenti registrati,
        altrimenti solo quelli con l'iniziale selezionata*/
        if(selectedInitial.equals("*")){
            utenti = utenteDAO.findUtenti();
        }else{
            utenti = utenteDAO.findUtentiByInitial(selectedInitial);
        }
        
        Integer temp = 0;
        ArrayList<Integer> numeroOrdini = new ArrayList<Integer>();
        
        /*Conto, per ogni utente non admin, quanti ordini ha effettuato*/
        for(int i=0; i<utenti.size(); i++){
            if(!utenti.get(i).isAdmin()){
                temp = ordineDAO.contaOrdini(utenti.get(i).getEmail());
            }
            numeroOrdini.add(temp);
        }

        /*Setto gli attributi del view model*/
        request.setAttribute("selectedInitial", selectedInitial);
        request.setAttribute("initials", initials);
        request.setAttribute("utenti", utenti);
        request.setAttribute("numeroOrdini", numeroOrdini);

    }
    
}
