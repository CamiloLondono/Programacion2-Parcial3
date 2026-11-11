/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: ConcreteCommand 4.
 * Conecta Command -> Memento -> State -> Observer -> Mediator.
 * Avanza la orden de EN_TRANSITO a ENTREGADA y dispara el cierre operativo y contable.
 */
public class ComandoConfirmarEntrega implements ComandoLogistico {
    private final OrdenEnvio orden;
    private final GestorHistorialMementos gestorMementos;
    private final MediadorLogistico mediador;

    public ComandoConfirmarEntrega(OrdenEnvio orden,
                                   GestorHistorialMementos gestorMementos,
                                   MediadorLogistico mediador) {
        this.orden = orden;
        this.gestorMementos = gestorMementos;
        this.mediador = mediador;
    }

    @Override
    public boolean ejecutar() {
        if (!(orden.getEstadoActual() instanceof EstadoEnTransito)) {
            orden.avanzarEstado(); // Mostrará el mensaje de bloqueo del patrón State
            return false;
        }
        gestorMementos.guardarSnapshot(orden.crearMemento("Antes de confirmar entrega final en destino"));
        orden.avanzarEstado(); // Pasa de EN_TRANSITO a ENTREGADA
        mediador.notificarEvento("Confirmación de Destino", "ORDEN_ENTREGADA",
                orden, "Entrega final verificada con firma digital");
        return true;
    }

    @Override
    public void deshacer() {
        OrdenMemento snapshot = gestorMementos.extraerUltimoSnapshot();
        if (snapshot != null) {
            orden.restaurarDesdeMemento(snapshot);
            mediador.notificarEvento("Confirmación de Destino (Undo)", "ROLLBACK_RESTAURADO",
                    orden, "Reversión de acta de entrega");
        }
    }

    @Override
    public String getNombreComando() {
        return "ComandoConfirmarEntrega [" + orden.getCodigoGuia() + "]";
    }
}
