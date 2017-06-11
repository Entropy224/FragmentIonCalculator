import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by micha on 6/7/2017.
 */
public class Peptide {
    private String sequence;
    private int sequenceLength;
    private double exactMass;
    private ArrayList<String[]> bIons;
    private ArrayList<String[]> yIons;
    private char[] aminoAcidsList;
    private double[] aaMass0List;
    private boolean hasModification;


    //Still to implement
    private ArrayList<String[]> composition;
    private String sumFormula;


    //Atomare Massen
    private static final Double H_MASS = 1.007825032;
    private static final Double H_PLUS_MASS = 1.00727647;
    private static final Double C_MASS = 12.0;
    private static final Double O_MASS = 15.99491462;
    private static final Double N_MASS = 14.00307400;
    private static final Double S_MASS = 31.97207117;

    //Constructor for Peptide class: take in Sequence and AminoAcid List
    public Peptide(String sequenceIn, ArrayList<AminoAcid> aaList) {

        //sequence and sequence length can be determined right away
        this.sequence = sequenceIn.toUpperCase();
        this.sequenceLength = sequenceIn.length();

        //get the individual amino acids and positions
        this.aminoAcidsList = new char[this.sequenceLength];
        this.aaMass0List = new double[this.sequenceLength];

        int aaListLength = aaList.size();
        // first for loops through all the amino acids in the sequence
        for (int i = 0; i < this.sequenceLength; i++) {
            this.aminoAcidsList[i] = this.sequence.charAt(i);

            //lookup individual amino acids in amino acid list
            //second for loops through amino acid list
            boolean aaNotFound = true;
            for (int k = 0; k < aaListLength && aaNotFound; k++) {
                aaNotFound = true;
                if (this.aminoAcidsList[i] == aaList.get(k).aa1Let().charAt(0)) {
                    this.aaMass0List[i] = aaList.get(k).aaWaterLoss();
                    //if amino acid was found, the loop can close before going through all 20 possibilities
                    aaNotFound = false;
                }
            }
        }
        //calculate exact peptide mass from waterloss masses
        this.exactMass = 0;
        for (int m = 0; m < this.sequenceLength; m++) {
            //here, invalid AA-Inputs are handled
            if (this.aaMass0List[m] == 0.0) {
                throw new NoSuchElementException("Amino acid unknown: " + this.aminoAcidsList[m] + ". Analysis aborted!");
            }
            this.exactMass += this.aaMass0List[m];

        }
        //for real exact mass, add H2O
        this.exactMass = this.exactMass + 2 * H_MASS + O_MASS;
        this.hasModification = false;
        this.bIons = this.bIonsBuilder();
        this.yIons = this.yIonsBuilder();


    }
    //Builder for the fragment ion series
    private ArrayList<String[]> bIonsBuilder() {
        int bIonNumber;
        String bMass0Str;
        double bMass0 = H_PLUS_MASS;
        ArrayList<String[]> bIonsList = new ArrayList<>();

        for (int i = 0; i < this.sequenceLength; i++) {
            String[] bIon = new String[2];
            bIonNumber = i + 1;

            bIon[0] = "b" + bIonNumber;


            bMass0 += this.aaMass0List[i];
            bMass0Str = "" + bMass0;
            bIon[1] = bMass0Str;
            bIonsList.add(bIon);
        }
        this.bIons = bIonsList;
        return this.bIons;
    }

    private ArrayList<String[]> yIonsBuilder() {
        int yIonNumber;
        String yMass0Str;
        double yMass0 = H_PLUS_MASS + O_MASS + 2 * H_MASS;
        ArrayList<String[]> yIonsList = new ArrayList<>();

        for (int i = this.sequenceLength - 1; i > -1; i--) {
            String[] yIon = new String[2];
            yIonNumber = -i + this.sequenceLength;
            yIon[0] = "y" + yIonNumber;

            yMass0 += this.aaMass0List[i];
            yMass0Str = "" + yMass0;
            yIon[1] = yMass0Str;
            yIonsList.add(yIon);
        }
        this.yIons = yIonsList;
        return this.yIons;

    }

    //peptide modification method
    //modListIn: gives 1.) Number and Mass of Modification or 2.) Amino Acid and Mass of Modification
    //optional: modification of Nterm and CTerm
    public Peptide peptideModifier(ArrayList<String[]> modListIn) {

        if (modListIn.get(0).length != 2) {
            throw new ArrayIndexOutOfBoundsException("Modification list out of bounds!");
        } else {
            int modListSize = modListIn.size();

            //this loop goes through all the modifications in the ArrayList
            for (int i = 0; i < modListSize; i++) {

                //check if modification position is integer (position) or amino acid
                boolean integer = IntegerChecker.integerChecker(modListIn.get(i)[0]);
                //decision: if the String is an integer, continue with modification of position
                if (integer) {
                    //read out position of modification, add modification
                    int modPlace = Integer.parseInt(modListIn.get(i)[0]) - 1;
                    this.aaMass0List[modPlace] += Double.parseDouble(modListIn.get(i)[1]);
                    this.hasModification = true;
                } else {
                    //read out amino acid to be modified
                    modListIn.get(i)[0] = modListIn.get(i)[0].toUpperCase();
                    char modAA = modListIn.get(i)[0].charAt(0);
                    //look through whole peptide sequence
                    for (int m = 0; m < this.sequenceLength; m++) {
                        // if modified AA is same as sequence, modify aaMass0List
                        if (modAA == this.aminoAcidsList[m]) {
                            this.aaMass0List[m] += Double.parseDouble(modListIn.get(i)[1]);
                            this.hasModification = true;
                        }
                    }
                }
            }

        }


        //add water and all modified AA0-masses to get exact mass
        this.exactMass = 2 * H_MASS + O_MASS;
        for (int k = 0; k < this.sequenceLength; k++) {
            this.exactMass += this.aaMass0List[k];
        }
        this.bIons = this.bIonsBuilder();
        this.yIons = this.yIonsBuilder();


        return this;
    }


    //getter for peptide parameter
    public String peptideSequence() {
        return this.sequence;
    }

    public int peptideLength() {
        return this.sequenceLength;
    }

    public boolean peptideHasMod() {return this.hasModification;}

    public double peptideMass() {
        return this.exactMass;
    }

    //getter for b ion series
    public ArrayList<String[]> peptideBIons() {
        return this.bIons;
    }

    //getter for y ion series
    public ArrayList<String[]> peptideYIons() {
        return this.yIons;
//    }
    }
}