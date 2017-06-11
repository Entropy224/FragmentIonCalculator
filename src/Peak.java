import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Michael Stadlmeier on 6/9/2017.
 */
public class Peak {
    private double peakMass;
    private int peakCharge;
    private double peakIntensity;
    private double peakRelIntensity;
    private boolean peakIsBasePeak;
    private int scanNumberAffil;
    DecimalFormat twoDec = new DecimalFormat("0.00");


    //initial constructor for peaks
    //can just create peak with basic information, doesn't know of the other peaks from the spectrum
    public Peak(double massIn, double intensityIn, int chargeIn, int scanNumberIn){
        this.peakMass = massIn;
        this.peakCharge = chargeIn;
        this.peakIntensity = intensityIn;
        this.scanNumberAffil = scanNumberIn;
    }

    //setter for peak properties derived from ArrayList
    private static void peakRelIntSetter(Peak peakIn, double relIntIn){
        peakIn.peakRelIntensity = relIntIn;
    }

    private static void peakBasePeakSetter(Peak peakIn){
        peakIn.peakIsBasePeak = true;
    }



    //this method adds the peakIntensity and base peak information after the initial arrayList was created
    public static void peakPacker(ArrayList<Peak> peaksIn) {
        double highestInt = 0;
        double currentInt;
        double relIntCalc;
        int wrongScanNumber = 0;

        int peakCount = peaksIn.size();
        //check if the peaks all belong to the same spectrum
        for (int a = 0; a < peakCount; a++) {
            if (peaksIn.get(0).scanNumber() != peaksIn.get(a).scanNumber()) {
                peaksIn.remove(a);
                peakCount = peakCount -1;
                wrongScanNumber++;

            }
        }

        peakCount = peaksIn.size();
        for (int i = 0; i < peakCount; i++) {
            currentInt = peaksIn.get(i).intensity();
            if (currentInt >= highestInt) {
                highestInt = currentInt;
            }
        }
        for (int m = 0; m < peakCount; m++) {
            currentInt = peaksIn.get(m).intensity();
            if (currentInt == 0) {
                throw new IllegalArgumentException("Intensity of peak was 0!");
            } else {
                relIntCalc = (currentInt / highestInt) * 100;
                peakRelIntSetter(peaksIn.get(m), relIntCalc);
                if (relIntCalc == 100) {
                    peakBasePeakSetter(peaksIn.get(m));
                }
            }
        }
        System.out.println("");
        System.out.println("Generating Peak list:");
        System.out.println(""+wrongScanNumber+" peak(s) were removed(different scan number).");
        System.out.println("");


    }


    //getter
    public double mass() {
        return this.peakMass;
    }
    public int charge(){
        return this.peakCharge;
    }
    public double intensity(){
        return this.peakIntensity;
    }
    public double relIntensity(){
        return this.peakRelIntensity;
    }
    public boolean basePeak(){
        return this.peakIsBasePeak;
    }
    public int scanNumber(){
        return this.scanNumberAffil;
    }


    public void peakPrinter (){
        System.out.println("Mass: "+this.peakMass);
        System.out.println("Charge: "+this.peakCharge);
        System.out.println("Intensity: "+this.peakIntensity);
        System.out.println("rel. intensity: "+twoDec.format(this.peakRelIntensity));
        System.out.println("Base Peak: "+this.peakIsBasePeak);
        System.out.println("Scan number: "+this.scanNumberAffil);
        System.out.println("");

    }

}
