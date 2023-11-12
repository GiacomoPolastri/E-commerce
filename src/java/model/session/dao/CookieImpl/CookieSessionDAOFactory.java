package model.session.dao.CookieImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.CarrelloDAO;
import model.session.dao.SessionDAOFactory;
import model.session.dao.LoggedUserDAO;

/**
 *
 * @author Giacomo Polastri
 */
public class CookieSessionDAOFactory implements SessionDAOFactory{
    
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     *
     * Inizializza la sessione memorizzando la request e la response
     * @param request
     * @param response
     */

    @Override
    public void initSession(HttpServletRequest request, HttpServletResponse response) {
        try{
            this.request = request;
            this.response = response;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public LoggedUserDAO getLoggedUserDAO() {
        return new LoggedUserDAOCookieImpl(request, response);
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {
        return new CarrelloDAOCookieImpl(request, response);
    }
    
}
