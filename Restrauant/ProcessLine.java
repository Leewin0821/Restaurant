package Restrauant;

/**
 * provide an interface to implement same method name to different
 * methods of different objects
 */
public interface ProcessLine {
    public void readFile(String fileName);
    public void process(String s);
}
