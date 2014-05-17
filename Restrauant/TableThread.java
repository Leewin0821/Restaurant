package Restrauant;

import java.util.LinkedList;

/**
 * Created by leewin on 14-3-19.
 * provide a table thread to pass orders gradually from kitchen to tables
 */
public class TableThread  implements Runnable{
    private MVCRestaurantModel model;
    private RestaurantLogger restaurantLogger = RestaurantLogger.getRestaurantLogger();//initialize singleton RestaurantLogger

    public TableThread(MVCRestaurantModel model){
        this.model = model;
    }

    /**
     * response to thread call, put orders from KitchenList to TableMap,
     * inform GUI to update.
     */
    public synchronized void run(){
        long timer = (long) (3000*Math.random())+2000;   // generate a random number from 2000-5000
        try{
        Thread.sleep(timer);  // sleep a random time before start process
        }catch (InterruptedException ie){
            System.out.println("Thread be interrupted at startProcess: "+ie);
        }
        //check the flag whether should stop or not
        while (!model.isDone()){
            try{
                // thread sleep 2000ms then begin work
                Thread.sleep(2000);
                // always get the first order in KitchenList
                Order order = model.getFirstOfKitchenList();
                if (order == null) break;
                //then remove this order from KitchenList
                model.removeFirstOfKitchenList();
                int tableID = order.getTableId();
                //check whether it's first order in one table
                if (model.getTableOrderSize(tableID) == 0){
                    //if so, create a LinkedList to store orders
                    LinkedList<Order> list = new LinkedList<Order>();
                    list.add(order);
                    model.putPairInTableMap(tableID,list);
                 // if not, add order to table
                } else model.addOrderToTableMap(tableID,order);
                //inform GUI to update
                model.sendNotifyToObservers();
                //use logger to record event
                restaurantLogger.getInfo("Move Order " + order.getOrderID() + " To Table " + tableID);
                //processed one order, counter + 1
                model.incrementCount();
                // if finished all the orders, set flag to stop thread
                if (model.getCount()==model.getOrderListSize()){
                    model.setDone();
                // if stopButton was clicked, finish remaining orders then set flag to stop thread
                } else if (model.isStop()&&model.isKitchenListEmpty()){
                    model.setDone();
                }
            } catch (InterruptedException ie){
                System.out.println("TableThread be interrupted at: "+ie);
            }
        }
        restaurantLogger.getInfo("Delivered All Orders");
    }
}
