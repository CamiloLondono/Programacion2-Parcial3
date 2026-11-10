/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: ConcreteCommand 1.
 * Conecta Command -> Memento -> State -> Strategy -> Observer -> Mediator.
 * Cambia la estrategia de cotización del envío guardando previamente un Memento.
 */
public class ComandoCambiarModalidad implements ComandoLogistico {
    private final OrdenEnvio orden;
    private final EstrategiaTarifa nuevaEstrategia;
    private final GestorHistorialMementos gestorMementos;
    private final MediadorLogistico mediador;

    public ComandoCambiarModalidad(OrdenEnvio orden,
                                   EstrategiaTarifa nuevaEstrategia,
                                   GestorHistorialMementos gestorMementos,
                                   MediadorLogistico mediador) {
        this.orden = orden;
        this.nuevaEstrategia = nuevaEstrategia;
        this.gestorMementos = gestorMementos;
        this.mediador = mediador;
    }

    @Override
    public boolean ejecutar() {
        OrdenMemento snapshotPrevio = orden.crearMemento("Antes de cambiar modalidad a " + nuevaEstrategia.getNombreModalidad());
        boolean exito = orden.cambiarEstrategiaTarifa(nuevaEstrategia);
        if (exito) {
            gestorMementos.guardarSnapshot(snapshotPrevio);
            mediador.notificarEvento("Gestor de Comandos", "CAMBIO_MODALIDAD",
                    orden, "Nueva modalidad: " + nuevaEstrategia.getNombreModalidad());
            return true;
        }
        return false;
    }

    @Override
    public void deshacer() {
        OrdenMemento snapshot = gestorMementos.extraerUltimoSnapshot();
        if (snapshot != null) {
            orden.restaurarDesdeMemento(snapshot);
            mediador.notificarEvento("Gestor de Comandos (Undo)", "ROLLBACK_RESTAURADO",
                    orden, "Reversión de cambio de modalidad tarifaria");
        }
    }

    @Override
    public String getNombreComando() {
        return "ComandoCambiarModalidad [" + orden.getCodigoGuia() + " -> " + nuevaEstrategia.getNombreModalidad() + "]";
    }
}
