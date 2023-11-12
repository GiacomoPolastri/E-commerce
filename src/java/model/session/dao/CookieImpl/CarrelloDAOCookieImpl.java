package model.session.dao.CookieImpl;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.CarrelloDAO;
import model.session.mo.Carrello;

/**
 *
 * @author Giacomo Polastri
 */
public class CarrelloDAOCookieImpl implements CarrelloDAO{
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    public CarrelloDAOCookieImpl(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /**
     *
     * Crea il cookie carrello
     * @param codiceProdotto
     * @param quantita
     * @return ArrayList di Carrello
     */
    @Override
    public ArrayList<Carrello> crea(long codiceProdotto, long quantita) {
        /*Creo un Carrello settandogli gli attributi*/
        Carrello carrello = new Carrello();
        carrello.setCodiceProdotto(codiceProdotto);
        carrello.setQuantità(quantita);
        
        ArrayList<Carrello> listaCarrello = new ArrayList<Carrello>();
        listaCarrello.add(carrello);

        /*
        *Creo un oggetto cookie dandogli il nome carrello e settandogli i
        *parametri come me li ritorna il metodo encode
        */
        Cookie cookie = new Cookie("carrello", encode(listaCarrello));
        /*Specifico che il cookie deve essere valido per tutto il sito*/
        cookie.setPath("/");
        /*Aggiungo un cookie alla response e ritorno il carrello*/
        response.addCookie(cookie);

        return listaCarrello;
    }

    /**
     *
     * Aggiunge un elemento al cookie carrello
     * @param codiceProdotto
     * @param quantita
     */
    @Override
    public void aggiungi(long codiceProdotto, long quantita) {
        ArrayList<Carrello> listaCarrello = trova();
        boolean trovato = false;
        
        Carrello carrello = new Carrello();
        carrello.setCodiceProdotto(codiceProdotto);
        carrello.setQuantità(quantita);
        
        /*Controllo che il prodotto inserito non fosse già nel carrello
        *In tal caso modifico la quantità di quel prodotto nel carrello*/
        for(int i=0 ; i < listaCarrello.size() && !trovato ; i++){
            if(listaCarrello.get(i).getCodiceProdotto() == carrello.getCodiceProdotto()){
                trovato = true;
                carrello.setQuantità(carrello.getQuantità()+listaCarrello.get(i).getQuantità());
                
                /*Rimuovo dalla lista l'elemento in modo da poterci aggiungere
                *l'elemento modificato*/
                listaCarrello.removeIf(n -> (n.getCodiceProdotto() == codiceProdotto));
                elimina();
            }
        }
        
        listaCarrello.add(carrello);
        
        Cookie cookie = new Cookie("carrello", encode(listaCarrello));
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    /**
     *
     * Rimuove un elemento dal cookie carrello
     * @param codiceProdotto
     * @return ArrayList di Carrello
     */
    @Override
    public ArrayList<Carrello> rimuovi(long codiceProdotto) {
        ArrayList<Carrello> listaCarrello = trova();
        Cookie cookie = null;
        
        /*Rimuovo dalla lista l'oggetto con il codiceProdotto passato per parametro*/
        listaCarrello.removeIf(n -> (n.getCodiceProdotto() == codiceProdotto));
        elimina();
        
        if(listaCarrello.size() > 0){
            cookie = new Cookie("carrello", encode(listaCarrello));
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        
        return listaCarrello;
    }

    /**
     *
     * Elimina l'intero cookie carrello
     */
    @Override
    public void elimina() {
        Cookie cookie = new Cookie("carrello", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     *
     * Recupera il cookie carrello
     * @return ArrayList di CArrello
     */
    @Override
    public ArrayList<Carrello> trova() {
        /*Carico l'array dei cookie e l'oggetto Carrello*/
        Cookie[] cookies = request.getCookies();
        ArrayList<Carrello> carrello = new ArrayList<Carrello>();

        /*
        *Se l'array dei cokie non è vuoto, lo scorro tutto oppure fino a quando
        *carrello non è più vuoto
        */
        if (cookies != null) {
            for (int i = 0; i < cookies.length && carrello != null; i++) {
                /*
                *Quando trovo il cookie carrello chiamo il metodo decode
                *per fargli settare e restituirmi l'oggetto carrello
                */
                if (cookies[i].getName().equals("carrello")) {
                    carrello = decode(cookies[i].getValue());
                }
            }
        }

        return carrello;
    }
    
    /**
     *
     * Modifica la quantità del prodotto specificato nel carrello
     * @param codiceProdotto
     * @param quantita
     * @return ArrayList di Carrello
     */
    @Override
    public ArrayList<Carrello> modificaQuantità(long codiceProdotto, long quantita){
        ArrayList<Carrello> listaCarrello = trova();
        
        Carrello carrello = new Carrello();
        
        for(int i=0; i<listaCarrello.size(); i++){
            if(listaCarrello.get(i).getCodiceProdotto() == codiceProdotto){
                listaCarrello.get(i).setQuantità(quantita);
            }
        }
        
        Cookie cookie = new Cookie("carrello", encode(listaCarrello));
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return listaCarrello;
    }
    
    /*
    *Creo una stringa contenente i codici dei prodotti e la relativa quantità 
    *nel carrello dell'utente concatenati ma separati da un #
    */
    private String encode(ArrayList<Carrello> carrello) {
        String encodedTemp = new String();
        for(int i=0 ; i<carrello.size() ; i++){
            encodedTemp += carrello.get(i).getCodiceProdotto() + "#" + carrello.get(i).getQuantità() + "%";
        }
        
        String encoded = encodedTemp.substring(0, encodedTemp.length()-1);
        
        return encoded;
    }
    
    /*
    *Ricevuta la stringa encoded, separo i campi codiceProdotto e quantità
    *separati da # e li setto nell'oggetto di Carrello
    */
    private ArrayList<Carrello> decode(String encoded) {
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String[] values = encoded.split("%");
        
        for(int i=0 ; i<values.length ; i++){
            carrelli.add(decodeAux(values[i]));
        }
        
        return carrelli;
    }
    
    private Carrello decodeAux(String encoded){
        Carrello carrello = new Carrello();
        String[] values = encoded.split("#");
        
        carrello.setCodiceProdotto(Long.parseLong(values[0]));
        carrello.setQuantità(Long.parseLong(values[1]));
        
        return carrello;
    }
    
}
