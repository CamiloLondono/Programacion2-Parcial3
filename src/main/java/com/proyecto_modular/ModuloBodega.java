/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: ConcreteColleague 1.
 * Gestiona el almacenamiento, preparación física y liberación de paquetes en plataforma.
 */
public class ModuloBodega extends DepartamentoColega {

    public ModuloBodega() {
        super("Módulo de Bodega Central");
    }

    public void reservarEspacioBodega(OrdenEnvio orden) {
        System.out.println("    [BODEGA] Espacio de estantería reservado para guía "
                + orden.getCodigoGuia() + " (" + orden.getPesoKg() + " kg).");
    }

    public void confirmarDespachoPlataforma(OrdenEnvio orden) {
        System.out.println("    [BODEGA] Paquete " + orden.getCodigoGuia()
                + " sellado y entregado en muelle de embarque.");
        if (mediador != null) {
            mediador.notificarEvento(nombreDepartamento, "ALISTAMIENTO_COMPLETADO",
                    orden, "Carga lista en muelle para asignación de vehículo y facturación");
        }
    }

    public void prepararRetornoTemporal(OrdenEnvio orden) {
        System.out.println("    [BODEGA] Bahía de contingencia habilitada por si la guía "
                + orden.getCodigoGuia() + " requiere reingreso.");
    }
}
