package model.session.dao;

import java.util.ArrayList;
import model.session.mo.Carrello;

/**
 *
 * @author Giacomo Polastri
 */
public interface CarrelloDAO {
    
    public ArrayList<Carrello> crea(long codiceProdotto , long quantita);
    
    public void aggiungi(long codiceProdotto, long quantita);
    
    public ArrayList<Carrello> rimuovi(long codiceProdotto);
    
    public void elimina();
    
    public ArrayList<Carrello> trova();
    
    public ArrayList<Carrello> modificaQuantità(long codiceProdotto, long quantita);
    
}
