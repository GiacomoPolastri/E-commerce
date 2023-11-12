package model.dao.mySQLJDBCImpl;

import java.sql.*;

import model.dao.PagamentoDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Ordine;
import model.mo.Pagamento;
import model.mo.Utente;

import static services.util.Conversion.convertJavaDateToSqlDate;

/**
 *
 * @author Giacomo Polastri
 */
public class PagamentoDAOMySQLJDBCImpl implements PagamentoDAO{
    
    private final String COUNTER_ID = "codicePagamento";
    private Connection connection;
    
    public PagamentoDAOMySQLJDBCImpl(Connection connection){
        this.connection=connection;
    }

    /**
     *
     * Metodo che crea una nuova tupla di pagamento nel DB
     * @param statoPagamento
     * @param cartaPagamento
     * @param dataRichiestaPagamento
     * @param dataPagamento
     * @param importo
     * @param utente
     * @param ordine
     * @return pagamento
     * @throws DuplicatedObjectException
     */
    @Override
    public Pagamento creaPagamento(String statoPagamento, 
                                   String cartaPagamento, 
                                   java.util.Date dataRichiestaPagamento, 
                                   java.util.Date dataPagamento, 
                                   float importo, 
                                   Utente utente, 
                                   Ordine ordine) throws DuplicatedObjectException {
        /*
        *Creo il preparestatement e un nuovo oggetto Pagamento caricandolo con i 
        *parametri ricevuti
        */
        
        PreparedStatement ps;
        Pagamento pagamento = new Pagamento();
        pagamento.setStatoPagamento(statoPagamento);
        pagamento.setCartaPagamento(cartaPagamento);
        pagamento.setDataRichiestaPagamento(dataRichiestaPagamento);
        pagamento.setDataPagamento(dataPagamento);
        pagamento.setImporto(importo);
        pagamento.setUtente(utente);
        pagamento.setOrdine(ordine);

        try{

            /*Preparo la query per vedere se esiste già un pagamento uguale*/
            String sql
                    = " SELECT codicePagamento "
                    + " FROM pagamento "
                    + " WHERE "
                    + " statoPagamento = ? AND"
                    + " cartaPagamento = ? AND"
                    + " dataRichiestaPagamento = ? AND"
                    + " dataPagamento = ? AND"
                    + " importo = ? AND"
                    + " email = ? ";

            ps = connection.prepareStatement(sql);
            /*Setto i parametri della query*/
            int i = 1;
            ps.setString(i++, pagamento.getStatoPagamento());
            ps.setString(i++, pagamento.getCartaPagamento());
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataRichiestaPagamento()));
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataPagamento()));
            ps.setFloat(i++, pagamento.getImporto());
            ps.setString(i++, pagamento.getUtente().getEmail());

            /*Eseguo la query*/
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            
            /*Se exist è true vuol dire che il pagamento è già stato effettuato,
            *quindi sollevo l'eccezione DuplicatedObjectException e la gestisco*/
            
            if(exist){
                throw new DuplicatedObjectException("PagamentoDAOMySQLJDBCImpl.creaPagamento: Tentativo di inserimento di un pagamento già esistente");
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

            pagamento.setCodicePagamento(resultSet.getLong("counterValue"));

            resultSet.close();
            /*Se sono arrivato qui il pagamento non esiste nel DB quindi creo la
            *query per inserire il nuovo pagamento
            */
             sql 
                    = " INSERT INTO pagamento "
                    + "   ( codicePagamento,"
                    + " dataRichiestaPagamento,"
                    + " dataPagamento,"
                    + " statoPagamento,"
                    + " cartaPagamento,"
                    + " importo,"
                    + " email "
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
             i = 1;
            ps.setLong(i++, pagamento.getCodicePagamento());
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataRichiestaPagamento()));
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataPagamento()));
            ps.setString(i++, pagamento.getStatoPagamento());
            ps.setString(i++, pagamento.getCartaPagamento());
            ps.setFloat(i++, pagamento.getImporto());
            ps.setString(i++, pagamento.getUtente().getEmail());
            
            ps.executeUpdate();
            
            

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return pagamento;
    }
    
    /**
     *
     * Recupera dal DB il codice dell'ultimo pagamento inserito nel DB
     * @return long
     */

    
    /**
     *
     * @param codicePagamento
     * @return float
     */
    @Override
    public float getImporto(long codicePagamento){
        PreparedStatement ps;
        float importo = 0;
        try{
            String sql = " SELECT importo "
                    + " FROM pagamento "
                    + " WHERE codicePagamento = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setLong(1, codicePagamento);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                importo = rs.getFloat("importo");
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return importo;
    }
}
