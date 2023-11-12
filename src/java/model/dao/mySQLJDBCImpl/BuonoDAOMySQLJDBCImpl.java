package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

import model.dao.BuonoDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Buono;
import static services.util.Conversion.convertJavaDateToSqlDate;


/**
 *
 * @author Giacomo Polastri
 */
public class BuonoDAOMySQLJDBCImpl implements BuonoDAO{
    
    private final String COUNTER_ID = "codiceBuono";
    private Connection connection;
    
    public BuonoDAOMySQLJDBCImpl(Connection connection){
        
        
        this.connection=connection;
    }

    /**
     * Inserisco nel DB un nuovo buono controllando che non esista già
     * @param nomeBuono
     * @param sconto
     * @param dataScadenza
     * @return Buono
     */

    @Override
    public Buono creaBuono(String nomeBuono, 
                           int sconto, 
                           java.util.Date dataScadenza) throws DuplicatedObjectException{
    
        /*
        *Creo il preparestatement e un nuovo oggetto Buono caricandolo con i 
        *parametri ricevuti
        */
        PreparedStatement ps;
        Buono buono = new Buono();
        buono.setNomeBuono(nomeBuono);
        buono.setSconto(sconto);
        buono.setDataScadenza(dataScadenza);

        try{

            /*query per verificare se il buono esiste già*/
            String sql 
                = " SELECT codiceBuono "
                + " FROM buono "
                + " WHERE "
                + " nomeBuono = ? AND "
                + " sconto = ? AND "
                + " dataScadenza = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, buono.getNomeBuono());
            ps.setDate(i++, convertJavaDateToSqlDate(buono.getDataScadenza()));
            ps.setInt(i++, buono.getSconto());

            ResultSet resultSet = ps.executeQuery();
            
            boolean exist;
            exist = resultSet.next();
            resultSet.close();
            
            if(exist) {
                throw new DuplicatedObjectException("BuonoDAOJDBCImpl.create: Tentativo di inserimento di un buono già esistente.");
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

            buono.setCodiceBuono(resultSet.getLong("counterValue"));

            resultSet.close();
            
            sql
                    = "INSERT INTO buono "
                    + " ( codiceBuono, "
                    + " nomeBuono, "
                    + " dataScadenza, "
                    + " sconto, "
                    + " usato, "
                    + " eliminato "
                    + "    ) "
                    + " VALUES (?,?,?,?,'N','N')";
            
            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, buono.getCodiceBuono());
            ps.setString(i++, buono.getNomeBuono());
            ps.setDate(i++, convertJavaDateToSqlDate(buono.getDataScadenza()));
            ps.setInt(i++, buono.getSconto());
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return buono;
    }

    /**
     * Modifico i valori di un buono nel DB
     * @param buono
     */
    @Override
    public void aggiorna(Buono buono){
        
        PreparedStatement ps;
        try {
            
            /*query per aggiornare il buono*/

            String sql 
                = " UPDATE buono "
                + " SET "
                + "   nomeBuono = ?, "
                + "   dataScadenza = ?, "
                + "   sconto = ?, "
                + "   usato = ? "
                + " WHERE "
                + "   codiceBuono = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, buono.getNomeBuono());
            ps.setDate(i++, convertJavaDateToSqlDate(buono.getDataScadenza()));
            ps.setInt(i++, buono.getSconto());
            if(buono.isUsato()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            ps.setLong(i++, buono.getCodiceBuono());
            
            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * Cancella logicamente un buono dal DB
     * @param codiceBuono
     */

    @Override
    public void eliminaByKey(long codiceBuono) {
        
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE buono "
                + " SET eliminato = 'S' "
                + " WHERE "
                + " codiceBuono = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceBuono);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     *
     * @param nomeBuono
     */
    @Override
    public void eliminaByName(String nomeBuono) {
        
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE buono "
                + " SET eliminato = 'S' "
                + " WHERE "
                + " nomeBuono = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, nomeBuono);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera dal DB tutti i buoni presenti raggruppandoli per nome
     * @return ArrayList di Buono
     */
    @Override
    public ArrayList<Buono> recuperaBuoni() {
        PreparedStatement ps;
        Buono buono;
        ArrayList<Buono> buoni = new ArrayList<Buono>();

        try{
            /*
            *Prende tutti i buoni nel DB raggruppati per nome
            */
            String sql
                = " SELECT * "
                + " FROM buono "
                + " GROUP BY nomeBuono";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                buono = read(resultSet);
                buoni.add(buono);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return buoni;
    }
    
    /**
     * Controlla la validità del buono con il codice del buono
     * @param codiceBuono
     * @return boolean
     */
    @Override
    public boolean checkValidità(long codiceBuono){
        PreparedStatement ps;
        java.util.Date dataScadenza = null;
        String usato = null, eliminato = null;

        try{
            String sql
                = " SELECT dataScadenza, usato, eliminato "
                + " FROM buono "
                + " WHERE codiceBuono = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceBuono);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                dataScadenza = resultSet.getDate("dataScadenza");
                usato = resultSet.getString("usato");
                eliminato = resultSet.getString("eliminato");
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        Date DataOdierna = new Date();
        if(DataOdierna.compareTo(dataScadenza) < 0 && usato.equals("N") && eliminato.equals("N"))
            return true;
        else
            return false;
    }
    
    /**
     * Recupera dal DB il buono con il codice buono
     * @param codiceBuono
     * @return Buono
     */
    @Override
    public Buono findBuonoByKey(long codiceBuono){
        PreparedStatement ps;
        Buono buono = null;
        
        try{
            String sql = " SELECT * "
                    + " FROM buono "
                    + " WHERE codiceBuono = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceBuono);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                buono = read(resultSet);
            }

            resultSet.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        
        return buono;
    }
    
    /**
     *
     * @param nomeBuono
     * @return ArrayList di Buono
     */
    @Override
    public ArrayList<Buono> findBuonoByName(String nomeBuono){
        PreparedStatement ps;
        Buono buono;
        ArrayList<Buono> buoni = new ArrayList<Buono>();
        
        try{
            String sql = " SELECT * "
                    + " FROM buono "
                    + " WHERE nomeBuono = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, nomeBuono);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                buono = read(resultSet);
                buoni.add(buono);
            }

            resultSet.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        
        return buoni;
    }
    
    /**
     * Recupera dal DB il codice dell'ultimo buono inserito
     * @return long
     */
    
    /*Leggo i vari campi del resultset e li carico in buono*/
    protected Buono read(ResultSet resultSet){
        Buono buono = new Buono();
        
        /*Leggo il codice*/
        try {
            buono.setCodiceBuono(resultSet.getLong("codiceBuono"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il nome*/
        try {
            buono.setNomeBuono(resultSet.getString("nomeBuono"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la data di scadenza*/
        try {
            buono.setDataScadenza(resultSet.getDate("dataScadenza"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo lo sconto*/
        try {
            buono.setSconto(resultSet.getInt("sconto"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo usato*/
        try {
            if(resultSet.getString("usato").equals("S")){
                buono.setUsato(true);
            }else{
                buono.setUsato(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo eliminato*/
        try {
            if(resultSet.getString("eliminato").equals("S")){
                buono.setEliminato(true);
            }else{
                buono.setEliminato(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return buono;
    }
    
}
