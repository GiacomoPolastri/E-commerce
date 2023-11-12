package services.config;

import java.util.Calendar;
import java.util.logging.Level;
import model.dao.JDBC;

/**
 *
 * @author Giacomo Polastri
 */
public class Configuration {
    
    /*DATABASE CONFIGURATION*/
    /*Costante che mi permette di scegliere il DB da usare. Tale stringa è contenuta
    nel DAOFactory corrispondente al DB che voglio usare*/
    public static final String DAO_IMPL=JDBC.MYSQLJDBCIMPL;
    /*Scelta del Driver*/
    public static final String DATABASE_DRIVER="com.mysql.jdbc.Driver";
    /*Scelta Timezone*/
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();
    /*Configurazione URL database*/
    public static final String DATABASE_URL="jdbc:mysql://localhost/progettoweb?user=root&password=&serverTimezone="+SERVER_TIMEZONE;
    
    /*LOGGER FILES CONFIGURATION*/
    public static final String LOG_NAME="Progetto_log.%g.%u.txt";
    public static final String DIR_LOG="C:\\Users\\Utente\\Documents\\NetBeansProjects\\Progetto\\logs\\" + LOG_NAME;
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;
    
    
    //"jdbc:mysql://localhost/progetto?user=root&password=&serverTimezone="
}