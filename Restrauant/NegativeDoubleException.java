package Restrauant;

/**
 * negative double number exception
 */
public class NegativeDoubleException extends Exception {
    public NegativeDoubleException(double num){
        super(num +" is not a positive double number.");
    }
}
