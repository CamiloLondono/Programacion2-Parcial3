/**
 * PATRÓN CHAIN OF RESPONSIBILITY (Bloque Investigativo #1) - Rol: ConcreteHandler 2.
 * Segundo eslabón: Verifica que el remitente no esté en lista restringida y que el
 * valor asegurado no supere el tope de póliza de $50.000.000 COP.
 */
public class ValidadorControlAntifraude extends ValidadorAdmision {
    private static final double TOPE_VALOR_DECLARADO_COP = 50_000_000.0;

    @Override
    protected boolean aprobarEslabon(OrdenEnvio orden) {
        if (orden.getCliente().equalsIgnoreCase("BLOQUEADO")
                || orden.getCliente().toUpperCase().contains("REPORTADO")) {
            System.out.println("    [CHAIN 2 - ALERTA ANTIFRAUDE] El cliente '" + orden.getCliente()
                    + "' registra bloqueo de seguridad. Envío rechazado de inmediato.");
            return false;
        }
        if (orden.getValorDeclaradoCOP() > TOPE_VALOR_DECLARADO_COP) {
            System.out.println("    [CHAIN 2 - RECHAZO PÓLIZA] El valor declarado ($"
                    + Formato.pesos(orden.getValorDeclaradoCOP())
                    + " COP) excede el tope máximo asegurable de $"
                    + Formato.pesos(TOPE_VALOR_DECLARADO_COP) + " COP.");
            return false;
        }
        System.out.println("    [CHAIN 2 - OK] ValidadorControlAntifraude: Cliente '" + orden.getCliente()
                + "' sin antecedentes y valor declarado dentro de cobertura ($"
                + Formato.pesos(orden.getValorDeclaradoCOP()) + " COP).");
        return true;
    }
}
