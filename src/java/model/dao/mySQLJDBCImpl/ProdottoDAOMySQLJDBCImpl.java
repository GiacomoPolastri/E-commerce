package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import model.dao.ProdottoDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Contiene;
import model.mo.Prodotto;

/**
 *
 * @author Giacomo Polastri
 */
public class ProdottoDAOMySQLJDBCImpl implements ProdottoDAO{
    
    private final String COUNTER_ID = "codiceProdotto";
    private Connection connection;
    
    public ProdottoDAOMySQLJDBCImpl(Connection connection){
        this.connection=connection;
    }

    /**
     *
     * Crea una nuova tupla di prodotto nel DB
     * @param nomeCarta
     * @param tipoCarta
     * @param rarita
     * @param edizione
     * @param testo
     * @param immagine
     * @param prezzo
     * @param quantita
     * @param blocked
     * @param push
     * @return Prodotto
     * @throws DuplicatedObjectException
     */
    @Override
    public Prodotto creaProdotto(String nomeCarta, 
                                 String tipoCarta, 
                                 String rarita, 
                                 String edizione, 
                                 String testo, 
                                 String immagine, 
                                 float prezzo, 
                                 long quantita, 
                                 boolean blocked, 
                                 boolean push, 
                                 Contiene[] contiene) throws DuplicatedObjectException {
        /*
        *Creo il preparestatement e un nuovo oggetto Prodotto caricandolo con i 
        *parametri ricevuti
        */
        PreparedStatement ps;
        Prodotto prodotto = new Prodotto();
        prodotto.setNomeCarta(nomeCarta);
        prodotto.setTipoCarta(tipoCarta);
        prodotto.setRarità(rarita);
        prodotto.setEdizione(edizione);
        prodotto.setImmagine(immagine);
        prodotto.setTesto(testo);
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantità(quantita);
        prodotto.setBlocked(blocked);
        prodotto.setPush(push);
        prodotto.setContiene(contiene);

        try{

            /*Preparo la query per vedere se esiste già un prodotto uguale*/
            String sql
                    = " SELECT codiceProdotto "
                    + " FROM prodotto "
                    + " WHERE "
                    + " nomeCarta = ? AND"
                    + " tipoCarta = ? AND"
                    + " rarita = ? AND"
                    + " edizione = ? AND"
                    + " immagine = ? AND"
                    + " testo = ? AND"
                    + " quantita = ? AND"
                    + " prezzo = ? ";

            ps = connection.prepareStatement(sql);
            /*Setto i parametri della query*/
            int i = 1;
            ps.setString(i++, prodotto.getNomeCarta());
            ps.setString(i++, prodotto.getTipoCarta());
            ps.setString(i++, prodotto.getRarità());
            ps.setString(i++, prodotto.getEdizione());
            ps.setString(i++, prodotto.getImmagine());
            ps.setString(i++, prodotto.getTesto());
            ps.setLong(i++, prodotto.getQuantità());
            ps.setFloat(i++, prodotto.getPrezzo());

            /*Eseguo la query*/
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /*
            *Se exist è true vuol dire che il prodotto esiste già, quindi sollevo
            *l'eccezione DuplicatedObjectException e la gestisco
            */
            if(exist){
                throw new DuplicatedObjectException("ProdottoDAOMySQLJDBCImpl.creaProdotto: Tentativo di inserimento di un prodotto già esistente");
            }
            sql 
                    = "UPDATE counter "
                    + "SET counterValue=counterValue+1 "
                    + "WHERE counterID='" + COUNTER_ID + "'";

            ps = connection.prepareStatement(sql);
            ps.executeUpdate();

            sql 
                    = "SELECT counterValue "
                    + "FROM counter "
                    + "WHERE counterId='" + COUNTER_ID + "'";

            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();
            resultSet.next();

            prodotto.setCodiceProdotto(resultSet.getLong("counterValue"));

            resultSet.close();
            /*
            *Se sono arrivato qui il prodotto non esiste nel DB quindi creo la
            *query per inserirlo
            */
            
            sql = " INSERT INTO prodotto "
                    + "   ( codiceProdotto , "
                    + " nomeCarta, "
                    + " tipoCarta, "
                    + " rarita, "
                    + " edizione, "
                    + " testo, "
                    + " immagine, "
                    + " prezzo, "
                    + " quantita, "
                    + " blocked, "
                    + " push "
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,'S','N')";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, prodotto.getCodiceProdotto());
            ps.setString(i++, prodotto.getNomeCarta());
            ps.setString(i++, prodotto.getTipoCarta());
            ps.setString(i++, prodotto.getRarità());
            ps.setString(i++, prodotto.getEdizione());
            ps.setString(i++, prodotto.getTesto());
            ps.setString(i++, prodotto.getImmagine());
            ps.setFloat(i++, prodotto.getPrezzo());
            ps.setLong(i++, prodotto.getQuantità());


            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotto;
    }

    /**
     *
     * Modifica i valori di un prodotto nel DB controllando che non venga duplicato
     * @param prodotto
     * @throws DuplicatedObjectException
     */

    @Override
    public void aggiorna(Prodotto prodotto) throws DuplicatedObjectException {
        PreparedStatement ps;
        try {
            /*
            *Preparo la query per vedere se nel DB esiste già un prodotto uguale
            *a quello che voglio aggiornare
            */
            String sql
                    = " SELECT codiceProdotto "
                    + " FROM prodotto "
                    + " WHERE "
                    + " nomeCarta = ? AND"
                    + " tipoCarta = ? AND"
                    + " rarita = ? AND"
                    + " edizione = ? AND"
                    + " immagine = ? AND"
                    + " testo = ? AND"
                    + " prezzo = ? AND"
                    + " quantita = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, prodotto.getNomeCarta());
            ps.setString(i++, prodotto.getTipoCarta());
            ps.setString(i++, prodotto.getRarità());
            ps.setString(i++, prodotto.getEdizione());
            ps.setString(i++, prodotto.getImmagine());
            ps.setString(i++, prodotto.getTesto());
            ps.setFloat(i++, prodotto.getPrezzo());
            ps.setLong(i++, prodotto.getQuantità());
            
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /*
            *Se exist è true vuol dire che il prodotto esiste già, quindi sollevo
            *l'eccezione DuplicatedObjectException e la gestisco
            */
            if(exist) {
                throw new DuplicatedObjectException("BuonoDAOMySQLJDBCImpl.aggiorna: Tentativo di aggiornamento in un buono già esistente");
            }
            
            /*
            *Se sono arrivato qui il prodotto non esiste nel DB quindi creo la
            *query per aggiornarlo
            */

            sql 
                = " UPDATE prodotto "
                + " SET nomeCarta = ?, "
                + " tipoCarta = ?, "
                + " rarita = ?, "
                + " edizione = ?, "
                + " immagine = ?, "
                + " testo = ?, "
                + " quantita = ?, "
                + " blocked = ?, "
                + " push = ? "
                + " WHERE "
                + " codiceProdotto = ?";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setString(i++, prodotto.getNomeCarta());
            ps.setString(i++, prodotto.getTipoCarta());
            ps.setString(i++, prodotto.getRarità());
            ps.setString(i++, prodotto.getEdizione());
            ps.setString(i++, prodotto.getImmagine());
            ps.setString(i++, prodotto.getTesto());
            ps.setLong(i++, prodotto.getQuantità());
            if(prodotto.isBlocked()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            if(prodotto.isPush()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            ps.setLong(i++, prodotto.getCodiceProdotto());
            
            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Setta come bloccato un prodotto nel DB
     * @param codiceProdotto
     */
    @Override
    public void blocca(long codiceProdotto) {
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE prodotto "
                + " SET blocked = 'S' "
                + " WHERE "
                + " codiceProdotto = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProdotto);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     *
     * Setta come sbloccato un prodotto nel DB
     * @param codiceProdotto
     */
    @Override
    public void sblocca(long codiceProdotto) {
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE prodotto "
                + " SET blocked = 'N' "
                + " WHERE "
                + " codiceProdotto = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProdotto);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     *
     * Recupera dal DB tutte le carte presenti
     * @return ArrayList di String
     */
    
    public ArrayList<String> trovaNomeCarte(){
        PreparedStatement ps;
        String nomeCarta;
        ArrayList<String> nomeCarte = new ArrayList<String>();
        
        try{
            /*
            *Prendo tutte le carte esistenti in ordine alfabetico una volta sola
            */
            String sql
                    = " SELECT DISTINCT nomeCarta "
                    + " FROM prodotto "
                    + " ORDER BY nomeCarta ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                nomeCarta = resultSet.getString("nomeCarta");
                nomeCarte.add(nomeCarta);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return nomeCarte;
    }
    
    /**
     *
     * Recupera dal DB tutti i tipi di carta presenti
     * @return ArrayList di String
     */
    
    public ArrayList<String> trovaTipoCarte(){
        PreparedStatement ps;
        String tipoCarta;
        ArrayList<String> tipoCarte = new ArrayList<String>();
        
        try{
            /*
            *Prendo tutti i tipi esistenti in ordine alfabetico una volta sola
            */
            String sql
                    = " SELECT DISTINCT tipoCarta "
                    + " FROM prodotto "
                    + " ORDER BY tipoCarta ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                tipoCarta = resultSet.getString("tipoCarta");
                tipoCarte.add(tipoCarta);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return tipoCarte;
    }
    
    /**
     *
     * Recupera dal DB tutte le diverse rarità presenti
     * @return ArrayList di String
     */
    @Override
    public ArrayList<String> trovaRare(){
        PreparedStatement ps;
        String rarita;
        ArrayList<String> rare = new ArrayList<String>();
        
        try{
            /*
            *Prendo tutte le rarità esistenti in ordine alfabetico una volta sola
            */
            String sql
                    = " SELECT DISTINCT rarita "
                    + " FROM prodotto "
                    + " ORDER BY rarita ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                rarita = resultSet.getString("rarita");
                rare.add(rarita);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return rare;
    }
    
    /**
     *
     * Recupera dal DB tutte le edizioni presenti
     * @return ArrayList di String
     */
    @Override
    public ArrayList<String> trovaEdizioni(){
        PreparedStatement ps;
        String edizione;
        ArrayList<String> edizioni = new ArrayList<String>();
        
        try{
            /*
            *Prendo tutte le edizioni esistenti in ordine alfabetico una volta sola
            */
            String sql
                    = " SELECT DISTINCT edizione "
                    + " FROM prodotto "
                    + " ORDER BY edizione ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                edizione = resultSet.getString("edizione");
                edizioni.add(edizione);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return edizioni;
    }

    /**
     *
     * Recupera dal DB tutti i prodotti appartenenti all'edizione specificata
     * @param edizione
     * @return ArrayList di Prodotto
     */
    
    public ArrayList<Prodotto> findByEdizione(String edizione) {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti dell'edizione specificata
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE edizione = ? AND blocked = 'N' ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, edizione);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }

    /**
     *
     * Recupera dal DB tutti i prodotti del tipo specificato
     * @param tipoCarta
     * @return ArrayList di Prodotto
     */
    @Override
    public ArrayList<Prodotto> findByTipoCarta(String tipoCarta) {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti del tipo specificato
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE tipoCarta = ? AND Blocked = 'N' ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, tipoCarta);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }

    /**
     *
     * Recupera dal DB tutti i prodotti della rarità specificata
     * @param rarita
     * @return ArrayList di Prodotto
     */
    @Override
    public ArrayList<Prodotto> findByRarità(String rarita) {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti in base alla rarità specificata
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE rarita = ? AND blocked = 'N' ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, rarita);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }

    /**
     *
     * Recupera dal DB tutti i prodotti che soddisfano la stringa di ricerca search
     * @param search
     * @return ArrayList di Prodotto
     */
    @Override
    public ArrayList<Prodotto> findByString(String search) {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti in base alla stringa specificata
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE nomeCarta LIKE '%" +search+ "%' OR"
                + " tipoCarta LIKE '%" +search+ "%' OR"
                + " rarita LIKE '%" +search+ "%' OR"
                + " edizione LIKE '%" +search+ "%' OR"
                + " testo LIKE '%" +search+ "%'"
                + " AND Blocked = 'N' ";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }
    
    /**
     *
     * Recupera dal DB il prodotto con il codiceProdotto specificato
     * @param codiceProdotto
     * @return Prodotto
     */
    @Override
    public Prodotto findByKey(Long codiceProdotto){
        PreparedStatement ps;
        Prodotto prodotto = null;

        try{
            /*
            *Prende il prodotto con la chiave specificata
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE codiceProdotto = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProdotto);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                prodotto = read(resultSet);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotto;
    }

    /**
     *
     * Recupera dal DB tutti i prodotti inseriti nella vetrina di push in home
     * page da parte dell'admin
     * @return ArrayList di Prodotto
     */
    @Override
    public ArrayList<Prodotto> findForPush() {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti in vetrina
            */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE push = 'S' AND blocked = 'N'";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }
    
    /**
     *
     * Recupera dal DB tutti i prodotti presenti
     * @return ArrayList di Prodotto
     */
    @Override
    public ArrayList<Prodotto> trovaProdotti(){
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /*
            *Prende tutti i prodotti nel DB
            */
            String sql
                = " SELECT * "
                + " FROM prodotto ";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }
    
    /**
     *
     * Recupera dal DB il codice dell'ultimo prodotto inserito
     * @return long
     */

    
    /*Leggo i vari campi del resultset e li carico in prodotto*/
    protected Prodotto read(ResultSet resultSet){
        Prodotto prodotto = new Prodotto();
        
        /*Leggo il codice*/
        try {
            prodotto.setCodiceProdotto(resultSet.getLong("codiceProdotto"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il nome*/
        try {
            prodotto.setNomeCarta(resultSet.getString("nomeCarta"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il tipo*/
        try {
            prodotto.setTipoCarta(resultSet.getString("tipoCarta"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo prezzo*/
        try {
            prodotto.setPrezzo(resultSet.getFloat("prezzo"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo rarità*/
        try {
            prodotto.setRarità(resultSet.getString("rarita"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo edizione*/
        try {
            prodotto.setEdizione(resultSet.getString("edizione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo l'immagine*/
        try {
            prodotto.setImmagine(resultSet.getString("immagine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la descrizione*/
        try {
            prodotto.setTesto(resultSet.getString("testo"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo blocked*/
        try {
            if(resultSet.getString("blocked").equals("S")){
                prodotto.setBlocked(true);
            }else{
                prodotto.setBlocked(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo push*/
        try {
            if(resultSet.getString("push").equals("S")){
                prodotto.setPush(true);
            }else{
                prodotto.setPush(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return prodotto;
    }
    
    public long getQuantitàByKey(long codiceProdotto){
        PreparedStatement ps;
        long quantita = 0;

        try{
            /*
            *Prende la disponibilità di magazzino in base al codiceProdotto
            */
            String sql
                = " SELECT quantita "
                + " FROM prodotto "
                + " WHERE codiceProdotto = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProdotto);
            

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                quantita = resultSet.getLong("quantita");
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return quantita;
    }

}
