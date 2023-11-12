/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dao.ContieneDAO;
import model.dao.exception.DuplicatedObjectException;

import model.mo.Contiene;
import model.mo.Ordine;
import model.mo.Prodotto;

/**
 *
 * @author Giacomo Polastri
 */
public class ContieneDAOMySQLJDBCImpl implements ContieneDAO {
    
     private Connection connection;
    
    public ContieneDAOMySQLJDBCImpl(Connection connection){
        this.connection=connection;
    }

    /**
     *
     * Crea una nuova tupla nella tabella contiene del DB
     * @param codiceOrdine
     * @param codiceProdotto
     * @param quantitaOrdine
     * @return Contiene
     * @throws DuplicatedObjectException
     */
    
    public Contiene creaContiene(long codiceOrdine, 
                                 long codiceProdotto, 
                                 long quantitaOrdine) throws DuplicatedObjectException {
        /*
        *Creo il preparestatement e un nuovo oggetto Contiene caricandolo con i 
        *parametri ricevuti
        */
        PreparedStatement ps;
        Contiene contiene = new Contiene();
        
        Ordine ordine = new Ordine();
        ordine.setCodiceOrdine(codiceOrdine);
        contiene.setOrdine(ordine);
        
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceProdotto(codiceProdotto);
        contiene.setProdotto(prodotto);
        contiene.setQuantitàOrdine(quantitaOrdine);
        

        try{
            
            /*Preparo la query per vedere se esiste già un contiene uguale*/
            String sql
                    = " SELECT codiceProdotto "
                    + " FROM contiene "
                    + " WHERE "
                    + " codiceProdotto = ? AND"
                    + " codiceOrdine = ? AND"
                    + " quantitaOrdine = ? ";

            ps = connection.prepareStatement(sql);
            /*Setto i parametri della query*/
            int i = 1;
            
            ps.setLong(i++, prodotto.getCodiceProdotto());
            ps.setLong(i++, ordine.getCodiceOrdine());
            ps.setLong(i++, contiene.getQuantitàOrdine());
            

            /*Eseguo la query*/
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /*
            *Se exist è true vuol dire che la tupla di contiene esiste già, quindi
            *sollevo l'eccezione DuplicatedObjectException e la gestisco
            */
            if(exist){
                throw new DuplicatedObjectException("ContieneDAOMySQLJDBCImpl.creaContiene: Tentativo di inserimento di un contiene già esistente");
            }

            /*
            *Se sono arrivato qui la tupla in contiene non esiste nel DB quindi
            *creo la query per inserirlo
            */

            sql = " INSERT INTO contiene "
                + "   ( codiceProdotto,"
                + "     codiceOrdine,"
                + "     quantitaOrdine"
                + "   ) "
                + " VALUES (?,?,?)";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, contiene.getProdotto().getCodiceProdotto());
            ps.setLong(i++, contiene.getOrdine().getCodiceOrdine());
            ps.setLong(i++, contiene.getQuantitàOrdine());
            

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return contiene;
    }
    
    /**
     *
     * @param codiceOrdine
     * @return ArrayList di Contiene
     */
    
    public ArrayList<Contiene> findContieneByOrdine(long codiceOrdine){
        PreparedStatement ps;
        Contiene contiene;
        ArrayList<Contiene> contieneArray = new ArrayList<Contiene>();

        try{
            /*
            *Prende tutti gli elementi di contiene in base all'ordine passato
            */
            String sql
                = " SELECT * "
                + " FROM contiene "
                + " WHERE codiceOrdine = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceOrdine);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                contiene = read(resultSet);
                contieneArray.add(contiene);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return contieneArray;
    }
    
    /*Leggo i vari campi del resultset e li carico in ordine*/
    protected Contiene read(ResultSet resultSet){
        Contiene contiene = new Contiene();
        
        Ordine ordine = new Ordine();
        contiene.setOrdine(ordine);
        
        Prodotto prodotto = new Prodotto();
        contiene.setProdotto(prodotto);
     
        /*Leggo il codiceProdotto*/
        try {
            contiene.getProdotto().setCodiceProdotto(resultSet.getLong("codiceProdotto"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il codiceOrdine*/
        try {
            contiene.getOrdine().setCodiceOrdine(resultSet.getLong("codiceOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la quantità*/
        try {
            contiene.setQuantitàOrdine(resultSet.getLong("quantitaOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        
        return contiene;
    }
}

