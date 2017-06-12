import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        DecimalFormat fiveDec = new DecimalFormat("0.00000");
        DecimalFormat twoDec = new DecimalFormat("0.00");

        String filePath = "C:\\Anwendungen\\IntelliJProjects\\FragmentIonCalculator\\Aminoacids_list.csv";
        File csvDoc = new File(filePath);
        ArrayList<AminoAcid> acids = new ArrayList<>();
        acids = CSVReader.aminoAcidParse(csvDoc);

        Peptide testPeptide = new Peptide("HLVDEPQNLIK", acids);

//        System.out.println("Peptide sequence: "+testPeptide.peptideSequence());
//        System.out.println("Peptide length: "+testPeptide.peptideLength());
//        System.out.println("Peptide Mass: "+fiveDec.format(testPeptide.peptideMass()));
//
//        for (int i = 0; i<testPeptide.peptideLength();i++){
//            System.out.print(""+testPeptide.peptideBIons().get(i)[0]+": "+fiveDec.format(Double.parseDouble(testPeptide.peptideBIons().get(i)[1]))+"     ");
//            System.out.println(""+testPeptide.peptideYIons().get(i)[0]+": "+fiveDec.format(Double.parseDouble(testPeptide.peptideYIons().get(i)[1])));
//        }

        System.out.println("");
        System.out.println("");

        ArrayList<String[]> modifications = new ArrayList<>();
        String[] mod01 = new String[2];
        mod01[0] = "K";
        //mod01[1] = "182.10553";
        mod01[1] = "361.18648";
        modifications.add(mod01);

        testPeptide.peptideModifier(modifications);

        String filePathPeak = "C:\\Anwendungen\\IntelliJProjects\\FragmentIonCalculator\\EColi_BSASpikeIn_1to1_12161_mod.csv";
        File peaksDoc = new File (filePathPeak);
        ArrayList<Peak> peaks;
        peaks = CSVReader.peakParse(peaksDoc);
        Peak.peakPacker(peaks);

        ArrayList<String[]> peakMatch;
        peakMatch = PeakCompare.peakFragmentCompare(peaks, testPeptide, 5.00 );

        //PeakCompare.matchPrinter(peakMatch, testPeptide);

        ArrayList<String[]> cSVWriting = new ArrayList<>();
        String[] csvLine1 = new String[4];
        String[] csvLine2 = new String[4];
        csvLine1[0] = "bla";
        csvLine1[1] = "blub";
        csvLine1[2] = "123";
        csvLine1[3] = "567";
        cSVWriting.add(csvLine1);
        csvLine2[0] = "schni";
        csvLine2[1] = "schna";
        csvLine2[2] = "845";
        csvLine2[3] = "schnappi";
        cSVWriting.add(csvLine2);

        CSVWriter.testCSVWrite(cSVWriting);

    }


}
