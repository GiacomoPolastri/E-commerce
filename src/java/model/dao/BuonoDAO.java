package model.dao;

import java.util.ArrayList;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Buono;

/**
 *
 * @author Giacomo Polastri
 */

/*
*Non viene lanciata DuplicatedObjectException in nessun metodo perchè quando un
*admin crea una tipologia di buoni ne crea una serie tutti uguali che si distinguono
*solamente per il loro codice, quindi è inutile un controllo
*/
public interface BuonoDAO {
    
    public Buono creaBuono(String nomeBuono, int sconto, java.util.Date sataScadenza) throws DuplicatedObjectException;
    
    /*Modifica i valori di un buono nel DB*/
    public void aggiorna(Buono buono);

    /*Viene cancellato 'logicamente' un buono dal DB*/
    public void eliminaByKey(long codiceBuono);
    
    /*Vengono cancellati 'logicamente' dal DB tutti i buoni con il nome specificato*/
    public void eliminaByName(String nomeBuono);

    /*Recupera tutti i buoni presenti nel DB*/
    public ArrayList<Buono> recuperaBuoni();
    
    /*Viene verificata la validità del buono con il codice passatogli*/
    public boolean checkValidità(long codiceBuono);
    
    /*Recupera il buono con il codice passato*/
    public Buono findBuonoByKey(long codiceBuono);
    
    /*Recupera tutti i buoni con il nome passato*/
    public ArrayList<Buono> findBuonoByName(String nomeBuono);
    
    
    
}
