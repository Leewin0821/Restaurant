package Restrauant;

/**
 * provide a kitchen thread to give orders gradually to kitchen
 */
public class KitchenThread implements Runnable{
    private MVCRestaurantModel model;
    private RestaurantLogger restaurantLogger = RestaurantLogger.getRestaurantLogger();//initialize singleton RestaurantLogger

    public KitchenThread(MVCRestaurantModel model){
        this.model = model;
    }

    /**
     * response to thread call, put orders from OrderList to KitchenList,
     * inform GUI to update.
     */
    public void run(){
        int count = 0;
        try{
            for(;count<model.getOrderListSize();count++){
                if (!model.isStop()){  // check the flag whether should stop or not
                    Thread.sleep(1000);
                    Order order = model.getOrderFromOrderList(count);
                    model.addToKitchenList(order); // put order into KitchenList
                    model.addToBackupList(order); //put order into BackupList
                    model.sendNotifyToObservers();
                    restaurantLogger.getInfo("Order "+order.getOrderID()+" Arrives In Kitchen");//use logger to record event
                }
            }
            model.addOrdersToCollection(); //add orders from BackupList to collection for process
        } catch (InterruptedException ie){
            System.out.println("Kitchen Thread be interrupted at: "+ie);
        }
    }

}
