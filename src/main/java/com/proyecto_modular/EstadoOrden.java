/**
 * PATRÓN STATE (Bloque Base #3) - Rol: State (Interfaz de Estado).
 * Define el comportamiento polimórfico que depende del estado actual del envío.
 */
public interface EstadoOrden {
    String getNombreEstado();
    boolean permiteCambioModalidad();
    void avanzar(OrdenEnvio orden);
    void reportarIncidencia(OrdenEnvio orden, String motivo);
}
