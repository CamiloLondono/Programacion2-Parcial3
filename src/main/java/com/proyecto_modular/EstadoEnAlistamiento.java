/**
 * PATRÓN STATE (Bloque Base #3) - Rol: ConcreteState 2.
 * La orden se encuentra en preparación física dentro de la bodega (Template Method).
 */
public class EstadoEnAlistamiento implements EstadoOrden {
    @Override
    public String getNombreEstado() {
        return "EN_ALISTAMIENTO";
    }

    @Override
    public boolean permiteCambioModalidad() {
        return true;
    }

    @Override
    public void avanzar(OrdenEnvio orden) {
        orden.cambiarEstadoInterno(new EstadoEnTransito(), "Protocolo de alistamiento completado; unidad despachada");
    }

    @Override
    public void reportarIncidencia(OrdenEnvio orden, String motivo) {
        orden.cambiarEstadoInterno(new EstadoIncidenciaRuta(motivo), "Incidente durante alistamiento en bodega: " + motivo);
    }
}
