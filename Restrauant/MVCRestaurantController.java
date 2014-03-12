package Restrauant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by leewin on 14-3-11.
 */
public class MVCRestaurantController {
    private MVCRestaurantView view;
    private MVCRestaurantModel model;

    public MVCRestaurantController(MVCRestaurantView view, MVCRestaurantModel model){
        this.view = view;
        this.model = model;
        view.addStartListener(new StartController());
        view.addStopListener(new StopController());
    }

    private class StartController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            view.disableStartButton();
            Thread thread = new Thread(model);
            thread.start();
        }
    }

    private class StopController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            model.setInterrupted();
        }
    }
}
