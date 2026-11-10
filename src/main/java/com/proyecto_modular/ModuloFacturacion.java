/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: ConcreteColleague 3.
 * Gestiona la emisión de facturas, ajustes por cambio de estrategia tarifaria y cierres contables.
 */
public class ModuloFacturacion extends DepartamentoColega {

    public ModuloFacturacion() {
        super("Módulo de Facturación y Recaudo");
    }

    public void emitirOrdenCobroInicial(OrdenEnvio orden) {
        System.out.println("    [FACTURACIÓN] Factura electrónica emitida para " + orden.getCliente()
                + " por $" + Formato.pesos(orden.getCostoEnvioCalculado())
                + " COP (Modalidad: " + orden.getEstrategiaTarifa().getNombreModalidad() + ").");
    }

    public void reliquidarFacturaPorCambioModalidad(OrdenEnvio orden) {
        System.out.println("    [FACTURACIÓN] Nota de ajuste tarifario generada para guía "
                + orden.getCodigoGuia() + ". Nuevo saldo actualizado: $"
                + Formato.pesos(orden.getCostoEnvioCalculado()) + " COP.");
    }

    public void congelarCobroPorIncidencia(OrdenEnvio orden) {
        System.out.println("    [FACTURACIÓN] Cobro de guía " + orden.getCodigoGuia()
                + " puesto en espera preventiva mientras se resuelve la incidencia en ruta.");
    }

    public void cerrarExpedienteContable(OrdenEnvio orden) {
        System.out.println("    [FACTURACIÓN] Paz y salvo financiero registrado para la guía "
                + orden.getCodigoGuia() + ". Transacción cerrada exitosamente.");
    }
}
