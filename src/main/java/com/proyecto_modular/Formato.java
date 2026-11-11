import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utilidad transversal para formatear valores monetarios en pesos colombianos (COP).
 */
public class Formato {
    private static final DecimalFormatSymbols SIMBOLOS =
            new DecimalFormatSymbols(new Locale("es", "CO"));

    public static String pesos(double valor) {
        return new DecimalFormat("#,##0", SIMBOLOS).format(valor);
    }
}
