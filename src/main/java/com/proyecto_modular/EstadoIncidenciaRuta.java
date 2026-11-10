/**
 * PATRÓN STATE (Bloque Base #3) - Rol: ConcreteState 4.
 * Estado de bloqueo preventivo cuando ocurre una contingencia en el transporte.
 * Impide entregar el paquete directamente hasta que se revierta o resuelva la contingencia.
 */
public class EstadoIncidenciaRuta implements EstadoOrden {
    private final String motivoIncidencia;

    public EstadoIncidenciaRuta(String motivoIncidencia) {
        this.motivoIncidencia = motivoIncidencia;
    }

    @Override
    public String getNombreEstado() {
        return "INCIDENCIA_EN_RUTA (" + motivoIncidencia + ")";
    }

    @Override
    public boolean permiteCambioModalidad() {
        return false;
    }

    @Override
    public void avanzar(OrdenEnvio orden) {
        System.out.println("  [STATE - Bloqueo] No se puede entregar la orden " + orden.getCodigoGuia()
                + " mientras permanezca en estado de INCIDENCIA. Debe resolverse o revertirse primero.");
    }

    @Override
    public void reportarIncidencia(OrdenEnvio orden, String motivo) {
        System.out.println("  [STATE] La orden ya se encuentra con una incidencia activa: " + motivoIncidencia);
    }
}
