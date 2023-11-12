package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

import model.dao.OrdineDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Buono;
import model.mo.Contiene;
import model.mo.Ordine;
import model.mo.Pagamento;
import model.mo.Utente;

import static services.util.Conversion.convertJavaDateToSqlDate;

/**
 *
 * @author Giacomo Polastri
 */
public class OrdineDAOMySQLJDBCImpl implements OrdineDAO{
    
    private final String COUNTER_ID = "codiceOrdine";
    private Connection connection;
    
    public OrdineDAOMySQLJDBCImpl(Connection connection){
        this.connection=connection;
    }

    /**
     *
     * Crea una nuova tupla di ordine nel DB
     * @param dataOrdine
     * @param statoOrdine
     * @param dataConsegna
     * @param nazione
     * @param citta
     * @param via
     * @param numeroCivico
     * @param CAP
     * @param pagamento
     * @param buono
     * @param utente
     * @param contiene
     * @return Ordine
     * @throws DuplicatedObjectException
     */
    
    public Ordine creaOrdine(Date dataOrdine,
                             String statoOrdine, 
                             Date dataConsegna,
                             String nazione, 
                             String citta, 
                             String via, 
                             long numeroCivico,
                             int CAP,
                             Pagamento pagamento, 
                             Buono buono, 
                             Utente utente, 
                             ArrayList<Contiene> contiene) throws DuplicatedObjectException {
        /*
        *Creo il preparestatement e un nuovo oggetto Ordine caricandolo con i 
        *parametri ricevuti
        */
        PreparedStatement ps;
        Ordine ordine = new Ordine();
        ordine.setDataOrdine(dataOrdine);
        ordine.setStatoOrdine(statoOrdine);
        ordine.setDataConsegna(dataConsegna);
        ordine.setNazione(nazione);
        ordine.setCittà(citta);
        ordine.setVia(via);
        ordine.setNumeroCivico(numeroCivico);
        ordine.setCAP(CAP);
        ordine.setPagamento(pagamento);
        ordine.setBuono(buono);
        ordine.setUtente(utente);
        ordine.setContiene(contiene);

        try{

            /*Preparo la query per vedere se esiste già un ordine uguale*/
            String sql
                    = " SELECT codiceOrdine "
                    + " FROM ordine "
                    + " WHERE "
                    + " dataOrdine = ? AND "
                    + " statoOrdine = ? AND"
                    + " dataConsegna = ? AND "
                    + " nazione = ? AND"
                    + " citta = ? AND"
                    + " via = ? AND"
                    + " numeroCivico = ? AND "
                    + " CAP = ? AND "
                    + " codicePagamento = ? AND"
                    + " codiceBuono = ? AND"
                    + " email = ? ";

            ps = connection.prepareStatement(sql);
            /*Setto i parametri della query*/
            int i = 1;
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataOrdine()));
            ps.setString(i++, ordine.getStatoOrdine());
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataConsegna()));
            ps.setString(i++, ordine.getNazione());
            ps.setString(i++, ordine.getCittà());
            ps.setString(i++, ordine.getVia());
            ps.setLong(i++, ordine.getNumeroCivico());
            ps.setInt(i++, ordine.getCAP());
            ps.setLong(i++, pagamento.getCodicePagamento());
            if(buono != null){
                ps.setLong(i++, buono.getCodiceBuono());
            }else{
                ps.setString(i++, null);
            }
            ps.setString(i++, utente.getEmail());

            /*Eseguo la query*/
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /*
            *Se exist è true vuol dire che l'ordine esiste già, quindi sollevo
            *l'eccezione DuplicatedObjectException e la gestisco
            */
            if(exist){
                throw new DuplicatedObjectException("OrdineDAOMySQLJDBCImpl.creaOrdine: Tentativo di inserimento di un ordine già esistente");
            }
            
            sql 
                    = "UPDATE counter "
                    + "SET counterValue=counterValue+1 "
                    + "WHERE counterId='" + COUNTER_ID + "'";

            ps = connection.prepareStatement(sql);
            ps.executeUpdate();

            sql 
                    = "SELECT counterValue "
                    + "FROM counter "
                    + "WHERE counterId='" + COUNTER_ID + "'";

            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();
            resultSet.next();
            
            ordine.setCodiceOrdine(resultSet.getLong("counterValue"));
            
            resultSet.close();
            
            /*
            *Se sono arrivato qui l'ordine non esiste nel DB quindi creo la
            *query per inserire il nuovo ordine
            */
            sql
                    = " INSERT INTO ordine "
                    + " ( codiceOrdine, "
                    + " dataOrdine, "
                    + " statoOrdine, "
                    + " dataConsegna, "
                    + " nazione, "
                    + " citta, "
                    + " via, "
                    + " numeroCivico, "
                    + " CAP, "
                    + " codicePagamento,"
                    + " codiceBuono,"
                    + " email"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, ordine.getCodiceOrdine());
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataOrdine()));
            ps.setString(i++, ordine.getStatoOrdine());
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataConsegna()));
            ps.setString(i++, ordine.getNazione());
            ps.setString(i++, ordine.getCittà());
            ps.setString(i++, ordine.getVia());
            ps.setLong(i++, ordine.getNumeroCivico());
            ps.setInt(i++, ordine.getCAP());
            ps.setLong(i++, pagamento.getCodicePagamento());
           if(buono != null){
                ps.setLong(i++, buono.getCodiceBuono());
            }else{
                ps.setString(i++, null);
            }
            ps.setString(i++, utente.getEmail());

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordine;
    }

    /**
     *
     * Modifica lo stato dell'ordine nel DB
     * @param codiceOrdine
     * @param statoOrdine
     */
    
    public void aggiornaStato(long codiceOrdine, 
                              String statoOrdine){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE ordine "
                + " SET "
                + "   statoOrdine = ? "
                + " WHERE "
                + "   codiceOrdine = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, statoOrdine);
            ps.setLong(i++, codiceOrdine);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     *
     * Modifica lo stato dell'ordine nel DB
     * @param codiceOrdine
     * @param statoOrdine
     * @param dataOdierna
     */
    
    public void aggiornaStatoConData(long codiceOrdine, 
                                     String statoOrdine, 
                                     Date dataOdierna){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE ordine "
                + " SET "
                + " statoOrdine = ?, "
                + " dataConsegna = ? "
                + " WHERE "
                + " codiceOrdine = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, statoOrdine);
            ps.setDate(i++, convertJavaDateToSqlDate(dataOdierna));
            ps.setLong(i++, codiceOrdine);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Recupera dal DB tutti gli ordini presenti
     * @return ArrayList di ordine
     */
    
    public ArrayList<Ordine> findOrdini() {
        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        try{
            /*
            *Prende tutti gli ordini nel DB in ordine di dataOrdine
            */
            String sql
                = " SELECT * "
                + " FROM ordine "
                + " ORDER BY dataOrdine";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                ordine = read(resultSet);
                ordini.add(ordine);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordini;
    }

    /**
     *
     * Recupera tutti gli ordini dal DB dell'utente con l'email passata come
     * parametro
     * @param email
     * @return ArrayList di Ordine
     */
    
    public ArrayList<Ordine> findByUtente(String email) {
        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        try{
            /*
            *Prende tutti gli ordini dell'utente passato nel DB in ordine di dataOrdine
            */
            String sql
                = " SELECT * "
                + " FROM ordine "
                + " WHERE email = ?"
                + " ORDER BY dataOrdine";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                ordine = read(resultSet);
                ordini.add(ordine);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordini;
    }
    
    /**
     *
     * Recupera dal DB il codice dell'ultimo ordine inserito
     * @return long
     */
    
    /**
     *
     * Conta gli ordini effettuati dall'utente con l'email passata come parametro
     * @param email
     * @return int
     */
    
    public int contaOrdini(String email){
        PreparedStatement ps;
        int Numero = 0;
        try{
            String sql = " SELECT COUNT(*) AS numeroOrdini "
                    + " FROM ordine "
                    + " WHERE email = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ResultSet resultSet = ps.executeQuery();
            
            if(resultSet.next()){
                Numero = resultSet.getInt("numeroOrdini");
            }
            
            resultSet.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return Numero;
    }
    
    /*Leggo i vari campi del resultset e li carico in ordine*/
    protected Ordine read(ResultSet resultSet){
        Ordine ordine = new Ordine();
        
        Pagamento pagamento = new Pagamento();
        ordine.setPagamento(pagamento);
        
        Utente utente = new Utente();
        ordine.setUtente(utente);
        
        Buono buono = new Buono();
        ordine.setBuono(buono);
        
        /*Leggo il codice*/
        try {
            ordine.setCodiceOrdine(resultSet.getLong("codiceOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo dataOrdine*/
        try {
            ordine.setDataOrdine(resultSet.getDate("dataOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo lo stato*/
        try {
            ordine.setStatoOrdine(resultSet.getString("statoOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo dataConsegna*/
        try {
            ordine.setDataConsegna(resultSet.getDate("dataConsegna"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo nazione*/
        try {
            ordine.setNazione(resultSet.getString("nazione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo città*/
        try {
            ordine.setCittà(resultSet.getString("citta"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo via*/
        try {
            ordine.setVia(resultSet.getString("via"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il numero civico*/
        try {
            ordine.setNumeroCivico(resultSet.getLong("numeroCivico"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il CAP*/
        try {
            ordine.setCAP(resultSet.getInt("CAP"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo codicePagamento*/
        try {
            ordine.getPagamento().setCodicePagamento(resultSet.getLong("codicePagamento"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo codiceBuono*/
        try {
            ordine.getBuono().setCodiceBuono(resultSet.getLong("codiceBuono"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo email*/
        try {
            ordine.getUtente().setEmail(resultSet.getString("email"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return ordine;
    }
}
