package controller;

import services.config.Configuration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.JDBC;
import model.dao.ProdottoDAO;

import model.session.dao.*;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.*;

import model.mo.Prodotto;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class Catalogo{
    
    /*Classe per gestire la visualizzazione e la ricerca dei prodotti da parte
    dell'utente
    catalogo.jsp e viewProd.jsp*/
    
    private Catalogo(){
    }
    
    /*
    *
    * Metodo per fare la view di catalogo.jsp
    */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ArrayList<Prodotto> prodotti = null;
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Metodo per caricare tipi, rarità e edizioni*/
            commonView(jdbc, sessionDAO, request);
            
            /*
            * Estraggo i prodotti da visualizzare in base al filtro di ricerca,
            * se non è applicato nessun filtro, di default prendo i prodotti
            * in push
            */
            if(request.getParameter("searchType") == null){
                prodotti = prodottoDAO.findForPush();
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("tipoCarta")){
                prodotti = prodottoDAO.findByTipoCarta(request.getParameter("searchName"));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("edizione")){
                prodotti = prodottoDAO.findByEdizione(request.getParameter("searchName"));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("rarita")){
                prodotti = prodottoDAO.findByRarità(request.getParameter("searchName"));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("searchString")){
                String cerca = request.getParameter("searchName");
                /*cerca = cerca.substring(0, 1).toUpperCase() + cerca.substring(1,cerca.length()).toLowerCase();*/
                prodotti = prodottoDAO.findByString(cerca);
            }
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("viewUrl", "catalogo/catalogo");
            
            jdbc.commitTransaction();
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    * Metodo per la view di viewProd.jsp
    */
    public static void viewProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBC jdbc = null;
    
        Logger logger = LogService.printLog();

        try {

            /*Inizializzo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            /*Inizio la transazione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Recupero la chiave del prodotto selezionato*/
            Long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));

            /*Recupero dal DB il prodotto selezionato*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProdotto);
            
            /*Metodo per caricare tipi, rarità ed edizioni*/
            commonView(jdbc, sessionDAO, request);
      
            jdbc.commitTransaction();

            /*Preparo gli attributi della response*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "catalogo/viewProdotto");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    * Metodo per aggiungere un prodotto al carrello
    * Usato in viewProdotto.jsp
    */
    public static void insert(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBC jdbc = null;
    
        Logger logger = LogService.printLog();

        try {

            /*Inizializzo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            /*Recupero la chiave e la quantità del prodotto selezionato*/
            Long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));
            Long quantita = Long.parseLong(request.getParameter("quantita"));

            /*Se un cookie carrello esiste già aggiungo il prodotto al carello,
            altrimenti ne creo uno nuovo*/
            if(carrelli == null){
                carrelloDAO.crea(codiceProdotto, quantita);
            }else{
                carrelloDAO.aggiungi(codiceProdotto, quantita);
            }
            
            /*Inizio la transazione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Recupero dal DB il prodotto selezionato*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProdotto);
            
            /*Metodo per caricare tipi, rarità ed edizioni*/
            commonView(jdbc, sessionDAO, request);
      
            jdbc.commitTransaction();

            /*Preparo gli attributi della response*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "catalogo/viewProdotto");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    * Metodo comune per caricare tipi, rarità ed edizioni dei prodotti
    */
    private static void commonView(JDBC jdbc, SessionDAOFactory sessionDAO, HttpServletRequest request) {
        ArrayList<String> tipoCarte;
        ArrayList<String> rare;
        ArrayList<String> edizioni;
            
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
        /*Estraggo i tipi, le edizioni e le rare*/
        tipoCarte = prodottoDAO.trovaTipoCarte();
        rare = prodottoDAO.trovaRare();
        edizioni = prodottoDAO.trovaEdizioni();
                
        request.setAttribute("tipoCarte", tipoCarte);
        request.setAttribute("rare", rare);
        request.setAttribute("edizioni", edizioni);

    }
    
}
