import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * PATRÓN COMMAND (Bloque Base #5) - Rol: Invoker (Invocador de Operaciones).
 * Desacopla a quien solicita la operación de quien sabe cómo ejecutarla,
 * llevando una bitácora completa de comandos y una pila para deshacer (Undo).
 */
public class GestorOperacionesInvoker {
    private final Deque<ComandoLogistico> pilaComandosEjecutados = new ArrayDeque<>();
    private final List<String> bitacoraComandos = new ArrayList<>();

    public boolean ejecutarOperacion(ComandoLogistico comando) {
        System.out.println("\n>>> [COMMAND INVOKER] Ejecutando: " + comando.getNombreComando());
        boolean resultado = comando.ejecutar();
        if (resultado) {
            pilaComandosEjecutados.push(comando);
            bitacoraComandos.add("[EXITOSO] " + comando.getNombreComando());
        } else {
            bitacoraComandos.add("[FALLIDO/BLOQUEADO] " + comando.getNombreComando());
        }
        return resultado;
    }

    public void deshacerUltimaOperacion() {
        if (pilaComandosEjecutados.isEmpty()) {
            System.out.println("\n>>> [COMMAND INVOKER] No hay operaciones pendientes para deshacer.");
            return;
        }
        ComandoLogistico ultimoComando = pilaComandosEjecutados.pop();
        System.out.println("\n<<< [COMMAND INVOKER - UNDO] Deshaciendo última operación: "
                + ultimoComando.getNombreComando());
        ultimoComando.deshacer();
        bitacoraComandos.add("[DESHECHO / ROLLBACK] " + ultimoComando.getNombreComando());
    }

    public void mostrarBitacoraComandos() {
        System.out.println("  [COMMAND INVOKER - Bitácora Histórica de Operaciones]:");
        for (int i = 0; i < bitacoraComandos.size(); i++) {
            System.out.println("    " + (i + 1) + ". " + bitacoraComandos.get(i));
        }
    }
}
