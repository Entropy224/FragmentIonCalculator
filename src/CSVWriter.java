

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by michael on 6/12/2017.
 */

//this class should write PeakCompare results to .csv File
public class CSVWriter {

    public static void testCSVWrite(ArrayList<String[]> StringsIn) throws FileNotFoundException{
        File file = new File("C:\\Anwendungen\\IntelliJProjects\\FragmentIonCalculator\\test.csv");
        PrintWriter testWriter = new PrintWriter(file);
        StringBuilder testString = new StringBuilder();

        for (String[]row : StringsIn ){
            String sep = "";
            for(String s : row) {
                testString.append(sep);
                sep = ",";
                testString.append(s);
            }
            testString.append('\n');
        }
        testWriter.write(testString.toString());
        testWriter.close();
        System.out.println(".csv File created!");








    }




}
