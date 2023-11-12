package model.mo;

/**
 *
 * @author Giacomo Polastri
 */
public class Prodotto {
    
    private String nomeCarta, tipoCarta, rarita, edizione, testo, immagine;
    private long quantita;
    private long codiceProdotto;
    private float prezzo;
    private boolean blocked, push;
    
    private Contiene[] con;
    
    public long getCodiceProdotto(){
        return codiceProdotto;
    }
    
    public void setCodiceProdotto(long c){
        codiceProdotto=c;
    }
    
    public String getNomeCarta(){
        return nomeCarta;
    }
    
    public void setNomeCarta(String c){
        nomeCarta=c;
    }
    
    public String getTipoCarta(){
        return tipoCarta;
    }
    
    public void setTipoCarta(String t){
        tipoCarta=t;
    }
    
    public float getPrezzo(){
        return prezzo;
    }
    
    public void setPrezzo(float p){
        prezzo=p;
    }
    
    public String getRarità(){
        return rarita;
    }
    
    public void setRarità(String r){
        rarita=r;
    }
    
    public String getEdizione(){
        return edizione;
    }
    
    public void setEdizione(String e){
        edizione=e;
    }
    
    public String getImmagine(){
        return immagine;
    }
    
    public void setImmagine(String i){
        immagine=i;
    }
    
    public String getTesto(){
        return testo;
    }
    
    public void setTesto(String t){
        testo = t;
    }
    
    public long getQuantità(){
        return quantita;
    }
    
    public void setQuantità(long q){
        quantita=q;
    }
    
    public boolean isBlocked(){
        return blocked;
    }
    
    public void setBlocked(boolean b){
        blocked=b;
    }
    
    public boolean isPush(){
        return push;
    }
    
    public void setPush(boolean p){
        push=p;
    }
    
    /*Ritorna l'intero array*/
    public Contiene[] getContiene() {
        return con;
    }

    /*Setta l'intero array*/
    public void setContiene(Contiene[] con) {
       this.con = con;
    }
    
    /*Ritorna l'elemento della posizione specificata*/
    public Contiene getContiene(int index) {
        return this.con[index];
    }
  
    /*Setta la posizione specificata con l'elemento specificato*/
    public void setContiene(int index, Contiene con) {
        this.con[index] = con;
    }
}
