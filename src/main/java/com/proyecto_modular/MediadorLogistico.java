/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: Mediator (Interfaz Mediadora).
 * Centraliza la comunicación entre los departamentos operativos (Bodega, Flota y Facturación)
 * para evitar referencias cruzadas directas (acoplamiento muchos-a-muchos).
 */
public interface MediadorLogistico {
    void notificarEvento(String departamentoOrigen, String codigoEvento, OrdenEnvio orden, String detalle);
}
