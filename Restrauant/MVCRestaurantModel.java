package Restrauant;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by leewin on 14-1-28.
 * provide a model to store all data
 * made some commit here
 */
public class MVCRestaurantModel extends Observable{
    private OrderCollection allOrderCollection;
    private OrderList allOrderList; //contains all orders
    private MenuSet allMenuSet;
    private LinkedList<Order> kitchenList;  // contains all the orders displayed in kitchen window
    private HashMap<Integer,LinkedList<Order>> tableMap; //stores orders, the key is tableID, the value are corresponding orders
    private Thread firstTableThread;  // used to deliver orders from kitchen to tables
    private Thread secondTableThread;
    private Thread kitchenThread;  // used to deliver orders to kitchen
    private int count; // count threads run times
    private boolean done; // stop deliver order to tables if true
    private boolean stop; // terminate deliver order to kitchen if true
    private OrderList backupList;
    private BillGUI billGUI;
    private OrderProducer orderProducer;

    public MVCRestaurantModel(){
        allOrderCollection = new OrderCollection();
        allOrderList = new OrderList();
        allMenuSet = new MenuSet();
        kitchenList = new LinkedList<Order>();
        tableMap = new HashMap<Integer, LinkedList<Order>>();// A little cheat, know there are 8 tables before design
        firstTableThread  = new Thread(new TableThread(this));
        secondTableThread = new Thread(new TableThread(this));
        kitchenThread = new Thread(new KitchenThread(this));
        count = 0;
        done = false;
        stop = false;
        backupList = new OrderList();
        orderProducer = new OrderProducer();
        process();  // read input order file when initiation
    }

    /**
     * read data from random order producer, process data for output
     */
    public void process(){
        orderProducer.run(30);
        allOrderList = orderProducer.getOrderList();
        allMenuSet.readFile("Menus.txt");
        allOrderCollection.getDishNamePrice(allMenuSet.getDishNamePrice());
    }

    //////////////////////////////////////////////////////////////////
                 //Below are methods used in threads
    //////////////////////////////////////////////////////////////////


    public int getCount(){
        return count;
    }

    public void incrementCount(){
        count++;
    }

    /**
     * set flag to let TableThread stop
     */
    public void setDone(){
        done = true;
    }

    /**
     * get flag condition
     * @return boolean value of done
     */
    public boolean isDone(){
        return done;
    }

    /**
     * set flag to let KitchenThread stop
     */
    public void setStop(){
        stop = true;
    }

    /**
     * get flag condition
     * @return boolean value of stop
     */
    public boolean isStop(){
        return stop;
    }

    /**
     * pass order list size
     * @return order list size
     */
    public int getOrderListSize(){
        return allOrderList.getSize();
    }

    /**
     * pass order
     * @param index order index of order list
     * @return order with the index
     */
    public Order getOrderFromOrderList(int index){
        return allOrderList.get(index);
    }

    /**
     * add order to kitchen list
     * @param order order be passed
     */
    public void addToKitchenList(Order order){
        kitchenList.add(order);
    }

    /**
     * pass the first order of kitchen list
     * @return the first order
     */
    public Order getFirstOfKitchenList(){
        if (kitchenList.isEmpty()){
            return null;
        }
        return kitchenList.getFirst();
    }

    /**
     * remove the first order of kitchen list
     */
    public void removeFirstOfKitchenList(){
        try{
            kitchenList.removeFirst();
        } catch (NoSuchElementException nsee){
            System.out.println("No remaining element is KitchenList: "+nsee);
        }

    }

    /**
     * check whether kitchen list has not been created
     * @return exist true, otherwise false
     */
    public boolean isKitchenListEmpty(){
        return kitchenList.isEmpty();
    }

    /**
     * pass order number of one table
     * @param tableId the table want to check
     * @return order number or 0
     */
    public int getTableOrderSize(int tableId){
        if (tableMap.get(tableId) == null)
            return 0;
        return tableMap.get(tableId).size();
    }

    /**
     * put new entry to table map
     * @param tableId tableId used as key
     * @param value LinkedList used as value
     */
    public void putPairInTableMap(int tableId,LinkedList<Order> value){
        tableMap.put(tableId, value);
    }

    /**
     * add the order to one table
     * @param tableId table number
     * @param order the order want to be added
     */
    public void addOrderToTableMap(int tableId,Order order){
        tableMap.get(tableId).add(order);
    }

    /**
     * add order to a backupList
     * @param order the order want to be added
     */
    public void addToBackupList(Order order){
        backupList.addOrder(order);
    }

    /**
     * organize order display format to show at kitchen window
     * @return well organised output message
     */
    public String getKitchenOrders(){
        String message = "";
        for(Order order : kitchenList){
            message += String.format("%9s",order.getOrderID());
            message += String.format("%14s",order.getTableId())+"    ";
            message += String.format("%-20s", order.getFoodName());
            message += String.format("%-5s",order.getQuantity())+"\n";
        }
        return message;
    }

    /**
     * organize order display format to show at table windows
     * @param index tableId
     * @return output message for corresponding table
     */
    public String getTableOrders(int index){
        String message = "";
        if (tableMap.get(index) != null){
            for(Order order : tableMap.get(index)){
                message += order.getOrderID()+"  "+order.getFoodName()+" * "+order.getQuantity()+"\n";
            }
            return message;
        } else return ""; // if no order in one table, return nothing
    }

    /**
     * start two thread to simulate deliveries to kitchen and tables
     */
    public void startProcess(){
        kitchenThread.start();
        firstTableThread.start();
        secondTableThread.start();
    }

    //////////////////////////////////////////////////////////////
                 //Below are methods called by buttons
    /////////////////////////////////////////////////////////////

    public void addOrdersToCollection(){
        allOrderCollection.addOrders(backupList);
        allOrderCollection.processSummary();
    }

    /**
     * implement the GUI part, set GUI visible
     */
    public void showGUI(){
        billGUI = new BillGUI(allOrderCollection);
        billGUI.setVisible(true);
    }

    /**
     * write output information to a specific file
     * @param fileName output file name
     */
    public void writeToFile(String fileName){
        FileWriter fileWriter;
        String message = allMenuSet.processMenuInfo()+"\n"+
                         allOrderCollection.processFrequencyReport()+"\n"+
                         allOrderCollection.getTableSummary()+"\n"+
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

    ///////////////////////////////////////////////////////////////////////
            //Below are methods related to observers
    ///////////////////////////////////////////////////////////////////////

    private LinkedList<Observer> registeredObservers = new LinkedList<Observer>(); // create list to hold observers

    /**
     * add observer to registeredObservers list
     * @param obs observer need to be added
     */
    public void registerObserver( Observer obs)
    {
        try{
            registeredObservers.add( obs);
        } catch (NullPointerException npe){
            System.out.println("Null observer when registering Manager: "+npe);
        }

    }

    /**
     * delete a observer from registeredObservers list
     * @param obs observer need to be deleted
     */
    public void removeObserver( Observer obs)
    {
        registeredObservers.remove( obs);
    }

    /**
     * used to notify observer update
     */
    public void notifyObservers()
    {
        if (registeredObservers.size() == 0){
            System.out.println("Manager registered observers list is null");
        } else {
            for( Observer obs : registeredObservers)
                obs.update(this,null);    // set argument as null, don't pass value
        }
    }

    /**
     * bundle three methods up to call at once
     */
    public void sendNotifyToObservers(){
        setChanged();    //set signal indicate there has state changed
        notifyObservers();  //inform observer
        clearChanged();  //clear signal for next inform
    }

}
