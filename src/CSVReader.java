import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

//Tasks of CSVReader
// 1.) supplied with .csv
// 2.) read out Line per Line Strings
// 3.) split Strings into individual parameters
// 4.) if necessary, convert parameter types
// 5.) give specific values for entries in the .csv file

public class CSVReader {

    public static ArrayList<AminoAcid> aminoAcidParse(File file){

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch(FileNotFoundException e) {
            System.out.println("Could not read given file - " + file.getAbsolutePath());
            return null;
        }
        ArrayList<AminoAcid> acids = new ArrayList<>();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split(",");
            if(fields.length < 4)
                continue;

            try {
                acids.add(new AminoAcid(fields[0], fields[1], fields[2], fields[3]));
            } catch(NumberFormatException e) {
                System.out.println("Invalid format : " + line);
                continue;
            }
        }
        scanner.close();
        return acids;

    }

    public static ArrayList<Peak> peakParse(File file){
        Scanner scanner = null;
        try{
            scanner = new Scanner(file);
        } catch (FileNotFoundException e){
            System.out.println("Could not read given file - " + file.getAbsolutePath());
            return null;
        }
        ArrayList<Peak> peaks = new ArrayList<>();

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] values = line.split(",");
            if (values.length < 4)
                continue;
            try {
                double massIn = Double.parseDouble(values[0]);
                double intIn = Double.parseDouble(values[1]);
                int chargeIn = Integer.parseInt((values[2]));
                int scanNumberIn = Integer.parseInt(values[3]);
                peaks.add(new Peak(massIn, intIn, chargeIn, scanNumberIn));
            }

            catch (NumberFormatException e){
                System.out.println("Invalid format: "+line);
                continue;
            }

        }
        scanner.close();
        return peaks;
    }


}
