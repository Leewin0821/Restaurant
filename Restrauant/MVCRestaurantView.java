package Restrauant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by leewin on 14-3-11.
 */
public class MVCRestaurantView extends JFrame implements Observer{
    private MVCRestaurantModel model;
    private JButton startButton;
    private JButton stopButton;
    private JButton searchButton;
    private JButton writeFileButton;
    private JTextArea kitchenWindow;
    private JTextArea [] tableWindows;

    public MVCRestaurantView(MVCRestaurantModel model){
        this.model = model;
        model.registerObserver(this);

        setTitle("Restaurant");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(200,200);


        Container contentPane = getContentPane();
        contentPane.add(createWestPanel(),BorderLayout.WEST);
        contentPane.add(createEastPanel(),BorderLayout.EAST);
        contentPane.add(createSouthPanel(),BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private JPanel createWestPanel(){
        JPanel westPanel = new JPanel();
        kitchenWindow = new JTextArea(45,45);
        kitchenWindow.setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.LIGHT_GRAY));
        westPanel.add(kitchenWindow);
        return westPanel;
    }

    private JPanel createEastPanel(){
        JPanel eastPanel = new JPanel(new GridLayout(4,2));
        tableWindows = new JTextArea[8];
        for (int i=0;i<8;i++){
            tableWindows[i] = new JTextArea(10,20);
            tableWindows[i].setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.LIGHT_GRAY));
            eastPanel.add(tableWindows[i]);
        }
        return eastPanel;
    }

    public void addStartListener(ActionListener actionListener){
        startButton.addActionListener(actionListener);
    }

    public void disableStartButton(){
        startButton.setEnabled(false);
    }

    public void addStopListener(ActionListener actionListener){
        stopButton.addActionListener(actionListener);
    }

    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel(new GridLayout(1,4));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        searchButton = new JButton("Search Bill");
        writeFileButton = new JButton("Write File");
        southPanel.add(startButton);
        southPanel.add(stopButton);
        southPanel.add(searchButton);
        southPanel.add(writeFileButton);
        return southPanel;
    }

    public synchronized void update(Observable observable,Object object){
        model = (MVCRestaurantModel) observable;
        String message = "LIST OF ORDERS\n";
        message += model.toString();
        kitchenWindow.setText(message);
    }
}
