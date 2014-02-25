package Restrauant;

/**
 * Created by leewin on 14-2-21.
 * exception to be thrown when number is negative
 */
public class NegativeNumberException extends Exception {
    public NegativeNumberException(int num){
        super(num+" is not a positive number.");
    }

}
