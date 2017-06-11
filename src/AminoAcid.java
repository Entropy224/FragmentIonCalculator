import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AminoAcid {

    //Definition der Variablen
    private String name;
    private String threeLetter;
    private String oneLetter;
    private Double exactMass;
    private String sumFormula;
    private Double waterLossMass;
    private Boolean hasModification;
    private Double[] isotopicDistribution;

    //Atomare Massen
    private static final Double H_MASS =  1.007825032;
    private static final Double H_PLUS_MASS = 1.00727647;
    private static final Double C_MASS = 12.0;
    private static final Double O_MASS =  15.99491462;
    private static final Double N_MASS = 14.00307400;
    private static final Double S_MASS = 31.97207117;


    //Constructor für die Aminosäuren - ohne Modification, ohne Summenformel
    public AminoAcid(String nameIn, String threeLetterIn, String oneLetterIn, double exactMassIn) {

        this.name = nameIn;
        this.threeLetter = threeLetterIn;
        this.oneLetter = oneLetterIn.toUpperCase();
        this.exactMass = exactMassIn;
        this.waterLossMass = exactMassIn - 2 * H_MASS - O_MASS;
        this.hasModification = false;

    }

    //Constructor für die Aminosäuren - ohne Modifikation, mit Summenformel
    public AminoAcid(String nameIn, String threeLetterIn, String oneLetterIn, String sumFormulaIn) {

        this.name = nameIn;
        this.threeLetter = threeLetterIn;
        this.oneLetter = oneLetterIn.toUpperCase();
        this.exactMass = 0.0;
        this.sumFormula = sumFormulaIn;

        ArrayList<String[]> usableFormula = new ArrayList<>();
        usableFormula = ElementParser.elementParseOut(sumFormulaIn);

        int elementNumber = usableFormula.size();
        double elementMass = 0;
        for (int i = 0; i < elementNumber; i++) {
            char elementChooser = 0;
            String elementString = "";
            elementString = usableFormula.get(i)[0];
            elementChooser = elementString.charAt(0);
            elementMass = AminoAcid.ElementLookUp(elementChooser);
            this.exactMass += elementMass * Integer.parseInt(usableFormula.get(i)[1]);


        }
        this.waterLossMass = this.exactMass - 2 * H_MASS - O_MASS;
        this.hasModification = false;


    }


    //Getter für verschiedene Parameter
    public String aaName() {
        return this.name;
    }

    public String aa3Let() {
        return this.threeLetter;
    }

    public String aa1Let() {
        return this.oneLetter;
    }

    public double aaMass() {
        return this.exactMass;
    }

    public double aaWaterLoss() {
        return this.waterLossMass;
    }

    public Boolean aahasMod() {
        return this.hasModification;
    }


    private static double ElementLookUp (char c){
        double massOut = 0;

        switch(c)
        {
            case 'H':
                massOut = H_MASS;
                break;
            case'C':
                massOut = C_MASS;
                break;
            case'O':
                massOut = O_MASS;
                break;
            case'N':
                massOut = N_MASS;
                break;
            case'S':
                massOut = S_MASS;
                break;
            default:
                throw new NoSuchElementException("Element unknown!");




        }
        return massOut;

    }


}

