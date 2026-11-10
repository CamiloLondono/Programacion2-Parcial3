/**
 * PATRÓN CHAIN OF RESPONSIBILITY (Bloque Investigativo #1) - Rol: ConcreteHandler 1.
 * Primer eslabón: Verifica datos obligatorios y que el peso esté en el rango operativo (0.1 a 500 kg).
 */
public class ValidadorDatosYPeso extends ValidadorAdmision {
    private static final double PESO_MAXIMO_KG = 500.0;

    @Override
    protected boolean aprobarEslabon(OrdenEnvio orden) {
        if (orden.getCliente() == null || orden.getCliente().trim().isEmpty()
                || orden.getDestino() == null || orden.getDestino().trim().isEmpty()) {
            System.out.println("    [CHAIN 1 - RECHAZO] Datos de cliente o destino incompletos en guía " + orden.getCodigoGuia());
            return false;
        }
        if (orden.getPesoKg() <= 0 || orden.getPesoKg() > PESO_MAXIMO_KG) {
            System.out.println("    [CHAIN 1 - RECHAZO] Peso fuera de límite operativo ("
                    + orden.getPesoKg() + " kg). Máximo permitido: " + PESO_MAXIMO_KG + " kg.");
            return false;
        }
        System.out.println("    [CHAIN 1 - OK] ValidadorDatosYPeso: Peso (" + orden.getPesoKg()
                + " kg) y destino ('" + orden.getDestino() + "') verificados.");
        return true;
    }
}
