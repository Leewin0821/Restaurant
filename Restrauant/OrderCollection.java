package Restrauant;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by leewin on 14-1-28.
 * provide a map to combine menu and order
 */
public class OrderCollection {
    private TreeMap<Integer, Order> orderCollection;
    private TreeMap<String,Integer> orderAndPrice = new TreeMap<String, Integer>();
    private Map<String,Double> dishNamePrice = new HashMap<String, Double>();  // store dish name and its price
    private ArrayList<Bill> billList = new ArrayList<Bill>();  // store all bills
    private Map<Integer,ArrayList<Bill>> tableAndBillMap = new HashMap<Integer, ArrayList<Bill>>();  // store tableId and its bills
    private Map<Integer,Double> sortMap = new LinkedHashMap<Integer, Double>();  //store tableId and its total price
    private Map<Integer,String> tableSummaryMap = new HashMap<Integer, String>();  // store tableId and its output information
    private LinkedList<String> draftList = new LinkedList<String>(); // store information of final output
    String pattern = "0.##";	//create output format in a pattern like this
    DecimalFormat df = new DecimalFormat(pattern);

    public OrderCollection(){
        orderCollection = new TreeMap<Integer, Order>();
    }

    /**
     * get order data from order list
     * @param orderList a array list that holds all orders
     */
    public void addOrders(OrderList orderList){
        for(Order order : orderList){
            orderCollection.put(order.getOrderID(),order);
        }
    }

    /**
     * receive dish names and prices from menu set class
     * @param map a hash map contains all dish names and prices
     */
    public void getDishNamePrice(Map<String,Double> map){
        dishNamePrice.putAll(map);
    }

    /**
     * process menu and order data: import dish name from menu set class,
     * count quantity from order list, then put them together as an entry
     * of a tree map. Organise the output format.
     * @return String sentence which display in output file
     */
    public String processFrequencyReport(){
        String message = "FREQUENCY REPORT:\n=========================\n";
        String message2 =  "\nDISHED NOT ORDERED:\n=========================\n";
        try{
        for(String str : dishNamePrice.keySet()){
            orderAndPrice.put(str,0);
        }
        for(Map.Entry<Integer, Order> entry : orderCollection.entrySet()){
            String dishName = entry.getValue().getFoodName();
                Integer origin = orderAndPrice.get(dishName);
                orderAndPrice.put(dishName,origin+entry.getValue().getQuantity());
        }
        for(Map.Entry<String,Integer> dishPair : orderAndPrice.entrySet()){
            if (dishPair.getValue()!=0){
                message += String.format("%-20s",dishPair.getKey())+String.format("%4d",dishPair.getValue())+"\n";
            } else message2 += String.format("%-20s",dishPair.getKey())+"\n";
        }
        message += message2;
        }catch (NullPointerException npe){
            System.out.println("There is null element in input file\n"+npe);
            System.exit(1);
        }
        return message+"\n";
    }

    /**
     * process methods to create essential lists and maps
     */
    public void processSummary(){
        createBillList();
        createTableBillMap();
        createTotalPriceBillMap();
        processTableSummary();
    }

    /**
     * create a bill list with all the bill objects from orderCollection and dishNamePrice
     */
    public void createBillList(){
        try{
        Iterator iterator = orderCollection.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,Order> entry = (Map.Entry<Integer,Order>)iterator.next();
            Integer billTable = entry.getValue().getTableId();
            Order billOrder = entry.getValue();
            Iterator iterator1 = dishNamePrice.entrySet().iterator();
            while (iterator1.hasNext()){
                Map.Entry<String,Double> entry1 = (Map.Entry<String,Double>) iterator1.next();
                String dishName = entry1.getKey();
                Double dishPrice = entry1.getValue();
                if (dishName.equals(billOrder.getFoodName())){
                    Bill bill = new Bill(billTable,dishName,dishPrice,billOrder.getQuantity());
                    billList.add(bill);
                    }
                }
            }
        } catch (ClassCastException cce){
            System.out.println("Unchecked cast in createBillList method.\n"+cce);
        }
    }

    /**
     * create a tableBillMap hold each tableId as key, corresponding bill as value
     */
    public void createTableBillMap(){
        for(Bill bill : billList){
            int tableId = bill.getTableId();
            if (!tableAndBillMap.containsKey(tableId)){
                ArrayList<Bill> firstTableAndBillList = new ArrayList<Bill>();
                firstTableAndBillList.add(bill);
                tableAndBillMap.put(tableId,firstTableAndBillList);
            } else{
                ArrayList<Bill> tableAndBillList = tableAndBillMap.get(tableId);
                tableAndBillList.add(bill);
            }
        }
    }

    /**
     * create a totalPriceBillMap hold each tableId as key ordered by descend sequence,
     * corresponding total price as value
     */
    public void createTotalPriceBillMap(){
        Map<Integer,Double> tableAndTotalPriceMap = new HashMap<Integer, Double>();
        Iterator<Map.Entry<Integer,ArrayList<Bill>>> iterator = tableAndBillMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,ArrayList<Bill>> entry = iterator.next();
            Integer tableId = entry.getKey();
            ArrayList<Bill> billInfo = entry.getValue();
            Double totalPrice = 0.0;
            for(Bill bill : billInfo){
                totalPrice += bill.getToTal();
            }
            tableAndTotalPriceMap.put(tableId,totalPrice);
        }
        ArrayList<Map.Entry<Integer,Double>> entryArrayList = new ArrayList<Map.Entry<Integer, Double>>();
        entryArrayList.addAll(tableAndTotalPriceMap.entrySet());
        Collections.sort(entryArrayList, new MapValueComparator());
        Iterator<Map.Entry<Integer,Double>> iterator1 = entryArrayList.iterator();
        Map.Entry<Integer,Double> tmpEntry;
        while (iterator1.hasNext()){
            tmpEntry = iterator1.next();
            Integer tableId = tmpEntry.getKey();
            Double totalPrice = tmpEntry.getValue();
            sortMap.put(tableId,totalPrice);
        }
    }

    /**
     * get summaries of each table
     * @return standard output information of all bills
     */
    public String getTableSummary(){
        String outputMessage = "TABLE SUMMARY\n=========================\n\n";
        for(String data : draftList){
            outputMessage += data;
        }
        return outputMessage;
    }

    /**
     * organise output format for table summary information with tableAndBillMap,
     * store output message in tableSummaryMap with tableId as key, corresponding whole bill
     * output message as value
     */
    public void processTableSummary(){
        for(Map.Entry<Integer,Double> entry : sortMap.entrySet()){
            Integer tableId = entry.getKey();
            Double totalPrice = entry.getValue();
            NumberFormat percentFormat = NumberFormat.getPercentInstance();
            String message = "TABLE "+tableId+"\n";
            for (Bill bill : tableAndBillMap.get(tableId)){
                message += bill.toString();
            }
            message += "=======================================\nTOTAL FOR THIS TABLE: "+
                    String.format("%16s",df.format(totalPrice))+"\n";
            double calculatedPrice = calculateBill(totalPrice);
            message += "DISCOUNT: "+String.format("%28s",percentFormat.format(1-calculatedPrice/totalPrice))+"\n";
            message += "PAY FOR: "+ String.format("%29s",df.format(calculatedPrice))+"\n\n\n";
            tableSummaryMap.put(tableId,message);
            draftList.add(message);
        }
    }

    /**
     * calculate the money should pay for
     * @param totalPrice total price of each table
     * @return a actual price with discount
     */
    public double calculateBill(double totalPrice){
        double discount;
        if (totalPrice>0 && totalPrice < 10){
            discount = 0.0;
        } else if (totalPrice >= 10 && totalPrice < 20){
            discount = 0.05;
        } else if (totalPrice >= 20 && totalPrice < 30){
            discount = 0.1;
        } else if (totalPrice >= 30 && totalPrice < 50){
            discount = 0.15;
        } else {
            discount = 0.2;
        } return totalPrice*(1-discount);
    }

    /**
     * find table bill by input the table number
     * @param id the input number through GUI
     * @return bill information about corresponding table
     */
    public String findById(int id){
        String info = "";
       try{
           info = tableSummaryMap.get(id);
       }catch (NullPointerException npe){
           System.out.println("This id is not in tableSummaryMap : "+id);
       }
        return info;
    }

    /**
     * process a sale summary for tonight, include the max, min, average and total sales
     * @return summary information of tonight
     */
    public String processSaleSummary(){
        String message = "TONIGHT SALE SUMMARY\n============================================\n";
        LinkedList<Double> valueList = new LinkedList<Double>();
        valueList.addAll(sortMap.values());
        double maxTableSale = getMaxTableSale(valueList);
        double minTableSale = getMinTableSale(valueList);
        double totalTableSale = getTotalTableSale(valueList);
        double averageTableSale = 0;
        try{
            averageTableSale = totalTableSale/valueList.size();
        } catch (ArithmeticException ae){
            System.out.println("Incorrect calculate in processSaleSummary\n"+ae);
        }
        message += String.format("%8s","MAX SALE")+String.format("%10s","MIN SALE")+
                   String.format("%14s","AVERAGE SALE")+String.format("%12s","TOTAL SALE")+"\n";
        message += String.format("%8s",maxTableSale)+String.format("%10s",minTableSale)+
                String.format("%14s",df.format(averageTableSale))+String.format("%12s",df.format(totalTableSale))+"\n";
        return message;
    }


    /**
     * return the max sale number of all sales
     * @param valueList a LinkedList contains all tables' sales
     * @return the max sale number
     */
    public double getMaxTableSale(LinkedList<Double> valueList){
        return valueList.getFirst();
    }

    /**
     * return the min sale number of all sales
     * @param valueList a LinkedList contains all tables' sales
     * @return the min sale number
     */
    public double getMinTableSale(LinkedList<Double> valueList){
        return valueList.getLast();
    }

    /**
     * return total sale number of tonight
     * @param valueList a LinkedList contains all tables' sales
     * @return the total sale number
     */
    public double getTotalTableSale(LinkedList<Double> valueList){
        Double sum = 0.0;
        for(Double value : valueList){
            sum += value;
        }
        return sum;
    }

}
