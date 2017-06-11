import com.sun.istack.internal.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Michael Stadlmeier on 6/10/2017.
 */
public class PeakCompare {


    public static ArrayList<String[]> peakFragmentCompare(ArrayList<Peak> peakListIn, Peptide peptideIn, double ppmAccuracy) {
        DecimalFormat twoDec = new DecimalFormat("0.00");
        DecimalFormat fiveDec = new DecimalFormat("0.00000");


        //matchedIons should give: Ion series matched to, calculated mass, real mass of peak found, charge of peak, ppm Deviation
        ArrayList<String[]> matchedIons = new ArrayList<>();
        //matchedIons.get[0] should give information about Peptide Sequence and ppm accuracy to yield better PrintOut capabilities
        String[] generalInfo = new String[3];
        generalInfo[0] = peptideIn.peptideSequence();// peptide sequence
        generalInfo[1] = "" + ppmAccuracy; // mass accuracy
        generalInfo[2] = "" + peakListIn.get(0).scanNumber(); // Scan Number
        matchedIons.add(generalInfo);


        ArrayList<String[]> bIonsExtended = new ArrayList<>();

        ArrayList<String[]> yIonsExtended = new ArrayList<>();
        int numberOfPeaks = peakListIn.size();

        for(String[] ions : peptideIn.peptideBIons())
            bIonsExtended.add(getIonProperties(ions, ppmAccuracy));
        for(String[] ions : peptideIn.peptideYIons())
            yIonsExtended.add(getIonProperties(ions, ppmAccuracy));

        //handle error if something in the Peptide or DeviationCalc methods goes wrong (and somehow isn't handled there)
        if (bIonsExtended.size() != yIonsExtended.size())
            throw new IllegalArgumentException("y- and b-ion lists aren't the same size!");
        //loop through the complete ion series
        for (int a = 0; a < bIonsExtended.size(); a++) {
            double bLowerMass = Double.parseDouble(bIonsExtended.get(a)[2]);
            double yLowerMass = Double.parseDouble(yIonsExtended.get(a)[2]);
            double bUpperMass = Double.parseDouble(bIonsExtended.get(a)[3]);
            double yUpperMass = Double.parseDouble(yIonsExtended.get(a)[3]);

            //2nd loop: go through all the peaks
            for (int b = 0; b < numberOfPeaks; b++) {
                double peakMass = peakListIn.get(b).mass();
                if (bLowerMass <= peakMass && peakMass <= bUpperMass) {
                    String[] matchedIon = new String[6];
                    matchedIon[0] = bIonsExtended.get(a)[0];
                    matchedIon[1] = bIonsExtended.get(a)[1];
                    matchedIon[2] = "" + peakMass;
                    matchedIon[3] = "" + peakListIn.get(b).relIntensity();
                    matchedIon[4] = "" + peakListIn.get(b).charge();
                    double massDev = DeviationCalc.ppmDeviationCalc(Double.parseDouble(bIonsExtended.get(a)[1]), peakMass);
                    matchedIon[5] = "" + twoDec.format(massDev);
                    matchedIons.add(matchedIon);
                }
                if (yLowerMass <= peakMass && peakMass <= yUpperMass) {
                    String[] matchedIon = new String[6];
                    matchedIon[0] = yIonsExtended.get(a)[0];
                    matchedIon[1] = yIonsExtended.get(a)[1];
                    matchedIon[2] = "" + peakMass;
                    matchedIon[3] = "" + peakListIn.get(b).relIntensity();
                    matchedIon[4] = "" + peakListIn.get(b).charge();
                    double massDev = DeviationCalc.ppmDeviationCalc(Double.parseDouble(yIonsExtended.get(a)[1]), peakMass);
                    matchedIon[5] = "" + twoDec.format(massDev);
                    matchedIons.add(matchedIon);
                }

            }

        }

        return matchedIons;
    }


    public static void matchPrinter(ArrayList<String[]> matchedIonsIn, Peptide peptideIn) {
        DecimalFormat twoDec = new DecimalFormat("0.00");
        DecimalFormat fiveDec = new DecimalFormat("0.00000");

        int matchSize = matchedIonsIn.size();
        int bIonsIdentified = 0;
        int yIonsIdentified = 0;
        int fragmentIonPossibilities = peptideIn.peptideLength() * 2;
        // first object of the matchedIonsIn-List hast general information
        System.out.println("***Results of spectral Analysis***");
        System.out.println("");
        System.out.println("Peptide sequence: " + peptideIn.peptideSequence());
        System.out.println("Modified peptide: " + peptideIn.peptideHasMod());
        System.out.println("Exact mass [M]: " + fiveDec.format(peptideIn.peptideMass()) + " Da");
        System.out.println("Analyzed spectrum number: " + matchedIonsIn.get(0)[2]);
        System.out.println("Allowed mass deviation: " + twoDec.format(Double.parseDouble(matchedIonsIn.get(0)[1])) + " ppm");
        System.out.println("");
        System.out.println("Fragment ions:");
        System.out.println("");
        for (int i = 0; i < peptideIn.peptideLength(); i++) {
            System.out.print("" + peptideIn.peptideBIons().get(i)[0] + ": " + fiveDec.format(Double.parseDouble(peptideIn.peptideBIons().get(i)[1])) + "     ");
            System.out.println("" + peptideIn.peptideYIons().get(i)[0] + ": " + fiveDec.format(Double.parseDouble(peptideIn.peptideYIons().get(i)[1])));
        }
        System.out.println("");
        System.out.println("");

        //split output in b- and y-ions
        System.out.println("Matched b-ions:");

        //i=1 because 1st element is general information
        for (int i = 1; i < matchSize; i++) {
            if (matchedIonsIn.get(i)[0].charAt(0) == 'b') {
                System.out.println("");
                System.out.println("" + matchedIonsIn.get(i)[0] + " ion");
                System.out.println("calculated mass: " + fiveDec.format(Double.parseDouble(matchedIonsIn.get(i)[1])));
                System.out.println("measured mass:  " + matchedIonsIn.get(i)[2]);
                System.out.println("relative intensity: " + twoDec.format(Double.parseDouble(matchedIonsIn.get(i)[3])) + "%");
                System.out.println("charge: " + matchedIonsIn.get(i)[4]);
                System.out.println("mass deviation: " + matchedIonsIn.get(i)[5] + " ppm");
                System.out.println("");
                bIonsIdentified++;
            }
        }

        System.out.println("Matched y-ions:");


        for (int i = 1; i < matchSize; i++) {
            if (matchedIonsIn.get(i)[0].charAt(0) == 'y') {
                System.out.println("");
                System.out.println("" + matchedIonsIn.get(i)[0] + " ion");
                System.out.println("calculated mass: " + fiveDec.format(Double.parseDouble(matchedIonsIn.get(i)[1])));
                System.out.println("measured mass:  " + matchedIonsIn.get(i)[2]);
                System.out.println("relative intensity: " + twoDec.format(Double.parseDouble(matchedIonsIn.get(i)[3])) + "%");
                System.out.println("charge: " + matchedIonsIn.get(i)[4]);
                System.out.println("mass deviation: " + matchedIonsIn.get(i)[5] + " ppm");
                System.out.println("");
                yIonsIdentified++;
            }
        }
        //calculating some ion characteristics
        double percentMatched = ((double) bIonsIdentified + (double) yIonsIdentified) / (double) fragmentIonPossibilities * 100;
        double summedIntensity=0;
        double summedDeviation=0;
        for (int i=1; i<matchSize;i++)
        {
            summedIntensity+= Double.parseDouble(matchedIonsIn.get(i)[3]);
            summedDeviation+= Double.parseDouble(matchedIonsIn.get(i)[5]);
        }

        double averageIntensity = summedIntensity/(double)(matchSize-1);
        double averageDeviation = summedDeviation/(double)(matchSize-1);

        System.out.println("");
        System.out.println("Matched b-ions: " + bIonsIdentified);
        System.out.println("Matched y-ions: " + yIonsIdentified);
        System.out.println("Possible b- and y-ions: " + fragmentIonPossibilities);
        System.out.println("");
        System.out.println("Matching rate: " + twoDec.format(percentMatched) + "%");
        System.out.println("Average fragment intensity: " + twoDec.format(averageIntensity) + "%");
        System.out.println("Average mass deviation: " + twoDec.format(averageDeviation) + " ppm");
    }

    private static String[] getIonProperties(@NotNull String[] ions, double accuracy) {
        String[] ionProperties = new String[4];
        ionProperties[0] = ions[0];
        ionProperties[1] = ions[1];
        double[] ppmDev = DeviationCalc.ppmRangeCalc(accuracy, Double.parseDouble(ionProperties[1]));
        ionProperties[2] = String.valueOf(ppmDev[0]);
        ionProperties[3] = String.valueOf(ppmDev[1]);

        return ionProperties;
    }


}
