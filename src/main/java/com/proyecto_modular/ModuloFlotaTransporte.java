/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: ConcreteColleague 2.
 * Administra la asignación de vehículos/aeronaves y las rutas de contingencia.
 */
public class ModuloFlotaTransporte extends DepartamentoColega {

    public ModuloFlotaTransporte() {
        super("Módulo de Flota y Transporte");
    }

    public void asignarUnidadSegunModalidad(OrdenEnvio orden) {
        String modalidad = orden.getEstrategiaTarifa().getNombreModalidad();
        String unidad;
        if (modalidad.contains("Refrigerada")) {
            unidad = "AeroCarga ThermoKing ATR-72 (Matrícula HK-5410-UQ)";
        } else if (modalidad.contains("Aérea")) {
            unidad = "Carguero Express Boeing 737F (Matrícula HK-4902)";
        } else {
            unidad = "Tractocamión Kenworth T800 (Placa UQ-984)";
        }
        orden.setVehiculoAsignado(unidad);
        System.out.println("    [FLOTA] Unidad asignada a guía " + orden.getCodigoGuia() + ": " + unidad);
    }

    public void activarProtocoloContingencia(OrdenEnvio orden, String detalle) {
        String unidadContingencia = orden.getVehiculoAsignado() + " [EN ESPERA / RUTA ALTERNA ACTIVA]";
        orden.setVehiculoAsignado(unidadContingencia);
        System.out.println("    [FLOTA] Contingencia activada para " + orden.getCodigoGuia()
                + " (" + detalle + "). Unidad en resguardo seguro.");
    }

    public void normalizarRutaUnidad(OrdenEnvio orden) {
        System.out.println("    [FLOTA] Alerta superada. La unidad '" + orden.getVehiculoAsignado()
                + "' retoma su itinerario normal hacia " + orden.getDestino() + ".");
    }

    public void liberarUnidad(OrdenEnvio orden) {
        System.out.println("    [FLOTA] Entrega completada en " + orden.getDestino()
                + ". Unidad '" + orden.getVehiculoAsignado() + "' disponible para nuevos despachos.");
        orden.setVehiculoAsignado(orden.getVehiculoAsignado() + " ( Viaje Finalizado )");
    }
}
