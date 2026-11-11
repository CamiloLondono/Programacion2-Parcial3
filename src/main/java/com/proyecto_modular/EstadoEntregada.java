/**
 * PATRÓN STATE (Bloque Base #3) - Rol: ConcreteState 5 (Estado Final).
 * El envío fue entregado al destinatario. Bloquea cualquier modificación o transición adicional.
 */
public class EstadoEntregada implements EstadoOrden {
    @Override
    public String getNombreEstado() {
        return "ENTREGADA";
    }

    @Override
    public boolean permiteCambioModalidad() {
        return false;
    }

    @Override
    public void avanzar(OrdenEnvio orden) {
        System.out.println("  [STATE] La orden " + orden.getCodigoGuia() + " ya fue ENTREGADA. Fin del ciclo de vida.");
    }

    @Override
    public void reportarIncidencia(OrdenEnvio orden, String motivo) {
        System.out.println("  [STATE] No se pueden reportar incidencias de ruta sobre una orden ya ENTREGADA.");
    }
}
