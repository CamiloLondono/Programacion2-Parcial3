/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: ConcreteCommand 3.
 * Conecta Command -> Memento -> State -> Observer -> Mediator.
 * Registra una contingencia en ruta, bloquea la entrega y activa protocolos en el Mediador.
 * Al ejecutar deshacer(), restaura la orden exactamente al estado previo mediante Memento.
 */
public class ComandoReportarIncidencia implements ComandoLogistico {
    private final OrdenEnvio orden;
    private final String motivoIncidencia;
    private final GestorHistorialMementos gestorMementos;
    private final MediadorLogistico mediador;

    public ComandoReportarIncidencia(OrdenEnvio orden,
                                     String motivoIncidencia,
                                     GestorHistorialMementos gestorMementos,
                                     MediadorLogistico mediador) {
        this.orden = orden;
        this.motivoIncidencia = motivoIncidencia;
        this.gestorMementos = gestorMementos;
        this.mediador = mediador;
    }

    @Override
    public boolean ejecutar() {
        gestorMementos.guardarSnapshot(orden.crearMemento("Antes de reportar incidencia: " + motivoIncidencia));
        orden.reportarIncidencia(motivoIncidencia);
        mediador.notificarEvento("Monitoreo de Ruta", "INCIDENCIA_RUTA", orden, motivoIncidencia);
        return true;
    }

    @Override
    public void deshacer() {
        OrdenMemento snapshot = gestorMementos.extraerUltimoSnapshot();
        if (snapshot != null) {
            orden.restaurarDesdeMemento(snapshot);
            mediador.notificarEvento("Monitoreo de Ruta (Undo)", "ROLLBACK_RESTAURADO",
                    orden, "Contingencia superada; estado previo restaurado desde Memento");
        }
    }

    @Override
    public String getNombreComando() {
        return "ComandoReportarIncidencia [" + orden.getCodigoGuia() + " - " + motivoIncidencia + "]";
    }
}
