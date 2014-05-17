package Restrauant;

/**
 * Created by leewin on 14-1-28.
 * main class of program
 */
public class RestaurantMain {
    public static void main(String[] args){
        MVCRestaurantModel model = new MVCRestaurantModel();
        MVCRestaurantView view = new MVCRestaurantView(model);
        MVCRestaurantController controller = new MVCRestaurantController(view,model);
        view.setVisible(true);
    }
}
