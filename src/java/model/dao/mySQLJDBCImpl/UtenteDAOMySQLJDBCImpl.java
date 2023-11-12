package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import model.dao.UtenteDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Utente;

/**
 *
 * @author Giacomo Polastri
 */
public class UtenteDAOMySQLJDBCImpl implements UtenteDAO{
    
    private Connection connection;
    
    public UtenteDAOMySQLJDBCImpl(Connection connection){
        this.connection=connection;
    }

    /**
     *
     * Crea una nuova tupla di utente nel DB
     * @param email
     * @param nomeUtente
     * @param cognome
     * @param password
     * @param genere
     * @param nazione
     * @param citta
     * @param via
     * @param numeroCivico
     * @param CAP
     * @param admin
     * @param blocked
     * @return Utente
     * @throws DuplicatedObjectException
     */
    @Override
    public Utente registrati(String email, 
                             String nomeUtente, 
                             String cognome, 
                             String password, 
                             String genere, 
                             String nazione, 
                             String citta, 
                             String via, 
                             String numeroCivico, 
                             int CAP, 
                             boolean admin, 
                             boolean blocked) throws DuplicatedObjectException {
        /*
        *Creo il preparestatement e un nuovo oggetto Utente caricandolo con i 
        *parametri ricevuti
        */
        PreparedStatement ps;
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setNomeUtente(nomeUtente);
        utente.setCognome(cognome);
        utente.setPassword(password);
        utente.setNazione(nazione);
        utente.setGenere(genere);
        utente.setNazione(nazione);
        utente.setCittà(citta);
        utente.setVia(via);
        utente.setNumeroCivico(numeroCivico);
        utente.setCAP(CAP);
        utente.setAdmin(admin);
        utente.setBlocked(blocked);

        try{

            /*
            *Un utente potrebbe avere più account, quindi mi basta verificare
            *che non ci sia una tupla con la stessa email
            */
            String sql
                    = " SELECT email "
                    + " FROM utente "
                    + " WHERE email = ? ";

            ps = connection.prepareStatement(sql);
            /*Setto i parametri della query*/
            ps.setString(1, utente.getEmail());

            /*Eseguo la query*/
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /*
            *Se exist è true vuol dire che l'utente esiste già, quindi sollevo
            *l'eccezione DuplicatedObjectException e la gestisco
            */
            if(exist){
                throw new DuplicatedObjectException("OrdineDAOMySQLJDBCImpl.creaOrdine: Tentativo di inserimento di un ordine già esistente");
                
            }

            /*
            *Se sono arrivato qui l'account non esiste nel DB quindi creo la
            *query per inserirlo
            */
            sql = " INSERT INTO utente "
                    + "   ( email,"
                    + " nomeUtente,"
                    + " cognome,"
                    + " password,"
                    + " genere,"
                    + " nazione,"
                    + " citta,"
                    + " via,"
                    + " numeroCivico,"
                    + " CAP,"
                    + " admin,"
                    + " blocked"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, utente.getEmail());
            ps.setString(i++, utente.getNomeUtente());
            ps.setString(i++, utente.getCognome());
            ps.setString(i++, utente.getPassword());
            ps.setString(i++, utente.getGenere());
            ps.setString(i++, utente.getNazione());
            ps.setString(i++, utente.getCittà());
            ps.setString(i++, utente.getVia());
            ps.setString(i++, utente.getNumeroCivico());
            ps.setInt(i++, utente.getCAP());
            if(utente.isAdmin()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            if(utente.isBlocked()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            
            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utente;
    }

    /**
     *
     * Recupera dal DB tutti gli utenti registrati
     * @return ArrayList di Utente
     */
    @Override
    public ArrayList<Utente> findUtenti() {
        PreparedStatement ps;
        Utente utente;
        ArrayList<Utente> utenti = new ArrayList<Utente>();

        try{
            /*
            *Prende tutti gli utenti nel DB in ordine alfabetico di cognome
            */
            String sql
                = " SELECT * "
                + " FROM utente "
                + " ORDER BY cognome, nomeUtente";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utenti;
    }
    
    /**
     *
     * Trova le iniziali dei cognomi di tutti gli utenti che possiedono un account
     * @return ArrayList di String
     */

    @Override
    public ArrayList<String> findInitialsUtenti(){
        PreparedStatement ps;
        String initial;
        ArrayList<String> initials = new ArrayList<String>();

        try {
            /*
            *Prende soltanto la prima lettera a sinistra del cognome, la
            *trasforma in maiuscolo e la chiama initial
            */
            String sql
                    = " SELECT DISTINCT UCase(Left(cognome,1)) AS initial "
                    + " FROM utente "
                    + " ORDER BY UCase(Left(cognome,1))";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                initial = resultSet.getString("initial");
                initials.add(initial);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return initials;
    }
    
    /**
     *
     * Recupera dal DB tutti gli utenti che hanno il cognome che inizia con
     * la lettera passata come parametro
     * @param initial
     * @return ArrayList di Utente
     */
    @Override
    public ArrayList<Utente> findUtentiByInitial(String initial){
        PreparedStatement ps;
        Utente utente;
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        
        try {
            /*Query dinamica*/
            String sql
                    = " SELECT * "
                    + " FROM utente "
                    + " WHERE UCASE(LEFT(cognome,1)) = ? "
                    + "ORDER BY cognome, nomeUtente";

            ps = connection.prepareStatement(sql);
            ps.setString(1, initial);
            
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utenti;
    }

    /**
     *
     * Recupera dal DB l'utente che possiede l'email passata come parametro
     * @param email
     * @return Utente
     */
    @Override
    public Utente findByEmail(String email) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM utente "
                    + " WHERE email = ? AND "
                    + "   blocked  = 'N' ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }
            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utente;
    }
    
    /**
     *
     * Blocca l'utente che possiede l'email passata come parametro
     * @param email
     */
    @Override
    public void bloccaUtente(String email){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE utente "
                + " SET "
                + "   blocked = 'S' "
                + " WHERE "
                + "   email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     *
     * Sblocca l'utente che ha l'email passata come parametro
     * @param email
     */
    @Override
    public void sbloccaUtente(String email){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE utente "
                + " SET "
                + "   blocked = 'N' "
                + " WHERE "
                + "   email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /*Leggo i vari campi del resultset e li carico in ordine*/
    protected Utente read(ResultSet resultSet){
        Utente utente = new Utente();
        
        /*Leggo l'email*/
        try {
            utente.setEmail(resultSet.getString("email"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il nome*/
        try {
            utente.setNomeUtente(resultSet.getString("nomeUtente"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il cognome*/
        try {
            utente.setCognome(resultSet.getString("cognome"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la password*/
        try {
            utente.setPassword(resultSet.getString("password"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il genere*/
        try {
            utente.setGenere(resultSet.getString("genere"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la nazione*/
        try {
            utente.setNazione(resultSet.getString("nazione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la città*/
        try {
            utente.setCittà(resultSet.getString("citta"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la via*/
        try {
            utente.setVia(resultSet.getString("via"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il numero civico*/
        try {
            utente.setNumeroCivico(resultSet.getString("numeroCivico"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il CAP*/
        try {
            utente.setCAP(resultSet.getInt("CAP"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo admin*/
        try {
            if(resultSet.getString("Admin").equals("S")){
                utente.setAdmin(true);
            }else{
                utente.setAdmin(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo blocked*/
        try {
            if(resultSet.getString("Blocked").equals("S")){
                utente.setBlocked(true);
            }else{
                utente.setBlocked(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return utente;
    }
    
}
