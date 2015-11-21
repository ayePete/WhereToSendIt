import java.io.*;
import java.util.*;

public class Main {

    private static final int N = 61;
    public static ArrayList<Journal> journals;
    public static final double REVISION_TIME = 30;
    public static final double SCOOP_RATE = 0.001;
    public static final int TOTAL_TIME = 3000;



    public static void main(String[] args) {
        System.out.println("Hello this World!");

    }

    public static double computeC(ArrayList<Journal> journals){
        int q = 1;
        double result = 1 / q;
        double sum = 0;
        for (int j = 1; j <= N; j++) {
            double remainingTimeSum = 0;
            for (int k = 1; k <= j; k++) {
                remainingTimeSum += journals.get(j).getSubToPub() - (j - 1) * REVISION_TIME;
            }
            double resubmissionRiskProduct = 1;

            for (int k = 1; k <= j - 1; k++) {
                resubmissionRiskProduct *= (1 - journals.get(k).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, journals.get(k).getSubToPub()
                        + REVISION_TIME);
            }
            sum += journals.get(j).getAcceptanceRate() * journals.get(j).getExpectedNumOfCitations()
                    * (TOTAL_TIME - remainingTimeSum) * resubmissionRiskProduct;
        }

        result *= sum;
        return result;
    }

    public static double computeR(ArrayList<Journal> journals){
        int q = 1;
        double result = 1 / q;
        double sum = 0;

        for (int j = 1; j <= N; j++) {
            double product = 1;
            double hSum = 0;
            for (int i = 1; i <= j; i++) {
                hSum += journals.get(j).getSubToPub() - (j - 1) * REVISION_TIME;
            }
            int h = TOTAL_TIME - hSum > 0 ? 1 : 0;
            for (int i = 1; i <= j-1; i++) {
                product *= (1 - journals.get(i).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, journals.get(i).getSubToPub()
                        + REVISION_TIME)  * h;
            }
            sum +=  (j+1) * journals.get(j).getAcceptanceRate() * product;
        }
        result *= sum;
        return result;
    }

    public static double computeP(ArrayList<Journal> journals){
        int q = 1;
        double result = 1 / q;
        double sum = 0;

        for (int j = 1; j <= N; j++) {
            double product = 1;
            double hSum = 0;
            for (int i = 1; i <= j; i++) {
                hSum += journals.get(i).getSubToPub() - (j - 1) * REVISION_TIME;
            }
            int h = TOTAL_TIME - hSum > 0 ? 1 : 0;
            for (int i = 1; i <= j-1; i++) {
                product *= (1 - journals.get(i).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, journals.get(i).getSubToPub()
                        + REVISION_TIME)  * h;
            }
            sum +=  (journals.get(j).getSubToPub() + (j - 1) * REVISION_TIME) * journals.get(j).getAcceptanceRate() * product;
        }
        result *= sum;
        return result;
    }

    public static void readData(){
        journals = new ArrayList<>();
        journals.add(new Journal());
        double acceptanceRate;
        double subToPub;
        double expectedNumOfCitations;

        FileReader fr;
        try {
            File f = new File("journal_data.txt");
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while(br.ready()){
                s = br.readLine();
                String[] sHolder = s.split("\\s+");
                acceptanceRate = Double.parseDouble(sHolder[0]);
                subToPub = Double.parseDouble(sHolder[1]);
                expectedNumOfCitations = Double.parseDouble(sHolder[2]);
                Journal j = new Journal(acceptanceRate, expectedNumOfCitations, subToPub);
                journals.add(j);
            }

            f = new File("journal_names.txt");
            br = new BufferedReader(new FileReader(f));
            int i = 1;
            while(br.ready()){
                s = br.readLine();
                journals.get(i).setName(s);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static double coEvaluateJournals(int j, int k){
        double jRank = journals.get(j).getAcceptanceRate() * journals.get(j).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(j).getSubToPub()
                - (1 - journals.get(k).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + journals.get(k).getSubToPub())
                * journals.get(k).getAcceptanceRate() * journals.get(k).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(k).getSubToPub()))
                + ((1 - journals.get(k).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + journals.get(k).getSubToPub())
                * journals.get(j).getAcceptanceRate() * journals.get(j).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(k).getSubToPub() - REVISION_TIME - journals.get(j).getSubToPub()));
        double kRank = journals.get(k).getAcceptanceRate() * journals.get(k).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(k).getSubToPub()
                - (1 - journals.get(j).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + journals.get(j).getSubToPub())
                * journals.get(j).getAcceptanceRate() * journals.get(j).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(j).getSubToPub()))
                + ((1 - journals.get(j).getAcceptanceRate()) * Math.pow(1 - SCOOP_RATE, REVISION_TIME + journals.get(j).getSubToPub())
                * journals.get(j).getAcceptanceRate() * journals.get(k).getExpectedNumOfCitations() * (TOTAL_TIME - journals.get(j).getSubToPub() - REVISION_TIME - journals.get(k).getSubToPub()));

        return jRank >= kRank ? j : k;
    }

}