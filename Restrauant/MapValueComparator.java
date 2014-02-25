package Restrauant;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by leewin on 14-2-11.
 * provide a comparator that can compare two map entry objects with a descending order
 */
public class MapValueComparator implements Comparator<Map.Entry<Integer,Double>> {
    public int compare(Map.Entry<Integer,Double> mv1, Map.Entry<Integer,Double> mv2){
        if (mv1.getValue() > mv2.getValue()){
            return -1;
        } else if (mv1.getValue() < mv2.getValue()){
            return 1;
        } else {
            return 0;
        }
    }
}