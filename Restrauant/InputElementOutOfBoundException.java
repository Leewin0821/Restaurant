package Restrauant;

/**
 * catch the exception of too many elements in a input line
 */
public class InputElementOutOfBoundException extends Exception {
    public InputElementOutOfBoundException(int lineNum){
        super("Too many elements at line: "+lineNum);
    }
}
