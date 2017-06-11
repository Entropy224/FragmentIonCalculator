import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Einfache Klasse fuer die Eingabe von Strings und ganzen Zahlen von der
 * Tastatur.
 *
 */
public class Input {

    /**
     * Liest eine Zeile von der Standardeingabe (üblicherweise die Tastatur) und
     * gibt diese als {@link String} zurück.
     *
     * <p>
     * Beispiel: {@code String s = Input.readString(); }
     *
     * @return Die eingelesene Zeile.
     */
    public static String readString() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen von der Standardeingabe");
            e.printStackTrace();
            System.exit(-1);
        }
        return line;
    }

    /**
     * Liest eine Zeile von der Standardeingabe (üblicherweise die Tastatur),
     * interpretiert diese als ganze Zahl und gibt sie als int zurück.
     *
     * <p>
     * Ist die Eingabe keine Zahl, oder ist sie zu groß, um als int gespeichert
     * zu werden, so wird eine Warnung ausgegeben und 0 zurückgegeben.
     *
     * <p>
     * Beispiel: {@code int i = Input.readInt(); }
     *
     * @return Die eingelesene Zeile als int-Wert oder 0, wenn die Eingabe keine
     *         ganze Zahl ist, die als int-Wert repräsentiert werden kann.
     */
    public static int readInt() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int i = 0;
        try {
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen von der Standardeingabe");
            e.printStackTrace();
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Fehler: Die Eingabe ist kein int-Wert!\n"
                    + "Es wird weitergemacht als wäre die Eingabe 0 gewesen.");
            i = 0;
        }
        return i;
    }
}
