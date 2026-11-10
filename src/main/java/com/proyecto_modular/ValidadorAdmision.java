/**
 * PATRÓN CHAIN OF RESPONSIBILITY (Bloque Investigativo #1) - Rol: Handler (Manejador Base).
 * Define la estructura de la cadena de validación secuencial por la que debe pasar
 * toda OrdenEnvio antes de ser admitida para alistamiento y despacho.
 */
public abstract class ValidadorAdmision {
    protected ValidadorAdmision siguiente;

    public ValidadorAdmision enlazarSiguiente(ValidadorAdmision siguienteValidador) {
        this.siguiente = siguienteValidador;
        return siguienteValidador;
    }

    public boolean validar(OrdenEnvio orden) {
        if (!aprobarEslabon(orden)) {
            orden.setAprobadaPorAdmision(false);
            return false;
        }
        if (siguiente != null) {
            return siguiente.validar(orden);
        }
        orden.setAprobadaPorAdmision(true);
        return true;
    }

    protected abstract boolean aprobarEslabon(OrdenEnvio orden);
}
