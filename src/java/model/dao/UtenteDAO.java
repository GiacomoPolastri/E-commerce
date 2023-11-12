package model.dao;

import java.util.ArrayList;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Utente;

/**
 *
 * @author Giacomo Polastri
 */
 public interface UtenteDAO {

    public Utente registrati(String email, String nomeUtente, String cognome,
                            String password, String genere,
                            String nazione, String città, String via,
                            String numeroCivico, int CAP, boolean admin,
                            boolean blocked) throws DuplicatedObjectException;

    /*Recupera tutti gli utenti registrati*/
    public ArrayList<Utente> findUtenti();
    
    /*Recupera tutte le iniziali dei cognomi degli utenti registrati*/
    public ArrayList<String> findInitialsUtenti();
    
    /*Recupera tutti gli utenti che hanno il cognome che inizia con la lettera passata*/
    public ArrayList<Utente> findUtentiByInitial(String initial);
    
    /*Recupera l'utente in base all'email*/
    public Utente findByEmail(String email);
    
    /*Blocca un utente sul DB*/
    public void bloccaUtente(String email);
    
    /*Sblocca un utente sul DB*/
    public void sbloccaUtente(String email);
    
}
