package controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;
import model.session.dao.CookieImpl.CookieSessionDAOFactory;
import model.session.mo.LoggedUser;

import services.logservice.LogService;

/**
 *
 * @author Giacomo Polastri
 */
public class HomeAdmin {
    
    /*Classe che gestisce la vista di homeAdmin.jsp*/
    
    /*
    *
    * Metodo per la view di homeAdmin.jsp
    */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAOFactory sessionDAO;
        LoggedUser ul;
        
        Logger logger = LogService.printLog();

        try{
            /*Creo la sessione*/
            sessionDAO = new CookieSessionDAOFactory();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            LoggedUserDAO ulDAO = sessionDAO.getLoggedUserDAO();
            ul = ulDAO.trova();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "homeAdmin/homeAdmin");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller HomeAdmin", e);
            throw new RuntimeException(e);
        }
    }
}
