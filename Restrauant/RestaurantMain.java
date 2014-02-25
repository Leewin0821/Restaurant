package Restrauant;

/**
 * Created by leewin on 14-1-28.
 * main class of program
 */
public class RestaurantMain {
    public static void main(String[] args){
        RestaurantManager manager = new RestaurantManager();
        manager.run();
        manager.showGUI();
    }
}
