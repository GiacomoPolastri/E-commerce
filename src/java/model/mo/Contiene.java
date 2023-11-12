package model.mo;

/**
 *
 * @author Giacomo Polastri
 */
public class Contiene {
    
    /*Entità di relazione M:N tra Prodotto e Ordine*/
    
    private long quantitaOrdine;
    
    /*Mappo le relazioni con Ordine e Prodotto*/
    private Ordine ordine;
    private Prodotto prodotto;
    
    
    public long getQuantitàOrdine(){
        return quantitaOrdine;
    }
    
    public void setQuantitàOrdine(long q){
        quantitaOrdine=q;
    }
    
    
    public Ordine getOrdine(){
        return ordine;
    }
    
    public void setOrdine(Ordine ordine){
        this.ordine=ordine;
    }
    
    public Prodotto getProdotto(){
        return prodotto;
    }
    
    public void setProdotto(Prodotto prodotto){
        this.prodotto=prodotto;
    }
}
