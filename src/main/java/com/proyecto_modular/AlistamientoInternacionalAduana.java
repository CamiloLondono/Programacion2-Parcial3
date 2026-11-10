/**
 * PATRÓN TEMPLATE METHOD (Bloque Investigativo #2) - Rol: ConcreteClass 2.
 * Implementa los pasos particulares y el Hook de inspección aduanera/fitosanitaria
 * para envíos internacionales de exportación.
 */
public class AlistamientoInternacionalAduana extends ProtocoloAlistamientoTemplate {

    @Override
    protected void aplicarEmbalajeEspecifico(OrdenEnvio orden) {
        System.out.println("    2. [Alistamiento Internacional] Contenedor isotérmico con recubrimiento al vacío y precinto satelital ISO-17712.");
    }

    @Override
    protected void generarDocumentacionLegal(OrdenEnvio orden) {
        System.out.println("    3. [Alistamiento Internacional] Declaración de Exportación (DEX - DIAN), Carta de Porte Internacional y Certificado de Origen emitidos hacia "
                + orden.getDestino() + ".");
    }

    @Override
    protected void ejecutarInspeccionAdicionalHook(OrdenEnvio orden) {
        System.out.println("    4. [Hook Aduanero / Fitosanitario] Inspección ICA y escáner antinarcóticos de exportación APROBADOS.");
    }
}
