/**
 * PATRÓN STRATEGY (Bloque Base #2) - Rol: ConcreteStrategy 1.
 * Calcula el costo para transporte terrestre estándar por carretera.
 */
public class TarifaTerrestreEstandar implements EstrategiaTarifa {
    private static final double CARGO_BASE = 20000.0;
    private static final double COSTO_POR_KG = 3500.0;
    private static final double PORCENTAJE_SEGURO = 0.005; // 0.5% del valor declarado

    @Override
    public String getNombreModalidad() {
        return "Terrestre Estándar";
    }

    @Override
    public String getTiempoEstimado() {
        return "72 a 96 horas";
    }

    @Override
    public double calcularCosto(OrdenEnvio orden) {
        double fletePeso = orden.getPesoKg() * COSTO_POR_KG;
        double seguro = orden.getValorDeclaradoCOP() * PORCENTAJE_SEGURO;
        return CARGO_BASE + fletePeso + seguro;
    }
}
