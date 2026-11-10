/**
 * PATRÓN TEMPLATE METHOD (Bloque Investigativo #2) - Rol: AbstractClass (Clase Plantilla).
 * Define el esqueleto invariante del algoritmo de alistamiento de mercancía en bodega
 * mediante el método 'final ejecutarProtocoloAlistamiento', permitiendo que las subclases
 * personalicen los pasos de embalaje, documentación e inspecciones especiales.
 */
public abstract class ProtocoloAlistamientoTemplate {

    /**
     * MÉTODO PLANTILLA (Template Method): Es 'final' para impedir que las subclases
     * alteren el orden estricto de los pasos del flujo operativo.
     */
    public final void ejecutarProtocoloAlistamiento(OrdenEnvio orden, ModuloBodega moduloBodega) {
        System.out.println("  [TEMPLATE METHOD] Iniciando protocolo estandarizado para guía " + orden.getCodigoGuia() + ":");
        verificarIntegridadMercancia(orden);
        aplicarEmbalajeEspecifico(orden);
        generarDocumentacionLegal(orden);
        ejecutarInspeccionAdicionalHook(orden);
        liberarHaciaPlataformaDespacho(orden, moduloBodega);
    }

    // Paso 1: Común para todos los envíos
    private void verificarIntegridadMercancia(OrdenEnvio orden) {
        System.out.println("    1. [Paso Común] Pesaje en báscula digital (" + orden.getPesoKg()
                + " kg) y escaneo láser de código de barras completado.");
    }

    // Paso 2: Abstracto (varía según modalidad nacional o internacional)
    protected abstract void aplicarEmbalajeEspecifico(OrdenEnvio orden);

    // Paso 3: Abstracto (varía según exigencias legales del destino)
    protected abstract void generarDocumentacionLegal(OrdenEnvio orden);

    // Paso 4: Hook (Gancho opcional, vacío por defecto; las subclases pueden sobrescribirlo)
    protected void ejecutarInspeccionAdicionalHook(OrdenEnvio orden) {
        // Por defecto no requiere inspección suplementaria
    }

    // Paso 5: Común final que conecta Template Method -> State -> Mediator
    private void liberarHaciaPlataformaDespacho(OrdenEnvio orden, ModuloBodega moduloBodega) {
        System.out.println("    5. [Paso Común Final] Sellado de seguridad completado. Avanzando estado a EN_TRANSITO...");
        orden.avanzarEstado(); // Transiciona de EN_ALISTAMIENTO a EN_TRANSITO (Patrón State + Observer)
        moduloBodega.confirmarDespachoPlataforma(orden); // Notifica al Centro de Control (Patrón Mediator)
    }
}
