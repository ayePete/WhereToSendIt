/**
 * Created by Peter on 11/18/2015.
 */
public class Test {
    public static void main(String[] args){
        Main.readData();
        System.out.println(Main.journalNames);
        System.out.println(Main.acceptanceRates);
        System.out.println(Main.expectedNumOfCitations);
        System.out.println(Main.subToPub);
        System.out.println(Main.computeVs());
        System.out.println(Main.computeC());
    }
}
