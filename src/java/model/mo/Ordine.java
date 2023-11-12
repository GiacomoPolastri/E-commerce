package model.mo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author Giacomo Polastri
 */
public class Ordine {
    
    private String statoOrdine, nazione, citta, via;
    private java.util.Date dataOrdine, dataConsegna;
    private int CAP;
    private long codiceOrdine, numeroCivico;
    
    /*Mappo le relazioni*/
    private Pagamento pagamento;
    private Buono buono;
    private Utente utente;
    private ArrayList<Contiene> contiene;
    
    public long getCodiceOrdine(){
        return codiceOrdine;
    }
    
    public void setCodiceOrdine(long c){
        codiceOrdine = c;
    }
    
    public java.util.Date getDataOrdine(){
        return dataOrdine;
    }
    
    public void setDataOrdine(java.util.Date dataOrdine){
        this.dataOrdine=dataOrdine;
    }
    
    public String getDataOrdineString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataOrdine);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataOrdine.toString() + "\"");
            return null;
        }
    }
    
    public String getStatoOrdine(){
        return statoOrdine;
    }
    
    public void setStatoOrdine(String s){
        statoOrdine=s;
    }
    
    public java.util.Date getDataConsegna(){
        return dataConsegna;
    }
    
    public void setDataConsegna(java.util.Date DataConsegna){
        this.dataConsegna=DataConsegna;
    }
    
    public String getDataConsegnaString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataConsegna);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataConsegna.toString() + "\"");
            return null;
        }
    }
    
    public String getNazione(){
        return nazione;
    }
    
    public void setNazione(String naz){
        nazione=naz;
    }
    
    public String getCittà(){
        return citta;
    }
    
    public void setCittà(String citt){
        citta=citt;
    }
    
    public String getVia(){
        return via;
    }
    
    public void setVia(String v){
        via=v;
    }
    
    public Long getNumeroCivico(){
        return numeroCivico;
    }
    
    public void setNumeroCivico(long n){
        numeroCivico=n;
    }
    
    public int getCAP(){
        return CAP;
    }
    
    public void setCAP(int cap){
        CAP=cap;
    }
    
    public Pagamento getPagamento(){
        return pagamento;
    }
    
    public void setPagamento(Pagamento p){
        pagamento = p;
    }
    
    public Buono getBuono(){
        return buono;
    }
    
    public void setBuono(Buono b){
        buono=b;
    }
    
    public Utente getUtente(){
        return utente;
    }
    
    public void setUtente(Utente u){
        utente=u;
    }
    
    /*Ritorna l'intera lista*/
    public ArrayList<Contiene> getContiene() {
        return contiene;
    }

    /*Setta l'intera lista*/
    public void setContiene(ArrayList<Contiene> con) {
       this.contiene = con;
    }
}
