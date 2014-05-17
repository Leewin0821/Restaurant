package Restrauant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * provide GUI to display and provide contents to interact with model
 */
public class MVCRestaurantView extends JFrame implements Observer{
    private MVCRestaurantModel model;
    private JTextArea kitchenWindow;
    private JTextArea [] tableWindows;
    private JButton startButton;
    private JButton stopButton;
    private JButton searchButton;
    private JButton writeFileButton;
    private Container contentPane = getContentPane();

    public MVCRestaurantView(MVCRestaurantModel model){
        this.model = model;
        model.registerObserver(this);
        setTitle("Restaurant");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(200,200);
        contentPane.add(createSouthPanel(),BorderLayout.SOUTH);
        contentPane.add(createWestPanel(),BorderLayout.WEST);
        contentPane.add(createEastPanel(),BorderLayout.EAST);
        pack();
        this.setVisible(true);
    }

    /**
     * create a panel to display whole orders in kitchen
     * @return a large text area
     */
    public JPanel createWestPanel(){
        JPanel westPanel = new JPanel();
        kitchenWindow = new JTextArea(45,45);
        String kitchenMessage = String.format("%30s\n","KITCHEN ORDERS");
        kitchenMessage += "OrderId   TableId   DishName        Quantity\n";
        kitchenWindow.setText(kitchenMessage);
        kitchenWindow.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.LIGHT_GRAY));
        westPanel.add(kitchenWindow);
        return westPanel;
    }

    /**
     * create a panel to hold 8 small windows displaying orders of each table
     * @return a panel with 8 small windows
     */
    public JPanel createEastPanel(){
        JPanel eastPanel = new JPanel(new GridLayout(4,2));
        tableWindows = new JTextArea[8];
        for (int i=0;i<8;i++){
            tableWindows[i] = new JTextArea(10,20);
            tableWindows[i].setText("TABLE "+(i+1)+"\n\n");
            tableWindows[i].setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.LIGHT_GRAY));
            JScrollPane scrollPane = new JScrollPane(tableWindows[i]);  //make small windows scrollable
            eastPanel.add(scrollPane);
        }
        return eastPanel;
    }

    /**
     * set startButton response to ActionLister in MVCRestaurantController
     * @param actionListener ActionListener created by programmer in MVCRestaurantController
     */
    public void addStartListener(ActionListener actionListener){
        startButton.addActionListener(actionListener);
    }

    /**
     * set startButton disabled after click
     */
    public void disableStartButton(){
        startButton.setEnabled(false);
    }

    /**
     *  set stopButton response to ActionLister in MVCRestaurantController
     * @param actionListener ActionListener created by programmer in MVCRestaurantController
     */
    public void addStopListener(ActionListener actionListener){
        stopButton.addActionListener(actionListener);
    }

    /**
     * set stopButton disabled after click
     */
    public void disableStopButton(){
        stopButton.setEnabled(false);
    }

    /**
     *  set writeFileButton response to ActionLister in MVCRestaurantController
     * @param actionListener ActionListener created by programmer in MVCRestaurantController
     */
    public void addWriteFileListener(ActionListener actionListener){
        writeFileButton.addActionListener(actionListener);
    }

    /**
     * set writeFileButton enabled.
     */
    public void enableWriteFileButton(){
        writeFileButton.setEnabled(true);
    }

    /**
     * set searchButton response to ActionLister in MVCRestaurantController
     * @param actionListener ActionListener created by programmer in MVCRestaurantController
     */
    public void addSearchListener(ActionListener actionListener){
        searchButton.addActionListener(actionListener);
    }

    /**
     * set searchButton enabled.
     */
    public void enableSearchButton(){
        searchButton.setEnabled(true);
    }

    /**
     * create a panel tp hold buttons
     * @return a panel with four buttons
     */
    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel(new GridLayout(1,4));
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        searchButton = new JButton("Search Bill");
        searchButton.setEnabled(false);
        writeFileButton = new JButton("Write File");
        writeFileButton.setEnabled(false);
        southPanel.add(startButton);
        southPanel.add(stopButton);
        southPanel.add(searchButton);
        southPanel.add(writeFileButton);
        return southPanel;
    }

    /**
     * listen the notify from model and refresh GUI
     * @param observable model
     * @param args here is null
     */
    public void update(Observable observable, Object args){
        if (observable instanceof MVCRestaurantModel){
            String kitchenMessage = String.format("%30s\n","KITCHEN ORDERS");
            kitchenMessage += "OrderId   TableId   DishName        Quantity\n";
            kitchenMessage += model.getKitchenOrders() ;
            kitchenWindow.setText(kitchenMessage);
            for (int i=0;i<8;i++){
                String tableMessage = "TABLE "+(i+1)+"\n\n";
                tableMessage += model.getTableOrders(i+1);
                tableWindows[i].setText(tableMessage);
            }
        }
    }
}
