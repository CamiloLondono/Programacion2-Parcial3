/**
 * PATRÓN STRATEGY (Bloque Base #2) - Rol: Strategy (Interfaz de Estrategia).
 * Encapsula la familia de algoritmos de cotización logística (flete + seguro + tiempos)
 * para que sean intercambiables en tiempo de ejecución sin usar condicionales en OrdenEnvio.
 */
public interface EstrategiaTarifa {
    String getNombreModalidad();
    String getTiempoEstimado();
    double calcularCosto(OrdenEnvio orden);
}
