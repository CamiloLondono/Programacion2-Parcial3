/**
 * PATRÓN OBSERVER (Bloque Base #1) - Rol: Observer (Interfaz Suscriptora).
 * Define el contrato para todos los módulos que desean recibir notificaciones
 * automáticas cada vez que una OrdenEnvio cambia de estado, tarifa o sufre un rollback.
 */
public interface ObservadorOrden {
    void actualizar(OrdenEnvio orden, String detalleEvento);
}
