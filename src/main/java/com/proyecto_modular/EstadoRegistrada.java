/**
 * PATRÓN STATE (Bloque Base #3) - Rol: ConcreteState 1 (Estado Inicial).
 * La orden ha sido registrada en el sistema. Permite cambios de modalidad de tarifa.
 * Solo puede avanzar a 'EN_ALISTAMIENTO' si ya aprobó la Cadena de Responsabilidad.
 */
public class EstadoRegistrada implements EstadoOrden {
    @Override
    public String getNombreEstado() {
        return "REGISTRADA";
    }

    @Override
    public boolean permiteCambioModalidad() {
        return true;
    }

    @Override
    public void avanzar(OrdenEnvio orden) {
        if (!orden.isAprobadaPorAdmision()) {
            System.out.println("  [STATE - Rechazo] La orden " + orden.getCodigoGuia()
                    + " no puede pasar a Alistamiento porque no ha superado los filtros de admisión.");
            return;
        }
        orden.cambiarEstadoInterno(new EstadoEnAlistamiento(), "Admisión superada; ingresa a planta de bodega");
    }

    @Override
    public void reportarIncidencia(OrdenEnvio orden, String motivo) {
        orden.cambiarEstadoInterno(new EstadoIncidenciaRuta(motivo), "Novedad administrativa previa al despacho: " + motivo);
    }
}
