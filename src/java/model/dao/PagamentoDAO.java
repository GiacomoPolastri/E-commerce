package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Ordine;
import model.mo.Pagamento;
import model.mo.Utente;

/**
 *
 * @author Giacomo Polastri
 */
public interface PagamentoDAO {
    
    public Pagamento creaPagamento(String statoPagamento, String cartaPagamento, 
                            java.util.Date dataRichiestaPagamento, java.util.Date
                            dataPagamento, float importo, Utente utente, Ordine ordine)
                            throws DuplicatedObjectException;
    
    
    
    /*Recupera l'importo del pagamento specificato*/
    public float getImporto(long codicePagamento);
    
}
