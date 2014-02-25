package Restrauant;

/**
 * Created by leewin on 14-2-21.
 * non-character string will be detected and throw this exception
 */
public class NonCharacterException extends Exception{
    public NonCharacterException(String str){
        super(str+" contains non-character elements.");
    }
}
