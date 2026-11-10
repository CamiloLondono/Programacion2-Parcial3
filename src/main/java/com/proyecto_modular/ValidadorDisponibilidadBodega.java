/**
 * PATRÓN CHAIN OF RESPONSIBILITY (Bloque Investigativo #1) - Rol: ConcreteHandler 3.
 * Tercer eslabón: Verifica cupo técnico en el centro de distribución para el tipo de carga.
 */
public class ValidadorDisponibilidadBodega extends ValidadorAdmision {
    @Override
    protected boolean aprobarEslabon(OrdenEnvio orden) {
        String tipoCarga = orden.isEsInternacional() ? "Internacional / Aduana" : "Nacional";
        System.out.println("    [CHAIN 3 - OK] ValidadorDisponibilidadBodega: Cupo confirmado en muelle "
                + tipoCarga + " para la guía " + orden.getCodigoGuia() + ".");
        return true;
    }
}
