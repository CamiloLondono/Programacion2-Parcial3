/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: ConcreteCommand 2.
 * Conecta Command -> Memento -> Chain of Responsibility -> State -> Template Method -> Mediator -> Observer.
 * Valida la orden en la cadena de admisión y, si aprueba, ejecuta el protocolo de alistamiento.
 */
public class ComandoProcesarYAlistarOrden implements ComandoLogistico {
    private final OrdenEnvio orden;
    private final ValidadorAdmision cadenaValidacion;
    private final ProtocoloAlistamientoTemplate protocoloAlistamiento;
    private final GestorHistorialMementos gestorMementos;
    private final CentroControlLogistico centroControl;

    public ComandoProcesarYAlistarOrden(OrdenEnvio orden,
                                        ValidadorAdmision cadenaValidacion,
                                        ProtocoloAlistamientoTemplate protocoloAlistamiento,
                                        GestorHistorialMementos gestorMementos,
                                        CentroControlLogistico centroControl) {
        this.orden = orden;
        this.cadenaValidacion = cadenaValidacion;
        this.protocoloAlistamiento = protocoloAlistamiento;
        this.gestorMementos = gestorMementos;
        this.centroControl = centroControl;
    }

    @Override
    public boolean ejecutar() {
        System.out.println("  [COMMAND -> CHAIN OF RESPONSIBILITY] Ejecutando cadena de validación para " + orden.getCodigoGuia() + ":");
        if (!cadenaValidacion.validar(orden)) {
            orden.notificarObservadores("Orden RECHAZADA en la Cadena de Admisión. No se procesará el alistamiento.");
            return false;
        }

        // Guardamos snapshot antes de transformar el estado de la orden
        gestorMementos.guardarSnapshot(orden.crearMemento("Antes de iniciar alistamiento y despacho en bodega"));

        // Notificamos admisión al Mediador para reservar espacio en bodega
        centroControl.notificarEvento("Cadena de Validación", "ADMISION_APROBADA",
                orden, "Orden validada exitosamente por los 3 filtros");

        // Avanzamos estado de REGISTRADA a EN_ALISTAMIENTO (Patrón State + Observer)
        orden.avanzarEstado();

        // Ejecutamos el Template Method (que al finalizar avanza a EN_TRANSITO y avisa al Mediador)
        protocoloAlistamiento.ejecutarProtocoloAlistamiento(orden, centroControl.getBodega());
        return true;
    }

    @Override
    public void deshacer() {
        OrdenMemento snapshot = gestorMementos.extraerUltimoSnapshot();
        if (snapshot != null) {
            orden.restaurarDesdeMemento(snapshot);
            centroControl.notificarEvento("Gestor de Comandos (Undo)", "ROLLBACK_RESTAURADO",
                    orden, "Reversión del proceso de alistamiento");
        }
    }

    @Override
    public String getNombreComando() {
        return "ComandoProcesarYAlistarOrden [" + orden.getCodigoGuia() + "]";
    }
}
