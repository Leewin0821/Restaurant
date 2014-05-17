package Restrauant;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by leewin on 14-3-20.
 * provide a singleton logger to record events
 */
public class RestaurantLogger {
    private static RestaurantLogger restaurantLogger = new RestaurantLogger();
    private Logger logger;
    private FileHandler fileHandler;

    private RestaurantLogger(){
        logger = Logger.getLogger("Restaurant");   // give logger a name to identify
        logger.setLevel(Level.INFO);   // set log level as INFO
        try{
            fileHandler = new FileHandler("Log.txt");  // store log file in Log.txt file
        } catch (IOException ioe){
            System.out.println("Log.txt file doesn't exist: "+ioe);
            System.exit(0);
        }
        fileHandler.setLevel(Level.INFO);  // fileHandler store INFO log
        fileHandler.setFormatter(new MyLogHandlerFormat());  // use customize log format
        logger.addHandler(fileHandler);
    }

    /**
     * use 2-phase create a method to return logger instance
     * @return logger instance
     */
    public static RestaurantLogger getRestaurantLogger ()
    {
        if ( restaurantLogger == null )
        {
            synchronized (RestaurantLogger.class)
            {
                if ( restaurantLogger == null )
                    restaurantLogger = new RestaurantLogger();
            }
        }
        return restaurantLogger;
    }

    /**
     * provide a method to use logger set info
     * @param message displayed message
     */
    public void getInfo(String message){
        logger.info(message);
    }

    /**
     * create inner MyLogHandlerFormat class to create new log format
     */
    class MyLogHandlerFormat extends Formatter {
        @Override
        public String format(LogRecord record) {
            return record.getLevel() + " : " + record.getMessage()+"\n";
        }
    }
}
