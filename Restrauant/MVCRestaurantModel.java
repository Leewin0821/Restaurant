package Restrauant;

import java.io.*;
import java.util.*;

/**
 * Created by leewin on 14-3-11.
 */
public class MVCRestaurantModel extends Observable implements Runnable{
    private ArrayList<Order> model;

    public MVCRestaurantModel(){
        model = new ArrayList<Order>();
    }

    public String toString(){
        String message = "";
        for(Order order : model){
            message += order.toString();
        }
        return message;
    }

    public void setInterrupted(){
        Thread.currentThread().interrupt();
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
        try{
            File file = new File("Orders.txt");
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            String inputLine;
            while (scanner.hasNextLine()){
                Thread.sleep(1000);
                inputLine = scanner.nextLine();
                lineNum++;
                ReadThread readThread = new ReadThread(inputLine,lineNum);
                readThread.start();
            }
        } catch (FileNotFoundException fnfe){
            System.out.println("Input Orders file not found at: "+fnfe);
            System.exit(0);
        } catch (InterruptedException ie){
            System.out.println("Model thread be interrupted at: "+ie);
        }
        }
    }

    private class ReadThread extends Thread{
        private String inputLine;
        private int lineNum;

        public ReadThread(String inputLine,int lineNum){
            this.inputLine = inputLine;
            this.lineNum = lineNum;
        }

        public void run(){
            try{
                String parts[] = inputLine.split(";");
                if (parts.length >3){
                    throw new InputElementOutOfBoundException(lineNum);
                }
                int tableID = Integer.parseInt(parts[0].trim());
                if (tableID < 0 ){
                    throw new NegativeNumberException(tableID);
                }
                if (parts[1].equals("")){
                    throw new NullPointerException("Null element at: "+lineNum);
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
                model.add(order);
                setChanged();
                notifyObservers();
                clearChanged();
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
    }

    private LinkedList<Observer> registeredObservers = new LinkedList<Observer>();

    public void registerObserver( Observer obs)
    {
        registeredObservers.add( obs);
    }

    public void removeObserver( Observer obs)
    {
        registeredObservers.remove( obs);
    }

    public void notifyObservers()
    {
        Object args = null;
        for( Observer obs : registeredObservers)
            obs.update(this,args);
    }

}
