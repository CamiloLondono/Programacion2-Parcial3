import java.util.ArrayDeque;
import java.util.Deque;

/**
 * PATRÓN MEMENTO (Bloque Investigativo #3) - Rol: Caretaker (Guardián / Custodio).
 * Administra la pila de instantáneas (OrdenMemento) sin modificar su contenido interno.
 */
public class GestorHistorialMementos {
    private final Deque<OrdenMemento> pilaSnapshots = new ArrayDeque<>();

    public void guardarSnapshot(OrdenMemento memento) {
        pilaSnapshots.push(memento);
        System.out.println("  [MEMENTO - Caretaker] Snapshot guardado -> " + memento);
    }

    public OrdenMemento extraerUltimoSnapshot() {
        if (pilaSnapshots.isEmpty()) {
            return null;
        }
        return pilaSnapshots.pop();
    }

    public boolean tieneSnapshots() {
        return !pilaSnapshots.isEmpty();
    }

    public void mostrarHistorialSnapshots() {
        System.out.println("  [MEMENTO - Historial de Puntos de Restauración Disponibles (" + pilaSnapshots.size() + ")]:");
        for (OrdenMemento m : pilaSnapshots) {
            System.out.println("    * " + m);
        }
    }
}
