package model.mo;

/**
 *
 * @author Giacomo Polastri
 */
public class Utente {
    
    private String nomeUtente, cognome, email;
    private String password, genere;
    private String nazione, citta, via, numeroCivico;
    private int CAP;
    private boolean admin, blocked;
    
    /*1 utente N pagamenti*/
    private Pagamento[] pagamenti;
    /*1 utente N ordini*/
    private Ordine[] ordini;
    
    public String getNomeUtente(){
        return nomeUtente;
    }
    
    public void setNomeUtente(String n){
        nomeUtente = n;
    }
    
    public String getCognome(){
        return cognome;
    }
    
    public void setCognome(String c){
        cognome=c;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String e){
        email=e;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String p){
        password=p;
    }
    
    public String getGenere(){
        return genere;
    }
    
    public void setGenere(String g){
        genere=g;
    }
    
    public String getNazione(){
        return nazione;
    }
    
    public void setNazione(String n){
        nazione=n;
    }
    
    public String getCittà(){
        return citta;
    }
    
    public void setCittà(String c){
        citta=c;
    }
    
    public String getVia(){
        return via;
    }
    
    public void setVia(String v){
        via=v;
    }
    
    public String getNumeroCivico(){
        return numeroCivico;
    }
    
    public void setNumeroCivico(String n){
        numeroCivico=n;
    }
    
    public int getCAP(){
        return CAP;
    }
    
    public void setCAP(int cap){
        CAP=cap;
    }
    
    public boolean isAdmin(){
        return admin;
    }
   
    public void setAdmin(boolean a){
        admin=a;
    }
    
    public boolean isBlocked(){
        return blocked;
    }
    
    public void setBlocked(boolean b){
        blocked=b;
    }
    
    /*Ritorna l'intero array*/
    public Pagamento[] getPagamento() {
        return pagamenti;
    }

    /*Setta l'intero array*/
    public void setPagamento(Pagamento[] pagamenti) {
       this.pagamenti = pagamenti;
    }
    
    /*Ritorna l'elemento della posizione specificata*/
    public Pagamento getPagamento(int index) {
        return this.pagamenti[index];
    }
  
    /*Setta la posizione specificata con l'elemento specificato*/
    public void setPagamento(int index, Pagamento pagamenti) {
        this.pagamenti[index] = pagamenti;
    }
    
    /*Ritorna l'intero array*/
    public Ordine[] getOrdine() {
        return ordini;
    }

    /*Setta l'intero array*/
    public void setOrdine(Ordine[] ordini) {
       this.ordini = ordini;
    }
    
    /*Ritorna l'elemento della posizione specificata*/
    public Ordine getOrdine(int index) {
        return this.ordini[index];
    }
  
    /*Setta la posizione specificata con l'elemento specificato*/
    public void setOrdine(int index, Ordine ordini) {
        this.ordini[index] = ordini;
    }
    
}
