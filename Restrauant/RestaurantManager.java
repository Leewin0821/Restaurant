package Restrauant;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by leewin on 14-1-28.
 * provide a class to manage activities of different objects
 */
public class RestaurantManager {
    private OrderCollection allOrderCollection;
    private OrderList allOrderList;
    private MenuSet allMenuSet;

    public RestaurantManager(){
        allOrderCollection = new OrderCollection();
        allOrderList = new OrderList();
        allMenuSet = new MenuSet();
    }

    /**
     * read data from input file, process data, then output data to a text file
     */
    public void run(){
        allOrderList.readFile("Orders.txt");
        allMenuSet.readFile("Menus.txt");
        allOrderCollection.addOrders(allOrderList);
        allOrderCollection.getDishNamePrice(allMenuSet.getDishNamePrice());
        allOrderCollection.processSummary();
        writeToFile("Output.txt");
    }

    /**
     * implement the GUI part, set GUI visiable
     */
    public void showGUI(){
        BillGUI gui = new BillGUI(allOrderCollection);
        gui.setVisible(true);
    }

    /**
     * write output information to a specific file
     * @param fileName output file name
     */
    public void writeToFile(String fileName){
        FileWriter fileWriter;
        String message = allMenuSet.processMenuInfo()+"\n"+
                         allOrderCollection.processFrequencyReport()+"\n"+
                         allOrderCollection.processTableSummary()+"\n"+
                         allOrderCollection.processSaleSummary()+"\n";
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(message);
            fileWriter.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
