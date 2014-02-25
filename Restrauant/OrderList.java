package Restrauant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * create an ArrayList of orders by reading input file
 */
public class OrderList implements ProcessLine,Iterable<Order>{
    private ArrayList<Order> orderList;

    public OrderList(){
        orderList = new ArrayList<Order>();
    }

    /**
     * re write the method to get index element of orderList
     * @param num index number
     * @return element at that index
     */
    public Order get(int num){
        Order order = new Order(0,0,"",0);
        try{
            if (orderList.get(num) instanceof Order){
                order = (Order) orderList.get(num);
            }
        } catch (NullPointerException npe){
            System.out.println("Trying get a non order object\n"+npe);
        }
        return order;
    }


    /** read each line of text file, count the number of lines
        @param fileName input file name
     */
    public void readFile(String fileName){
        try{
            File f = new File(fileName);
            Scanner scanner = new Scanner(f);
            int lineNum = 0;
            while (scanner.hasNextLine()){
                String inputLine = scanner.nextLine();
                lineNum++;
                if (inputLine.length()!=0){
                    process(inputLine,lineNum);
                }
            }
        }catch (FileNotFoundException fnfe){
            System.out.println(fileName + " doesn't exist\n" + fnfe);
            System.exit(0);
        }catch (IOException ioe){
            System.out.println("IOException at: " + ioe);
            System.exit(1);
        }
    }

    /**
     * get  data from each line, lineNum is OrderID
     * @param line input line waits to be processed
     * @param lineNum number of this line
     */
    public void process(String line,int lineNum){
        try{
            String parts[] = line.split(";");
            if (parts.length >3){
                throw new InputElementOutOfBoundException(lineNum);
            }
            int tableID = Integer.parseInt(parts[0].trim());
            if (tableID < 0 ){
                throw new NegativeNumberException(tableID);
            }
            String foodName = parts[1];
            for(int index=0;index<foodName.length();index++){
                if (!(foodName.charAt(index)>='a'&&foodName.charAt(index)<='z'||
                      foodName.charAt(index)>='A'&&foodName.charAt(index)<='Z'||
                      foodName.charAt(index)==' ')){
                    throw new NonCharacterException(foodName);
                }
            }
            int quantity = Integer.parseInt(parts[2].trim());
            if (quantity < 0){
                throw new NegativeNumberException(quantity);
            }
            Order order = new Order(lineNum,tableID,foodName,quantity);
            orderList.add(order);
        }catch (NumberFormatException nfe){
            System.out.println("Input order tableID or quantity is not a number at line: "+lineNum+"\n"+nfe);
            System.exit(1);
        }catch (NegativeNumberException nne){
            System.out.println("Input order tableID or quantity is not a positive number at line: "+lineNum+"\n"+nne);
            System.exit(1);
        }catch (InputElementOutOfBoundException ieoob){
            System.out.println("Input order has too many elements at line: "+ieoob);
            System.exit(1);
        }catch (ArrayIndexOutOfBoundsException aiooe){
            System.out.println("Input order have elements out of array bounds at line: "+lineNum+"\n"+aiooe);
            System.exit(1);
        }catch (NonCharacterException nce){
            System.out.println("Input order have non-character elements at line: "+lineNum+"\n"+nce);
            System.exit(1);
        }
    }

    /**
     * provide iterator interface and create a inner class to operate elements
     * @return a order object
     */
    public Iterator<Order> iterator(){
        return new MyIterator();
    }

    class MyIterator implements Iterator<Order>{
        private int index = 0;

        public boolean hasNext(){
            return index != orderList.size();
        }

        public Order next(){
            return orderList.get(index++);
        }

        public void remove(){
            orderList.remove(index);
        }
    }
}
