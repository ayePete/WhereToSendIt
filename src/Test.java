import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Peter on 11/18/2015.
 */
public class Test {
    public static void main(String[] args){
        Main.readData();
        ArrayList<Journal> journals = (ArrayList<Journal>) Main.journals.clone();
        System.out.println(journals);
        System.out.println(Main.computeC(journals));
        Collections.sort(journals);
        System.out.println(journals);
    }
}
