/**
 * PATRÓN STRATEGY (Bloque Base #2) - Rol: ConcreteStrategy 3.
 * Calcula el costo para carga crítica de exportación con control térmico activo (cadena de frío).
 */
public class TarifaPrioritariaRefrigerada implements EstrategiaTarifa {
    private static final double CARGO_BASE = 120000.0;
    private static final double COSTO_POR_KG = 14000.0;
    private static final double PORCENTAJE_SEGURO = 0.020; // 2.0% del valor declarado

    @Override
    public String getNombreModalidad() {
        return "Prioritaria Refrigerada (Cadena de Frío)";
    }

    @Override
    public String getTiempoEstimado() {
        return "24 a 36 horas (Control Térmico Activo)";
    }

    @Override
    public double calcularCosto(OrdenEnvio orden) {
        double fletePeso = orden.getPesoKg() * COSTO_POR_KG;
        double seguro = orden.getValorDeclaradoCOP() * PORCENTAJE_SEGURO;
        return CARGO_BASE + fletePeso + seguro;
    }
}
