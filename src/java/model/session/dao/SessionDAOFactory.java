package model.session.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Giacomo Polastri
 */
public interface SessionDAOFactory {

  public  void initSession(HttpServletRequest request, HttpServletResponse response); /*creo una funzione astratta da implementare*/

  public  LoggedUserDAO getLoggedUserDAO();                                                                                /*altra funzione da implementare*/
  
  public  CarrelloDAO getCarrelloDAO();
  
}

