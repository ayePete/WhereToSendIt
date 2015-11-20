import java.io.*;
import java.util.*;

public class Main {

    private static final int N = 5;
    public static ArrayList<Double> acceptanceRates;
    public static ArrayList<Double> subToPub;
    public static ArrayList<Double> expectedNumOfCitations;
    public static ArrayList<String> journalNames;
    public static List<AbstractMap.SimpleEntry<Integer, Double>> sortedVs;
    public static final double REVISION_TIME = 30;
    private static final double SCOOP_RATE = 0.001;
    private static final int TOTAL_TIME = 3000;


    public static void main(String[] args) {
        System.out.println("Hello this World!");
    }

    public static double computeC(){
        int q = 1;
        double result = 1 / q;
        double sum = 0;
        for (int j = 1; j <= N; j++) {
            double remainingTimeSum = 0;
            for (int k = 1; k <= j; k++) {
                remainingTimeSum += subToPub.get(j) - (j - 1) * REVISION_TIME;
            }
            double resubmissionRiskProduct = 1;
            for (int k = 1; k <= j - 1; k++) {
                resubmissionRiskProduct *= (1 - acceptanceRates.get(k)) * Math.pow(1 - SCOOP_RATE, subToPub.get(k)
                        + REVISION_TIME);
            }
            sum += acceptanceRates.get(j) * expectedNumOfCitations.get(j)
                    * (TOTAL_TIME - remainingTimeSum) * resubmissionRiskProduct;
        }

        result *= sum;
        return result;

    }

    public static double computeR(){
        int q = 1;
        double result = 1 / q;
        double sum = 0;

        for (int j = 1; j <= N; j++) {
            double product = 1;
            double hSum = 0;
            for (int i = 1; i <= j; i++) {
                hSum += subToPub.get(j) - (j - 1) * REVISION_TIME;
            }
            int h = TOTAL_TIME - hSum > 0 ? 1 : 0;
            for (int i = 1; i <= j-1; i++) {
                product *= (1 - acceptanceRates.get(i)) * Math.pow(1 - SCOOP_RATE, subToPub.get(i)
                        + REVISION_TIME)  * h;
            }
            sum +=  (j+1) * acceptanceRates.get(j) * product;
        }
        result *= sum;
        return result;
    }

    public static double computeP(){
        int q = 1;
        double result = 1 / q;
        double sum = 0;

        for (int j = 1; j <= N; j++) {
            double product = 1;
            double hSum = 0;
            for (int i = 1; i <= j; i++) {
                hSum += subToPub.get(j) - (j - 1) * REVISION_TIME;
            }
            int h = TOTAL_TIME - hSum > 0 ? 1 : 0;
            for (int i = 1; i <= j-1; i++) {
                product *= (1 - acceptanceRates.get(i)) * Math.pow(1 - SCOOP_RATE, subToPub.get(i)
                        + REVISION_TIME)  * h;
            }
            sum +=  (subToPub.get(j) + (j - 1) * REVISION_TIME) * acceptanceRates.get(j) * product;
        }
        result *= sum;
        return result;
    }

    public static void readData(){
        acceptanceRates = new ArrayList<>();
        acceptanceRates.add(-1.0);
        subToPub = new ArrayList<>();
        subToPub.add(-1.0);
        expectedNumOfCitations = new ArrayList<>();
        expectedNumOfCitations.add(-1.0);
        journalNames = new ArrayList<>();
        journalNames.add("Empty");

        FileReader fr;
        try {
            File f = new File("journal_data.txt");
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while(br.ready()){
                s = br.readLine();
                String[] sHolder = s.split("\\s+");
                acceptanceRates.add(Double.parseDouble(sHolder[0]));
                subToPub.add(Double.parseDouble(sHolder[1]));
                expectedNumOfCitations.add(Double.parseDouble(sHolder[2]));
            }

            f = new File("journal_names.txt");
            br = new BufferedReader(new FileReader(f));
            while(br.ready()){
                s = br.readLine();
                journalNames.add(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return Sorted map of v values with index to journal name as key
     */
    public static List<AbstractMap.SimpleEntry<Integer, Double>> computeVs(){
        sortedVs = new ArrayList<>();
        for (int i = 0; i < journalNames.size(); i++) {
            double num = acceptanceRates.get(i) * expectedNumOfCitations.get(i)*(1 - subToPub.get(i)/ TOTAL_TIME);
            double den = 1 - (1 - subToPub.get(i)/ TOTAL_TIME - REVISION_TIME / TOTAL_TIME)
                    * (1 - acceptanceRates.get(i)) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + subToPub.get(i));
            double v = num/den;
            sortedVs.add(new AbstractMap.SimpleEntry<>(i, v));
        }
        Collections.sort(sortedVs, new Comparator<AbstractMap.SimpleEntry<Integer, Double>>(){

            public int compare(AbstractMap.SimpleEntry<Integer, Double> o1, AbstractMap.SimpleEntry<Integer, Double> o2){
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return sortedVs;
    }

    public static double coEvaluateJournals(int j, int k){
        double result = acceptanceRates.get(j) * expectedNumOfCitations.get(j) * (TOTAL_TIME - subToPub.get(j)
                - (1 - acceptanceRates.get(k)) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + subToPub.get(k))
                * acceptanceRates.get(k) * expectedNumOfCitations.get(k) * (TOTAL_TIME - subToPub.get(k)))
                + ((1 - acceptanceRates.get(k)) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + subToPub.get(k))
                * acceptanceRates.get(j) * expectedNumOfCitations.get(j) * (TOTAL_TIME - subToPub.get(k) - REVISION_TIME - subToPub.get(j)));

        return result;
    }

}
