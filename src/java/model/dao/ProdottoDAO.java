package model.dao;

import java.util.ArrayList;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Contiene;
import model.mo.Prodotto;

/**
 *
 * @author Giacomo Polastri
 */
public interface ProdottoDAO {
    
    public Prodotto creaProdotto(String nomeCarta, String tipoCarta,
                                String rarita, String edizione, String immagine, 
                                String testo ,float prezzo,long quantita, boolean blocked, 
                                boolean push, Contiene[] contiene) 
            throws DuplicatedObjectException;
    
    /*Aggiorna una tupla di prodotto*/
    public void aggiorna(Prodotto prodotto) throws DuplicatedObjectException;
    
    /*Blocca un prodotto*/
    public void blocca(long codiceProdotto);
    
    /*Sblocca un prodotto*/
    public void sblocca(long codiceProdotto);
    
    /*trova tutte le carte*/
    public ArrayList<String> trovaNomeCarte();
    
    /*Trova tutte le categorie di prodotti presenti nel db*/
    public ArrayList<String> trovaTipoCarte();
    
    /*Trova tutte le marche dei prodotti presenti del db*/
    public ArrayList<String> trovaRare();
    
    /*Trova tutti i generi dei prodotti presenti nel db*/
    public ArrayList<String> trovaEdizioni();
    
    /*Trova tutti i prodotti dlla categoria specificata*/
    public ArrayList<Prodotto> findByTipoCarta(String tipoCarta);
    
    /*Trova tutti i prodotti della marca specificata*/
    public ArrayList<Prodotto> findByRarità(String rarita);
    
    /*Trova tutti i prodotti del genere specificato*/
    public ArrayList<Prodotto> findByEdizione(String edizione);
    
    /*Trova tutti i prodotti che in un certo modo sono compatibili con la stringa
    di ricerca passata*/
    public ArrayList<Prodotto> findByString(String search);
    
    /*Recupera un prodotto dal db in base alla sua chiave*/
    public Prodotto findByKey(Long codiceProdotto);
    
    /*Recupera tutti i prodotti messi in push dall'admin*/
    public ArrayList<Prodotto> findForPush();
    
    /*Recupera dal DB tutti i prodotti presenti*/
    public ArrayList<Prodotto> trovaProdotti();
    
    
    
    public long getQuantitàByKey(long codiceProdotto);
    
}
