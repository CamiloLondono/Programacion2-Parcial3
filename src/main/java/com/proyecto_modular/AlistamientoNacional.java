/**
 * PATRÓN TEMPLATE METHOD (Bloque Investigativo #2) - Rol: ConcreteClass 1.
 * Implementa los pasos particulares para envíos dentro del territorio nacional.
 */
public class AlistamientoNacional extends ProtocoloAlistamientoTemplate {

    @Override
    protected void aplicarEmbalajeEspecifico(OrdenEnvio orden) {
        System.out.println("    2. [Alistamiento Nacional] Empaque en caja corrugada doble pared con cinta de seguridad nacional.");
    }

    @Override
    protected void generarDocumentacionLegal(OrdenEnvio orden) {
        System.out.println("    3. [Alistamiento Nacional] Remesa terrestre y manifiesto electrónico RNDC generados para ruta hacia "
                + orden.getDestino() + ".");
    }
}
