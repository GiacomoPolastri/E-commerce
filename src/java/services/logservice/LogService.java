package services.logservice;

import services.config.Configuration;

import java.io.IOException;
import java.util.logging.*;

/**
 *
 * @author Giacomo Polastri
 */
public class LogService {
    
    private static Logger applicationLogger;
    
    public LogService(){
    }
    
    public static Logger printLog(){
        SimpleFormatter formatterTxt;
        Handler fileHandler;

        try {

            /*Se è nullo (quindi non esiste),creo un file di log, altrimenti lo ritorno semplicemente*/
            if (applicationLogger == null) {

                applicationLogger = Logger.getLogger(Configuration.LOG_NAME);
                fileHandler = new FileHandler(Configuration.DIR_LOG, true);
                formatterTxt = new SimpleFormatter();
                fileHandler.setFormatter(formatterTxt);
                applicationLogger.addHandler(fileHandler);
                applicationLogger.setLevel(Configuration.GLOBAL_LOGGER_LEVEL);
                applicationLogger.setUseParentHandlers(false);
            }
            
        } catch (IOException e) {
        /*Se ho un'eccezione la loggo nel file con un livello alto*/
            applicationLogger.log(Level.SEVERE, "Error occured in Logger creation", e);
        }
        
        return applicationLogger;
    }
    
}
