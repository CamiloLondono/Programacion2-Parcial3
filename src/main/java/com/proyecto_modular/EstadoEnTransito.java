/**
 * PATRÓN STATE (Bloque Base #3) - Rol: ConcreteState 3.
 * La orden ya salió hacia su destino. Bloquea cambios de modalidad de tarifa,
 * permite reportar incidencias en la vía/vuelo o avanzar a ENTREGADA.
 */
public class EstadoEnTransito implements EstadoOrden {
    @Override
    public String getNombreEstado() {
        return "EN_TRANSITO";
    }

    @Override
    public boolean permiteCambioModalidad() {
        return false; // En ruta ya no se puede cambiar el tipo de transporte
    }

    @Override
    public void avanzar(OrdenEnvio orden) {
        orden.cambiarEstadoInterno(new EstadoEntregada(), "Acta de entrega firmada a satisfacción en destino");
    }

    @Override
    public void reportarIncidencia(OrdenEnvio orden, String motivo) {
        orden.cambiarEstadoInterno(new EstadoIncidenciaRuta(motivo), "Alerta operativa en ruta: " + motivo);
    }
}
