package model.session.dao.CookieImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.session.dao.LoggedUserDAO;
import model.session.mo.LoggedUser;

/**
 *
 * @author Giacomo Polastri
 */
public class LoggedUserDAOCookieImpl implements LoggedUserDAO{
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    public LoggedUserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /**
     *
     * Crea un nuovo cookie loggedUser
     * @param email
     * @param nomeUtente
     * @param cognome
     * @param admin
     * @return loggedUser
     */
    @Override
    public LoggedUser crea(String email, String nomeUtente, String cognome, boolean admin) {
        /*Creo un LoggedUser settandogli gli attributi*/
        LoggedUser loggedUser = new LoggedUser();
        loggedUser.setEmail(email);
        loggedUser.setNomeUtente(nomeUtente);
        loggedUser.setCognome(cognome);
        loggedUser.setAdmin(admin);

        /*
        *Creo un oggetto cookie dandogli il nome loggedUser e settandogli i
        *parametri come me li ritorna il metodo encode
        */
        Cookie cookie = new Cookie("loggedUser", encode(loggedUser));
        /*Specifico che il cookie deve essere valido per tutto il sito*/
        cookie.setPath("/");
        /*Aggiungo un cookie alla response e ritorno l'utente loggato*/
        response.addCookie(cookie);

        return loggedUser;
    }

    /**
     *
     * Aggiorna il cookie loggedUser
     * @param loggedUser
     */
    @Override
    public void aggiorna(LoggedUser loggedUser) {
        Cookie cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     *
     * Elimina il cookie loggedUser
     */
    @Override
    public void elimina() {
        Cookie cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     *
     * Recupera il cookie loggedUser
     * @return ul
     */
    @Override
    public LoggedUser trova() {
        /*Carico l'array dei cookie e l'oggetto UtenteLoggato*/
        Cookie[] cookies = request.getCookies();
        LoggedUser ul = null;

        /*
        *Se l'array dei cokie non è vuoto, lo scorro tutto oppure fino a quando
        *ul non è più vuoto
        */
        if (cookies != null) {
            for (int i = 0; i < cookies.length && ul == null; i++) {
                /*
                *Quando trovo il cookie loggedUser chiamo il metodo decode
                *per fargli settare e restituirmi l'oggetto ul
                */
                if (cookies[i].getName().equals("loggedUser")) {
                    ul = decode(cookies[i].getValue());
                }
            }
        }

        return ul;
    }
    
    /*
    *Creo una stringa contenente email, nome e cognome dell'utente concatenati 
    *ma separati da un #
    */
    private String encode(LoggedUser loggedUser) {
        String encoded;
        String ad = null;
        if(loggedUser.isAdmin()){
            ad="S";
        }else{
            ad="N";
        }
        encoded = loggedUser.getEmail() + "#" + loggedUser.getNomeUtente() + "#" + loggedUser.getCognome() + "#" + ad;
        return encoded;
    }
    
    /*
    *Ricevuta la stringa encoded, separo i campi email, nome e cognome
    *separati da # e li setto nell'oggetto di UtenteLoggato
    */
    private LoggedUser decode(String encoded) {
        LoggedUser loggedUser = new LoggedUser();
        String[] values = encoded.split("#");

        loggedUser.setEmail(values[0]);
        loggedUser.setNomeUtente(values[1]);
        loggedUser.setCognome(values[2]);
        if(values[3].equals("S")){
            loggedUser.setAdmin(true);
        }else{
            loggedUser.setAdmin(false);
        }

        return loggedUser;
    }
    
}
