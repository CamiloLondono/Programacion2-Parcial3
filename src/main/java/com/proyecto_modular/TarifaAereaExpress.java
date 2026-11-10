/**
 * PATRÓN STRATEGY (Bloque Base #2) - Rol: ConcreteStrategy 2.
 * Calcula el costo para transporte aéreo prioritario de alta velocidad.
 */
public class TarifaAereaExpress implements EstrategiaTarifa {
    private static final double CARGO_BASE = 75000.0;
    private static final double COSTO_POR_KG = 9500.0;
    private static final double PORCENTAJE_SEGURO = 0.012; // 1.2% del valor declarado

    @Override
    public String getNombreModalidad() {
        return "Aérea Express";
    }

    @Override
    public String getTiempoEstimado() {
        return "12 a 24 horas";
    }

    @Override
    public double calcularCosto(OrdenEnvio orden) {
        double fletePeso = orden.getPesoKg() * COSTO_POR_KG;
        double seguro = orden.getValorDeclaradoCOP() * PORCENTAJE_SEGURO;
        return CARGO_BASE + fletePeso + seguro;
    }
}
