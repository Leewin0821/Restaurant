package Restrauant;

/**
 * provide a class to hold all information of a menu
 */
public class Menu implements Comparable<Menu>{
    private String dishName;
    private double price;
    private String category;

    public Menu(String dishName,double price,String category){
        this.dishName=dishName;
        if (price < 0) throw new IllegalArgumentException("Price can not less than 0: "+price);
        this.price=price;
        this.category=category;
    }

    /**
     * provide a method to test the comparability of two menu objects
     * @param other another Menu object that used to compare
     * @return true if the two menu have the same dish name, otherwise false
     */

    public boolean equals(Menu other)
    {
        return other!=null && dishName.equals(other.getDishName());
    }

    /**
     * compare two menu object for the purpose of sorting
     * @param other another Menu object that used to compare
     * @return a negative integer if this dish's name comes before the parameter's in alphabet,
     * zero if they are equal
     * and a positive integer if this comes after the other.
     */

    public int compareTo(Menu other)
     {
         int result = 0 ;
         try{
             if (!other.equals(null)){
             result = dishName.compareTo(other.getDishName());
             }
         }catch (NullPointerException e){
             System.out.println("Compared menu parameter is null: " + e);
             System.exit(1);
         }
         return result;
    }

    public String getDishName(){
        return dishName;
    }

    public double getPrice(){
        return price;
    }

    public String getCategory(){
        return category;
    }

    /**
     * Whole data of one menu
     * @return information about this menu
     */
    public String toString(){
        return dishName+"   "+price+"   "+category;
    }
}
