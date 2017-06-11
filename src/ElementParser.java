import java.util.ArrayList;

/**
 * Created by michael on 6/6/2017.
 */

public class ElementParser {
    public static ArrayList<String[]> elementParseOut(String in) {
        char element = 0;
        String number = "";
        ArrayList<String[]> elementList = new ArrayList<>();
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) >= 'A' && in.charAt(i) < 'a') {
                if (element != 0) {
                    String[] elementNumber = new String[2];
                    elementNumber[0] = ""+element;
                    elementNumber[1] = number.isEmpty() ? "1" : number;
                    elementList.add(elementNumber);


                }

                number = "";
                element = in.charAt(i);
            } else
                number += in.charAt(i);

        }
        String[] elementNumber = new String[2];
        elementNumber[0] = ""+element;
        elementNumber[1] = number.isEmpty() ? "1" : number;
        elementList.add(elementNumber);
        return elementList;
    }


}
