package model.dao;

import java.util.ArrayList;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Contiene;

/**
 *
 * @author Giacomo Polastri
 */
public interface ContieneDAO {
    
    public Contiene creaContiene(long codiceOrdine, long codiceProdotto, long quantitaOrdine)
                                throws DuplicatedObjectException;
    
    public ArrayList<Contiene> findContieneByOrdine(long codiceOrdine);
    
}
