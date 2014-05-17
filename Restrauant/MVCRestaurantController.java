package Restrauant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * provide a controller to control the interaction of view and model
 */
public class MVCRestaurantController {
    private MVCRestaurantView view;
    private MVCRestaurantModel model;
    private RestaurantLogger restaurantLogger = RestaurantLogger.getRestaurantLogger(); //initialize singleton RestaurantLogger

    public MVCRestaurantController(MVCRestaurantView view, MVCRestaurantModel model){
        this.view = view;
        this.model = model;
        view.addStartListener(new StartController()); // set startButton to response methods in StartController class
        view.addStopListener(new StopController());  // similar to start button
        view.addWriteFileListener(new WriteFileController());
        view.addSearchListener(new SearchController());
    }

    /**
     * create a inner class to hold methods for start button
     */
    private class StartController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            view.disableStartButton();
            model.startProcess();
            view.enableSearchButton();
            view.enableWriteFileButton();
            restaurantLogger.getInfo("Clicked StartButton");
        }
    }

    /**
     * create a inner class to hold methods for stop button
     */
    private class StopController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            view.disableStopButton();
            model.setStop();
            restaurantLogger.getInfo("Clicked StopButton");
        }
    }

    /**
     * create a inner class to hold methods for write file button.
     * ask user whether want to leave when click the button
     */
    private class WriteFileController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            model.writeToFile("Output.txt");
            int n = JOptionPane.showConfirmDialog(null,"Report Has Been Written To Output File Successfully\nReady To Leave?",
                                          "Confirmation",JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                System.exit(1);
            }

        }
    }

    /**
     * create a inner class to hold methods for search button
     */
    private class SearchController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            model.showGUI();
        }
    }
}
