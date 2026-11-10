/**
 * PATRÓN OBSERVER (Bloque Base #1) - Rol: ConcreteObserver 3.
 * Registra cada evento en la bitácora inmutable de calidad y acuerdos de nivel de servicio (SLA).
 */
public class MonitorAuditoriaSLA implements ObservadorOrden {
    private int contadorEventos = 0;

    @Override
    public void actualizar(OrdenEnvio orden, String detalleEvento) {
        contadorEventos++;
        System.out.println("    [OBSERVER - Auditoría SLA #" + contadorEventos + "]: Registro verificado para "
                + orden.getCodigoGuia() + " | Tiempo prometido: "
                + orden.getEstrategiaTarifa().getTiempoEstimado());
    }
}
