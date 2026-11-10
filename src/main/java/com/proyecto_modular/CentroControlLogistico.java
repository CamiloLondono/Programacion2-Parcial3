/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: ConcreteMediator.
 * Coordina todas las interacciones entre Bodega, Flota y Facturación.
 * Ningún departamento invoca a otro directamente; todo pasa por este Centro de Control.
 */
public class CentroControlLogistico implements MediadorLogistico {
    private final ModuloBodega bodega;
    private final ModuloFlotaTransporte flota;
    private final ModuloFacturacion facturacion;

    public CentroControlLogistico(ModuloBodega bodega,
                                  ModuloFlotaTransporte flota,
                                  ModuloFacturacion facturacion) {
        this.bodega = bodega;
        this.flota = flota;
        this.facturacion = facturacion;

        this.bodega.setMediador(this);
        this.flota.setMediador(this);
        this.facturacion.setMediador(this);
    }

    @Override
    public void notificarEvento(String departamentoOrigen, String codigoEvento,
                                OrdenEnvio orden, String detalle) {
        System.out.println("  [MEDIATOR - Centro de Control] Evento '" + codigoEvento
                + "' recibido desde [" + departamentoOrigen + "] -> Coordinando áreas:");

        switch (codigoEvento) {
            case "ADMISION_APROBADA":
                bodega.reservarEspacioBodega(orden);
                break;

            case "CAMBIO_MODALIDAD":
                facturacion.reliquidarFacturaPorCambioModalidad(orden);
                break;

            case "ALISTAMIENTO_COMPLETADO":
                flota.asignarUnidadSegunModalidad(orden);
                facturacion.emitirOrdenCobroInicial(orden);
                break;

            case "INCIDENCIA_RUTA":
                flota.activarProtocoloContingencia(orden, detalle);
                bodega.prepararRetornoTemporal(orden);
                facturacion.congelarCobroPorIncidencia(orden);
                break;

            case "ROLLBACK_RESTAURADO":
                flota.normalizarRutaUnidad(orden);
                facturacion.reliquidarFacturaPorCambioModalidad(orden);
                break;

            case "ORDEN_ENTREGADA":
                flota.liberarUnidad(orden);
                facturacion.cerrarExpedienteContable(orden);
                break;

            default:
                System.out.println("    [MEDIATOR] Evento informativo registrado: " + detalle);
                break;
        }
    }

    public ModuloBodega getBodega() { return bodega; }
    public ModuloFlotaTransporte getFlota() { return flota; }
    public ModuloFacturacion getFacturacion() { return facturacion; }
}
