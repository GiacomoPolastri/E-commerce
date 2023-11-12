package model.mo;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Giacomo Polastri
 */
public class Pagamento {
    
    private String statoPagamento, cartaPagamento;
    private java.util.Date dataRichiestaPagamento, dataPagamento;
    private long codicePagamento;
    private float importo;
    
    /*Mappo le relazioni con Utente e Ordine*/
    private Utente utente;
    private Ordine ordine;
    
    public long getCodicePagamento(){
        return codicePagamento;
    }
    
    public void setCodicePagamento(long c){
        codicePagamento=c;
    }
    
    public java.util.Date getDataRichiestaPagamento(){
        return dataRichiestaPagamento;
    }
    
    public void setDataRichiestaPagamento(java.util.Date d){
        dataRichiestaPagamento=d;
    }
    
    public String getDataRichiestaPagamentoString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataRichiestaPagamento);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataRichiestaPagamento.toString() + "\"");
            return null;
        }
    }
    
    public java.util.Date getDataPagamento(){
        return dataPagamento;
    }
    
    public void setDataPagamento(java.util.Date d){
        dataPagamento=d;
    }
    
    public String getDataPagamentoString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataPagamento);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataPagamento.toString() + "\"");
            return null;
        }
    }
    
    public String getStatoPagamento(){
        return statoPagamento;
    }
    
    public void setStatoPagamento(String s){
        statoPagamento=s;
    }
    
    public String getCartaPagamento(){
        return cartaPagamento;
    }
    
    public void setCartaPagamento(String c){
        cartaPagamento = c;
    }
    
    public float getImporto(){
        return importo;
    }
    
    public void setImporto(float i){
        importo=i;
    }
    
    public Utente getUtente(){
        return utente;
    }
    
    public void setUtente(Utente u){
        utente=u;
    }
    
    public Ordine getOrdine(){
        return ordine;
    }
    
    public void setOrdine(Ordine o){
        ordine=o;
    }
    
}
