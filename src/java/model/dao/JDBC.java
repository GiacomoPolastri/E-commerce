package model.dao;

import model.dao.mySQLJDBCImpl.JDBCImpl;

/**
 *
 * @author Giacomo Polastri
 */
public abstract class JDBC {

    // List of DAO types supported by the factory
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";

    /*Metodi per le transazioni*/
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();
    
    /*Metodi per ritornare i DAO*/
    public abstract BuonoDAO getBuonoDAO();
    public abstract ContieneDAO getContieneDAO();
    public abstract OrdineDAO getOrdineDAO();
    public abstract PagamentoDAO getPagamentoDAO();
    public abstract ProdottoDAO getProdottoDAO();
    public abstract UtenteDAO getUtenteDAO();
    
    public static JDBCImpl getJDBCImpl(String whichFactory) {
        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new JDBCImpl();
        }else{
            return null;
        }
    }

}
