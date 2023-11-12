package model.dao;

import java.util.ArrayList;
import java.util.Date;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Buono;
import model.mo.Contiene;
import model.mo.Ordine;
import model.mo.Pagamento;
import model.mo.Utente;

/**
 *
 * @author Giacomo Polastri
 */
public interface OrdineDAO {
    
    public Ordine creaOrdine(Date dataOrdine,
                             String statoOrdine,
                             Date dataConsegna,
                             String nazione, 
                             String citta,
                             String via, 
                             long numeroCivico,
                             int CAP,
                             Pagamento pagamento, 
                             Buono buono, 
                             Utente utente,
                             ArrayList<Contiene> contiene) throws DuplicatedObjectException;
    
    /*Aggiorna lo stato dell'ordine specificato nel caso passi da 'in preparazione'
    *a 'in viaggio'*/
    public void aggiornaStato(long codiceOrdine, String stato);
    
    /*Aggiorna lo stato dell'ordine specificato nel caso passi a 'consegnato' poichè
    *in questo caso viene aggiornata anche la data di consegna*/
    public void aggiornaStatoConData(long codiceOrdine, String statoOrdine, Date dataOdierna);
    
    /*Recupera tutti gli ordini effettuati*/
    public ArrayList<Ordine> findOrdini();
    
    /*Recupera tutti gli ordini effetttuati da uno specifico utente*/
    public ArrayList<Ordine> findByUtente(String email);
    
    
    
    /*Conta quanti ordini ha effettuato uno specifico cliente*/
    public int contaOrdini(String email);

}
