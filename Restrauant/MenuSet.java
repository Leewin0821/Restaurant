package Restrauant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * provide a set to hold menus with a ascending sort order
 */
public class MenuSet implements ProcessLine {
    private TreeSet<Menu> menuSet;
    private Map<String,Double> dishNamePrice = new HashMap<String, Double>();

    public MenuSet(){
        menuSet = new TreeSet<Menu>();
    }

    public void add(Menu menu){
        Menu menu1 = new Menu("",0,"");
        try{
            if (menu instanceof Menu){
                menu1 = (Menu) menu;
            }
        } catch (ClassCastException cce){
            System.out.println("Wrong usage of menu object\n"+cce);
        }
        menuSet.add(menu1);
    }

    public int getSize(){
        return menuSet.size();
    }

    /** read each line of text file, count the number of lines
     @param fileName input file name
     */
    public void readFile(String fileName){
        try{
            File f = new File(fileName);
            Scanner scanner = new Scanner(f);
            int lineNum = 1;
            while (scanner.hasNextLine()){
                String inputLine = scanner.nextLine();
                lineNum++;
                if (inputLine.length()!=0){
                    process(inputLine,lineNum);
                }
            }
        }catch (FileNotFoundException fnfe){
            System.out.println(fileName+" doesn't exist\n"+fnfe);
            System.exit(0);
        }catch (IOException ioe){
            System.out.println("IOException at: "+ioe);
            System.exit(1);
        }
    }

    /**
     * get data from each line
     * @param line the line of input data file
     * @param lineNum the number of input line
     */
    public void process(String line,int lineNum){
        try{
            String parts[] = line.split(";");
            if (parts.length >3){
                throw new InputElementOutOfBoundException(lineNum);
            }
            String dishName = parts[0];
            for(int index=0;index<dishName.length();index++){
                if (!(dishName.charAt(index)>='a'&&dishName.charAt(index)<='z'||
                        dishName.charAt(index)>='A'&&dishName.charAt(index)<='Z'||
                        dishName.charAt(index)==' ')){
                    throw new NonCharacterException(dishName);
                }
            }
            double price = Double.parseDouble(parts[1].trim());
            if (price < 0 ){
                throw new NegativeDoubleException(price);
            }
            String category = parts[2];
            Menu menu = new Menu(dishName,price,category);
            menuSet.add(menu);
        }catch (NumberFormatException nfe){
            System.out.println("Input menu price is not a number at line: "+lineNum+"\n"+nfe);
            System.exit(1);
        }catch (NegativeDoubleException nne){
            System.out.println("Input menu price is not a positive number at line: "+lineNum+"\n"+nne);
            System.exit(1);
        }catch (InputElementOutOfBoundException ieoobe){
            System.out.println("Input menu file has too many elements at line: "+ieoobe);
            System.exit(1);
        }catch (ArrayIndexOutOfBoundsException aiooe){
            System.out.println("Input menu have elements out of array bounds at line: "+lineNum+"\n"+aiooe);
            System.exit(1);
        }catch (NonCharacterException nce){
            System.out.println("Input menu have non-character elements at line: "+lineNum+"\n"+nce);
            System.exit(1);
        }
    }

    /**
     * return menu information divided by category and ordered by alphabet
     */
    public String processMenuInfo(){
        String message_Starter = "MENU\n=========================\nSTARTER\n";
        String message_Main = "MAIN\n";
        String message_Dessert = "DESSERT\n";
        String message_Drinks = "DRINKS\n";
        for(Menu menu : menuSet){
            String message = String.format("%-20s",menu.getDishName())+String.format("%4s",menu.getPrice())+"\n";
            switch (MenuCategory.toMenuCategory(menu.getCategory().toUpperCase())){
                case STARTER : message_Starter += message;
                    break;
                case MAIN : message_Main += message;
                    break;
                case DESSERT : message_Dessert += message;
                    break;
                case DRINKS : message_Drinks += message;
                    break;
                default : System.out.println("enu category wrong: "+menu.getDishName());
                    break;
            }
        }
        return message_Starter+"\n"+message_Main+"\n"+message_Dessert+"\n"+message_Drinks+"\n";
    }

    /**
     * provide a method to pass a hash map which contains all dish names and
     * price to other classes
     * @return a hash map contains dish names and prices
     */
    public Map<String,Double> getDishNamePrice(){
        for (Menu menu : menuSet){
            dishNamePrice.put(menu.getDishName(),menu.getPrice());
        }
        return dishNamePrice;
    }

}
