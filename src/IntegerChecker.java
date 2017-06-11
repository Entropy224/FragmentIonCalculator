/**
 * Created by michael on 6/8/2017.
 */
public class IntegerChecker {

    public static boolean integerChecker(String toTest){
        int whichInt;
        try{
            whichInt = Integer.parseInt(toTest);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }
}
