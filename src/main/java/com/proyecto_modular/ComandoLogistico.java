/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: Command (Interfaz Comando).
 * Encapsula una solicitud u operación logística como un objeto independiente,
 * permitiendo parametrizar acciones, llevar bitácora y soportar operaciones deshacer (Undo).
 */
public interface ComandoLogistico {
    boolean ejecutar();
    void deshacer();
    String getNombreComando();
}
