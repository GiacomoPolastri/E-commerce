package model.session.dao;

import model.session.mo.LoggedUser;

/**
 *
 * @author Giacomo Polastri
 */
public interface LoggedUserDAO {
    
    /*Creazione cookie*/
    public LoggedUser crea(String email, String nomeUtente, String cognome, boolean admin);
    
    /*Aggiornamento cookie*/
    public void aggiorna(LoggedUser loggedUser);
    
    /*Cancellazione cookie*/
    public void elimina();
    
    /*Lettura cookie*/
    public LoggedUser trova();
    
}

   
    

