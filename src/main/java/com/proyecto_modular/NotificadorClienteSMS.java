/**
 * PATRÓN OBSERVER (Bloque Base #1) - Rol: ConcreteObserver 1.
 * Envía alertas de seguimiento al cliente propietario del envío.
 */
public class NotificadorClienteSMS implements ObservadorOrden {
    @Override
    public void actualizar(OrdenEnvio orden, String detalleEvento) {
        System.out.println("    [OBSERVER - SMS Cliente (" + orden.getCliente() + ")]: Guía "
                + orden.getCodigoGuia() + " -> " + detalleEvento);
    }
}
