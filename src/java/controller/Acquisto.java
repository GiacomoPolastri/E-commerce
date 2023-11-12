package controller;

import services.config.Configuration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.*;
import model.dao.exception.DuplicatedObjectException;

import model.session.dao.*;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.*;

import model.mo.*;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class Acquisto {
    
    /*Classe per gestire il carrello e l'ordine del carrello
    *carrello.jsp, pagamento.jsp e riepilogo.jsp*/
    
    private Acquisto(){
    }
    
    /*Metodo per visualizzare carrello.jsp*/
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*
            *Metodo comune a tutte le view di carrello.jsp
            *Carico tutti i prodotti dal DB contenuti nel carrello e mappo le varie
            *disponibilità
            */
            commonView(jdbc, sessionDAO, request);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    
    /*Metodo per cambiare la quantità di un prodotto nel carrello*/
    public static void cambiaQuantita(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        double prezzo = 0;
        
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
            /*Modifico il cookie*/
            carrelli = carrelloDAO.modificaQuantità(Long.parseLong(request.getParameter("codiceProdotto")), Long.parseLong(request.getParameter("quantita")));
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
            }
            
            ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
            
            
            /*Mappo le disponibilità dei prodotti*/
            for(int j=0; j<prodotti.size(); j++){
                if(carrelli.get(j).getQuantità() < prodottoDAO.getQuantitàByKey(carrelli.get(j).getCodiceProdotto())){
                    disponibilità.add(Boolean.TRUE);
                }else{
                    disponibilità.add(Boolean.FALSE);
                }
            }
            
            /*Calcolo il prezzo totale del carrello*/
            for(int i=0; i<prodotti.size(); i++){
                prezzo += prodotti.get(i).getPrezzo()*carrelli.get(i).getQuantità();
            }
        
            /*Arrotondo il prezzo alla seconda cifra decimale*/
            prezzo = Math.round(prezzo*100.0) / 100.0;
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("disponibilita", disponibilità);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    
    /*Metodo per rimuovere un prodotto dal carrello, carrello.jsp*/
    public static void rimuovi(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        double prezzo = 0;
        
        JDBC jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Rimuovo il prodotto dal carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.rimuovi(Long.parseLong(request.getParameter("codiceProdotto")));
            
            /*Stabilisco la connessione*/
            jdbc = JDBC.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
            }
            
            ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
            
            
            /*Mappo le disponibilità dei prodotti*/
            for(int j=0; j<prodotti.size(); j++){
                if(carrelli.get(j).getQuantità() < prodottoDAO.getQuantitàByKey(carrelli.get(j).getCodiceProdotto())){
                    disponibilità.add(Boolean.TRUE);
                }else{
                    disponibilità.add(Boolean.FALSE);
                }
            }
            
            /*Calcolo il prezzo totale del carrello*/
            for(int i=0; i<prodotti.size(); i++){
                prezzo += prodotti.get(i).getPrezzo()*carrelli.get(i).getQuantità();
            }
        
            /*Arrotondo il prezzo alla seconda cifra decimale*/
            prezzo = Math.round(prezzo*100.0) / 100.0;
        
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("disponibilita", disponibilità);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    
    /*Metodo per cancellare l'intero carrello, carrello.jsp*/
    public static void cancella(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        double prezzo = 0;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            /*Elimino il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelloDAO.elimina();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
        }
    }
    
    /*Metodo per portarmi dietro i cookie e fare la view di pagamento.jsp solo
    se ho disponibilità di magazzino sufficiente per tutti i prodotti del carrello*/
    public static void ordina(HttpServletRequest request, HttpServletResponse response){
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
            
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
                        
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
            }
            
            
            boolean disponibilità = true;
            
            for(int i=0; i<carrelli.size(); i++){
                if(carrelli.get(i).getQuantità() > prodottoDAO.getQuantitàByKey(carrelli.get(i).getCodiceProdotto()) || prodotti.get(i).isBlocked()){
                    disponibilità = false;
                }
            }
            
            if(!disponibilità){
                /*
                *Metodo comune a tutte le view di carrello.jsp
                *Carico tutti i prodotti dal DB contenuti nel carrello e mappo le varie
                *disponibilità
                */
            commonView(jdbc, sessionDAO, request);
            }
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            if(disponibilità){
                request.setAttribute("carrello", carrelli);
                request.setAttribute("viewUrl", "acquisto/pagamento");
            }else{
                request.setAttribute("viewUrl", "acquisto/carrello");
                request.setAttribute("applicationMessage", "Uno o più prodotti non sono disponibili per l'acquisto");
            }
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    
    /*Metodo per passare alla schermata di riepilogo dell'ordine*/
    public static void procedi(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String applicationMessage = null;
        
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
            
            BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            Buono buono = null;
            boolean validità = true;
            
            /*Controllo se è stato inserito un buono*/
            if(Long.parseLong(request.getParameter("codiceBuono")) != 0){
                /*Se è stato inserito mi salvo se è valido o meno e me lo carico
                dal DB*/
                validità = buonoDAO.checkValidità(Long.parseLong(request.getParameter("codiceBuono")));
                buono = buonoDAO.findBuonoByKey(Long.parseLong(request.getParameter("codiceBuono")));
            }
            
            if(validità){
                /*Se è valido ricavo tutti i valori inseriti dall'utente e
                calcolo il prezzo*/
                String cartaPagamento = request.getParameter("cartaPagamento");
                String nazione = request.getParameter("nazione");
                String citta = request.getParameter("citta");
                String via = request.getParameter("via");
                long numeroCivico = Long.parseLong(request.getParameter("numeroCivico"));
                int CAP = Integer.parseInt(request.getParameter("CAP"));
                double prezzo = 0;
                
                /*Mi carico i prodotti del carrello dal DB*/
                ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
                ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
                for(int i=0; i<carrelli.size(); i++){
                    prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
                }
                
                /*Faccio la somma dei loro prezzi*/
                for(int i=0; i<prodotti.size(); i++){
                    prezzo += prodotti.get(i).getPrezzo()*carrelli.get(i).getQuantità();
                }
                
                /*Se c'è un buono calcolo lo sconto*/
                if(buono != null){
                    double sconto = (prezzo/100)*buono.getSconto();
                    prezzo -= sconto;
                }
                
                /*Arrotondo il prezzo alla seconda cifra decimale*/
                prezzo = Math.round(prezzo*100.0) / 100.0;
                
                request.setAttribute("cartaPagamento", cartaPagamento);
                request.setAttribute("nazione", nazione);
                request.setAttribute("citta", citta);
                request.setAttribute("via", via);
                request.setAttribute("numeroCivico", numeroCivico);
                request.setAttribute("CAP", CAP);
                request.setAttribute("prezzo", prezzo);
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("buonoPresente", buono!=null);
                request.setAttribute("buono", buono);
                request.setAttribute("viewUrl", "acquisto/riepilogo");

            }else{
                /*Se il codice del buono non è valido torno nella pagina precedente*/
                applicationMessage = "Buono non valido";
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "acquisto/pagamento");
            }
            
            jdbc.commitTransaction();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    
    /*Metodo per il pagamento del carrello, carrello.jsp*/
    public static void paga(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String applicationMessage = null;
        
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
            
            boolean disponibilità = true;

            Prodotto prodotto = null;
            
            /*Estraggo i prodotti dal DB*/
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
            }
            
            /*CONTROLLO DISPONIBILITA*/
            for(int j=0; j<carrelli.size(); j++){
                if(carrelli.get(j).getQuantità() > prodottoDAO.getQuantitàByKey(carrelli.get(j).getCodiceProdotto()) || prodotti.get(j).isBlocked()){
                    disponibilità = false;
                } else {
                }
            }
            
            /*Se nel frattempo è rimasta disponibilità procedo all'acquisto,
            *altrimenti mando un messaggio all'utente*/
            if(disponibilità){
                Buono buono = null;
                BuonoDAO buonoDAO = jdbc.getBuonoDAO();
            
                /*Leggo il parametro che mi dice se era stato inserito un buono valido o meno*/
                String buonoPresente = request.getParameter("buonoPresente");
                if(buonoPresente.equals("S")){
                    /*In caso affermativo lo carico dal db*/
                    buono = buonoDAO.findBuonoByKey(Long.parseLong(request.getParameter("codiceBuono")));
                }
            
                /*Carico l'utente loggato*/
                UtenteDAO utenteDAO = jdbc.getUtenteDAO();
                Utente utente = utenteDAO.findByEmail(ul.getEmail());
            
                Pagamento pagamento = null;
                PagamentoDAO pagamentoDAO = jdbc.getPagamentoDAO();
            
                /*****INIZIO TRANSAZIONE*****/
                Date dataOdierna = new Date();
                
                /*INSERIMENTO PAGAMENTO NEL DB*/
                try{
                    
                    pagamento = pagamentoDAO.creaPagamento("confermato", 
                            request.getParameter("cartaPagamento"), 
                            dataOdierna, dataOdierna, Float.parseFloat(request.getParameter("prezzo")),
                            utente, null);
                }catch(DuplicatedObjectException doe){
                    applicationMessage = "Pagamento già esistente";
                    logger.log(Level.INFO, "Tentativo di inserimento di un pagamento già esistente");
                }
            
                Ordine ordine = null;
                OrdineDAO ordineDAO = jdbc.getOrdineDAO();
            
                /*Imposto una data di consegna di default 3 giorni successivi alla
                data dell'ordine*/
                java.util.Date dataConsegna = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataConsegna);
                cal.add(Calendar.DATE, 3);
                dataConsegna = cal.getTime();
            
                /*INSERIMENTO ORDINE NEL DB*/
                try{
                    ordine = ordineDAO.creaOrdine(dataOdierna, 
                                                  "In preparazione", 
                                                  dataConsegna, 
                                                  request.getParameter("nazione"), 
                                                  request.getParameter("citta"), 
                                                  request.getParameter("via"), 
                                                  Long.parseLong(request.getParameter("numeroCivico")), 
                                                  Integer.parseInt(request.getParameter("CAP")), pagamento,
                                                  buono, utente, null);
                }catch(DuplicatedObjectException doe){
                    applicationMessage = "Ordine già esistente";
                    logger.log(Level.INFO, "Tentativo di inserimento di un ordine già esistente");
                }
            
                Contiene contiene = null;
                ContieneDAO contieneDAO = jdbc.getContieneDAO();
            
                /*INSERIMENTO CONTIENE NEL DB*/
                for(int i=0; i<carrelli.size(); i++){
                    try{
                        contiene = contieneDAO.creaContiene(ordine.getCodiceOrdine(),
                                carrelli.get(i).getCodiceProdotto(), carrelli.get(i).getQuantità());
                    }catch(DuplicatedObjectException doe){
                        applicationMessage = "Contiene già esistente";
                        logger.log(Level.INFO, "Tentativo di inserimento di un 'Contiene' già esistente");
                    }
                }
            
                /*MARCO COME USATO IL BUONO NEL DB*/
                if(buonoPresente.equals("S")){
                    buono.setUsato(true);
                    buonoDAO.aggiorna(buono);
                }
            
                /*MODIFICA GIACENZA NEL DB*/
                for(int i=0; i<carrelli.size(); i++){
                    prodotto = prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto());
                    prodotto.setQuantità(prodottoDAO.getQuantitàByKey(carrelli.get(i).getCodiceProdotto())-carrelli.get(i).getQuantità());
                    prodottoDAO.aggiorna(prodotto);
                }
            
                /*CANCELLA COOCKIE CARRELLO*/
                carrelloDAO.elimina();
                applicationMessage = "Ordine avvenuto con successo";
                
                /*Dopo la transazione ritorno alla pagina del carrello che, anche
                *se vuoto, si aspetta di ricevere un parametro chiamato prezzo e
                *quindi gliene passo uno fittizio*/
                double prezzo = 0;
                request.setAttribute("prezzo", prezzo);
            }else{
                /*Caso in cui sia cambiata la disponibilità di magazzino*/
                applicationMessage = "Disponibilità modificata durante la transazione, impossibile procedere all'ordine";
                /*
                *Metodo comune a tutte le view di carrello.jsp
                *Carico tutti i prodotti dal DB contenuti nel carrello e mappo le varie
                *disponibilità
                */
                commonView(jdbc, sessionDAO, request);
            }
              
            jdbc.commitTransaction();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "acquisto/carrello");
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    * Metodo comune a tutte le view di carrello.jsp
    * Carico tutti i prodotti dal DB contenuti nel carrello e mappo le varie
    * disponibilità
    */
    public static void commonView(JDBC jdbc, SessionDAOFactory sessionDAO, HttpServletRequest request){
        /*Recupero il cookie carrello*/
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
        carrelli = carrelloDAO.trova();
        double prezzo = 0;
        
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
        /*Estraggo i prodotti dal DB*/
        for(int i=0; i<carrelli.size(); i++){
            prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProdotto()));
        }
            
        ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
            
        /*Mappo le disponibilità dei prodotti*/
        for(int j=0; j<prodotti.size(); j++){
            if(carrelli.get(j).getQuantità() < prodottoDAO.getQuantitàByKey(carrelli.get(j).getCodiceProdotto()) && !prodotti.get(j).isBlocked()){
                disponibilità.add(Boolean.TRUE);
            }else{
                disponibilità.add(Boolean.FALSE);
            }
        }
        
        /*Calcolo il prezzo totale del carrello*/
        for(int i=0; i<prodotti.size(); i++){
            prezzo += prodotti.get(i).getPrezzo()*carrelli.get(i).getQuantità();
        }
        
        /*Arrotondo il prezzo alla seconda cifra decimale*/
        prezzo = Math.round(prezzo*100.0) / 100.0;
        
        /*Setto gli attributi del viewModel*/
        request.setAttribute("prezzo", prezzo);
        request.setAttribute("disponibilita", disponibilità);
        request.setAttribute("prodotti", prodotti);
        request.setAttribute("carrello", carrelli);
    }}
    

