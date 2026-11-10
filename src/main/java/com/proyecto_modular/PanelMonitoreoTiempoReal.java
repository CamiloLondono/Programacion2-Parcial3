/**
 * PATRÓN OBSERVER (Bloque Base #1) - Rol: ConcreteObserver 2.
 * Actualiza el tablero visual de la torre de control logístico en tiempo real.
 */
public class PanelMonitoreoTiempoReal implements ObservadorOrden {
    @Override
    public void actualizar(OrdenEnvio orden, String detalleEvento) {
        System.out.println("    [OBSERVER - Dashboard Torre Control]: [" + orden.getCodigoGuia()
                + " | Estado: " + orden.getEstadoActual().getNombreEstado()
                + "] Actualización: " + detalleEvento);
    }
}
