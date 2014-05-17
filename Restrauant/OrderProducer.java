package Restrauant;

import java.util.Random;

/**
 * Created by leewin on 14-3-21.
 * provide a producer to create orders randomly
 */
public class OrderProducer {
    private OrderList orderList;
    // a little cheat, know the menus before program start
    private String [] menus = {"Smoked Salmon","Chicken Balerno","Roast Beef","Chocolate Cake","Coffee and Mints",
                               "Carrot Soup","Tandoori Chicken","Mango","Gulap jamon","Mixed kebab roll"};
    private int [] randomNumbers;    // store random numbers represent tables and menus

    public OrderProducer(){
        orderList = new OrderList();
    }

    public OrderList getOrderList(){
        return orderList;
    }

    /**
     * create distinct random numbers, then create corresponding tableId and menu
     * @param size the total order number
     */
    public void run(int size){
        //the first position is 0 which is didn't used, +1 in case of out of bound
        randomNumbers = new int[size+1];
        Random random = new Random();
        for (int count=1;count<=size;count++){
            int temp = random.nextInt(80);   // produce a random number between 0-79
            boolean isHave = false;
            for(int index=0;index<randomNumbers.length;index++){
                //check if have the same number before
                if (randomNumbers[index]==temp){
                    isHave = true;
                    break;
                }
            }
            //if have the same number, reduce count, reproduce a random number
            if (isHave){
                count--;
                continue;
            }
            // put each distinct number into array
            randomNumbers[count] = temp;
        }
        for(int index=1;index<randomNumbers.length;index++){
            //the remainder represents the index of menu in menu array
            int menuId = randomNumbers[index]%10;
            //the quotient +1 represents the index of tableId
            int tableId = (randomNumbers[index]-menuId)/10+1;
            int quantity = random.nextInt(4)+1;
            String menu = menus[menuId];
            Order order = new Order(index,tableId,menu,quantity);
            orderList.addOrder(order);
        }
    }
}
