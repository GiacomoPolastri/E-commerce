
package model.dao.mySQLJDBCImpl;
import services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.dao.*;

/**
 *
 * @author Giacomo Polastri
 */
public class JDBCImpl extends JDBC{
    
    private Connection connection;

    /**
     *
     * Stabilisce la connessione con il DB settando a false l'autocommit
     */

    @Override
    public void beginTransaction() {
        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        }catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Metodo per fare la commit della transazione
     */

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Metodo per fare la rollBack della transazione
     */

    @Override
    public void rollbackTransaction() {
        try {
            this.connection.rollback();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Metodo per chiudere la connessione con il DB
     */

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BuonoDAO getBuonoDAO() {
        return new BuonoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ContieneDAO getContieneDAO() {
        return new ContieneDAOMySQLJDBCImpl(connection);
    }

    @Override
    public OrdineDAO getOrdineDAO() {
        return new OrdineDAOMySQLJDBCImpl(connection);
    }

    @Override
    public PagamentoDAO getPagamentoDAO() {
        return new PagamentoDAOMySQLJDBCImpl(connection);
    }

    @Override
    public ProdottoDAO getProdottoDAO() {
        return new ProdottoDAOMySQLJDBCImpl(connection) {};
    }
    

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOMySQLJDBCImpl(connection);
    }
    
}
