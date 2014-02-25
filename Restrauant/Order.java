package Restrauant;

/**
 * provide a class to hold order information
 */
public class Order {
    private int orderID;
    private int tableId;
    private String foodName;
    private int quantity;

    public Order(int orderID,int tableId,String foodName, int quantity){
        if (orderID < 0) throw new IllegalArgumentException("OrderID can not less than 0: "+orderID);
        this.orderID=orderID;
        if (tableId < 0 ) throw new IllegalArgumentException("TableID can not less than 0: "+tableId);
        this.tableId=tableId;
        this.foodName=foodName;
        if (quantity < 0 ) throw new IllegalArgumentException("Quantity can not less than 0: "+quantity);
        this.quantity=quantity;
    }

    public int getOrderID(){return orderID;}
    public int getTableId(){return tableId;}
    public String getFoodName(){return foodName;}
    public int getQuantity(){return quantity;}

    /**
     * Whole data of one order
     * @return information about this order
     */
    public String toString(){
        return orderID+"   "+tableId+"   "+foodName+"   "+quantity+"\n";
    }

    /**
     * provide a method to test the comparability of two order objects
     * @param other another order object that used to compare
     * @return true if the two orders have everything same, otherwise false
     */
    public boolean isEquals(Order other)
    {
        return other!=null && orderID==other.getOrderID() &&
                tableId==other.getTableId() && foodName.equals(other.getFoodName()) &&
                quantity==other.getQuantity();
    }

}
