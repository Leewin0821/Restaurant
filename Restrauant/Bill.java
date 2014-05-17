package Restrauant;

import java.text.DecimalFormat;

/**
 * Created by leewin on 14-2-9.
 * provide a class hold all the information of a bill
 */
public class Bill {
    private int tableId;
    private String foodName;
    private double price;
    private int quantity;
    String pattern = "0.##";	//create output format in a pattern like this
    DecimalFormat df = new DecimalFormat(pattern);

    public Bill(int tableId,String foodName,double price,int quantity){
        this.tableId = tableId;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getTableId(){ return tableId;}

    public String getFoodName(){
        return foodName;
    }

    public double getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getToTal(){
        return price*quantity;
    }

    public String toString(){
        String message = String.format("%-20s",foodName)+
                         String.format("%3s",quantity)+" * "+
                         String.format("%4s",price)+" = "+
                         String.format("%5s",df.format(getToTal()));
        return message+"\n";
    }
}
