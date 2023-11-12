package model.mo;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Giacomo Polastri
 */
public class Buono {
    
    private java.util.Date dataScadenza;
    private String nomeBuono;
    private int sconto;
    private long codiceBuono;
    private boolean eliminato, usato;
    
    /*Mappo la relazione con Ordine*/
    private Ordine ordine;
    
    public long getCodiceBuono(){
        return codiceBuono;
    }
    
    public void setCodiceBuono(long c){
        codiceBuono=c;
    }
    
    public String getNomeBuono(){
        return nomeBuono;
    }
    
    public void setNomeBuono(String n){
        nomeBuono = n;
    }
    
    public java.util.Date getDataScadenza(){
        return dataScadenza;
    }
    
    public void setDataScadenza(java.util.Date d){
        dataScadenza=d;
    }
    
    public String getDataScadenzaString(){
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        try{
            return output.format(dataScadenza);
        } catch (Exception pe) {
            System.err.println("FormatUtils.java ERROR: Cannot parse \""+ dataScadenza.toString() + "\"");
            return null;
        }
    }
    
    public int getSconto(){
        return sconto;
    }
    
    public void setSconto(int s){
        sconto=s;
    }
    
    
    public boolean isEliminato(){
        return eliminato;
    }
    
    public void setEliminato(boolean e){
        eliminato=e;
    }
    
    public Ordine getOrdine(){
        return ordine;
    }
    
    public void setOrdine(Ordine o){
        ordine=o;
    }
    
    public boolean isUsato(){
        return usato;
    }
    
    public void setUsato(boolean u){
        usato = u;
    }
}
