import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

public class Main {

    private int revisionTime;
    private static ArrayList<Double> acceptanceRates;
    private static ArrayList<Double> subToPub;
    private static ArrayList<Double> expectedNumOfCitations;
    private static ArrayList<String> journalNames;
    private static double scoopRate;
    private static int totalTime;


    public static void main(String[] args) {
        System.out.println("Hello this World!");
    }

    public static double computeC(){
        double result = 0;

    }

    private static void readData(){
        acceptanceRates = new ArrayList<>();
        subToPub = new ArrayList<>();
        expectedNumOfCitations = new ArrayList<>();
        journalNames = new ArrayList<>();

        FileReader fr;
        try {
            File f = new File("journal_data.txt");
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while(!(s = br.readLine()).isEmpty()){
                String[] sHolder = s.split("\\s+");
                acceptanceRates.add(Double.parseDouble(sHolder[0]));
                subToPub.add(Double.parseDouble(sHolder[1]));
                expectedNumOfCitations.add(Double.parseDouble(sHolder[2]));
            }

            f = new File("journal_names.txt");
            br = new BufferedReader(new FileReader(f));
            while(!(s = br.readLine()).isEmpty()){
                journalNames.add(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
