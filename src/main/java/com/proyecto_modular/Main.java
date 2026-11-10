/**
 * SISTEMA INTEGRADO: AEROQUINDÍO SMART LOGISTICS
 * Parcial de Diseño de Software / Programación II - Universidad del Quindío
 *
 * Demostración funcional en un flujo de negocio unificado de principio a fin
 * que integra de manera cohesiva 8 Patrones de Diseño de Comportamiento (GoF):
 *
 * BLOQUE BASE (5 Patrones):
 *   1. Observer  (ObservadorOrden, NotificadorClienteSMS, PanelMonitoreoTiempoReal, MonitorAuditoriaSLA)
 *   2. Strategy  (EstrategiaTarifa, TarifaTerrestreEstandar, TarifaAereaExpress, TarifaPrioritariaRefrigerada)
 *   3. Command   (ComandoLogistico, ComandoCambiarModalidad, ComandoProcesarYAlistarOrden,
 *                 ComandoReportarIncidencia, ComandoConfirmarEntrega, GestorOperacionesInvoker)
 *   4. State     (EstadoOrden, EstadoRegistrada, EstadoEnAlistamiento, EstadoEnTransito,
 *                 EstadoIncidenciaRuta, EstadoEntregada)
 *   5. Mediator  (MediadorLogistico, CentroControlLogistico, DepartamentoColega,
 *                 ModuloBodega, ModuloFlotaTransporte, ModuloFacturacion)
 *
 * BLOQUE INVESTIGATIVO (3 Patrones GoF):
 *   6. Chain of Responsibility (ValidadorAdmision, ValidadorDatosYPeso,
 *                               ValidadorControlAntifraude, ValidadorDisponibilidadBodega)
 *   7. Template Method         (ProtocoloAlistamientoTemplate, AlistamientoNacional,
 *                               AlistamientoInternacionalAduana)
 *   8. Memento                 (OrdenMemento, GestorHistorialMementos, OrdenEnvio como Originator)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("===================================================================================");
        System.out.println("   AEROQUINDÍO SMART LOGISTICS - MOTOR UNIFICADO CON 8 PATRONES DE COMPORTAMIENTO  ");
        System.out.println("===================================================================================");

        // =========================================================================
        // FASE 1: INICIALIZACIÓN DE INFRAESTRUCTURA (MEDIATOR, CHAIN, MEMENTO, INVOKER)
        // =========================================================================
        System.out.println("\n[FASE 1] Configurando Centro de Control (Mediator), Cadena de Validación (Chain) e Invoker (Command)...");

        // 1.1 Configuración del Patrón MEDIATOR con sus 3 departamentos colegas
        ModuloBodega moduloBodega = new ModuloBodega();
        ModuloFlotaTransporte moduloFlota = new ModuloFlotaTransporte();
        ModuloFacturacion moduloFacturacion = new ModuloFacturacion();
        CentroControlLogistico centroControl = new CentroControlLogistico(
                moduloBodega, moduloFlota, moduloFacturacion
        );

        // 1.2 Configuración del Patrón CHAIN OF RESPONSIBILITY (3 eslabones enlazados)
        ValidadorAdmision cadenaAdmision = new ValidadorDatosYPeso();
        cadenaAdmision
                .enlazarSiguiente(new ValidadorControlAntifraude())
                .enlazarSiguiente(new ValidadorDisponibilidadBodega());

        // 1.3 Configuración del Caretaker (Patrón MEMENTO) y del Invoker (Patrón COMMAND)
        GestorHistorialMementos gestorMementos = new GestorHistorialMementos();
        GestorOperacionesInvoker invoker = new GestorOperacionesInvoker();

        // =========================================================================
        // FASE 2: CREACIÓN DE LA ORDEN, SUSCRIPCIÓN DE OBSERVERS Y CAMBIO DE STRATEGY
        // =========================================================================
        System.out.println("\n[FASE 2] Registro de Orden de Exportación de Café Especial (State + Strategy + Observer)...");

        // Se crea la orden en estado inicial 'REGISTRADA' (State) con 'TarifaTerrestreEstandar' (Strategy)
        OrdenEnvio ordenExportacion = new OrdenEnvio(
                "UQ-EXP-2026-001",
                "Cooperativa Cafetera del Quindío",
                "Miami - Estados Unidos",
                120.0,          // 120 kg de café especial microlote
                18_000_000.0,   // Valor declarado: $18.000.000 COP
                true,           // Es envío internacional
                new TarifaTerrestreEstandar()
        );

        // Suscripción de 3 observadores al sujeto OrdenEnvio (Patrón OBSERVER)
        ordenExportacion.suscribir(new NotificadorClienteSMS());
        ordenExportacion.suscribir(new PanelMonitoreoTiempoReal());
        ordenExportacion.suscribir(new MonitorAuditoriaSLA());

        System.out.println("  -> Estado inicial de la orden recién creada:");
        ordenExportacion.mostrarFichaResumen();

        // El cliente solicita cambiar la modalidad a 'Prioritaria Refrigerada' antes del despacho.
        // Se ejecuta vía COMMAND -> guarda MEMENTO -> valida STATE -> cambia STRATEGY -> avisa OBSERVER y MEDIATOR.
        ComandoLogistico cmdUpgradeModalidad = new ComandoCambiarModalidad(
                ordenExportacion,
                new TarifaPrioritariaRefrigerada(),
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdUpgradeModalidad);

        // =========================================================================
        // FASE 3: VALIDACIÓN EN CADENA Y ALISTAMIENTO CON TEMPLATE METHOD
        // =========================================================================
        System.out.println("\n[FASE 3] Admisión por Cadena de Responsabilidad y Alistamiento con Método Plantilla...");

        // Seleccionamos la plantilla de alistamiento internacional (Patrón TEMPLATE METHOD)
        ProtocoloAlistamientoTemplate plantillaInternacional = new AlistamientoInternacionalAduana();

        // Ejecutamos el comando integral que conecta:
        // Command -> Chain of Responsibility -> Memento -> Mediator -> State -> Template Method -> Observer
        ComandoLogistico cmdProcesarYAlistar = new ComandoProcesarYAlistarOrden(
                ordenExportacion,
                cadenaAdmision,
                plantillaInternacional,
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdProcesarYAlistar);

        System.out.println("\n  -> Ficha de la orden tras superar Chain of Responsibility y Template Method:");
        ordenExportacion.mostrarFichaResumen();

        // Demostración de restricción del Patrón STATE conectada con STRATEGY:
        // Intentar cambiar la tarifa a Terrestre cuando el avión ya está EN_TRANSITO (debe bloquearse)
        System.out.println("\n[PRUEBA DE REGLA STATE -> STRATEGY] Intentando cambiar tarifa mientras está EN_TRANSITO:");
        ComandoLogistico cmdCambioInvalidoEnRuta = new ComandoCambiarModalidad(
                ordenExportacion,
                new TarifaAereaExpress(),
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdCambioInvalidoEnRuta);

        // =========================================================================
        // FASE 4: CONTINGENCIA EN RUTA Y RECUPERACIÓN (MEMENTO + COMMAND UNDO)
        // =========================================================================
        System.out.println("\n[FASE 4] Contingencia en Ruta (State + Mediator) y Restauración con Memento (Command Undo)...");

        // Se reporta cierre temporal de pista por tormenta eléctrica mediante un Comando
        ComandoLogistico cmdIncidencia = new ComandoReportarIncidencia(
                ordenExportacion,
                "Tormenta eléctrica en corredor aéreo del Caribe",
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdIncidencia);

        // Intentamos confirmar la entrega mientras la orden tiene una incidencia activa:
        // El patrón STATE impide entregar un paquete retenido por contingencia.
        System.out.println("\n[PRUEBA DE BLOQUEO STATE] Intentando entregar orden mientras está en INCIDENCIA_EN_RUTA:");
        ComandoLogistico cmdIntentoEntregaBloqueado = new ComandoConfirmarEntrega(
                ordenExportacion,
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdIntentoEntregaBloqueado);

        // La torre meteorológica informa que la tormenta se disipó.
        // El Invoker deshace el último comando (ComandoReportarIncidencia), restaurando el Memento previo (EN_TRANSITO).
        invoker.deshacerUltimaOperacion();

        // =========================================================================
        // FASE 5: ENTREGA EXITOSA EN DESTINO Y CIERRE DEL CICLO UNIFICADO
        // =========================================================================
        System.out.println("\n[FASE 5] Confirmación de Entrega Final en Destino y Cierre en el Mediador...");

        ComandoLogistico cmdEntregaFinal = new ComandoConfirmarEntrega(
                ordenExportacion,
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdEntregaFinal);

        System.out.println("\n  -> Ficha definitiva de la orden entregada:");
        ordenExportacion.mostrarFichaResumen();

        // =========================================================================
        // FASE 6: CASO DE CONTROL - BLOQUEO PREVENTIVO EN CHAIN OF RESPONSIBILITY
        // =========================================================================
        System.out.println("\n===================================================================================");
        System.out.println("[FASE 6 - CASO DE CONTROL] Envío Nacional Rechazado por Chain of Responsibility");
        System.out.println("===================================================================================");

        OrdenEnvio ordenSospechosa = new OrdenEnvio(
                "UQ-NAC-2026-999",
                "CLIENTE_REPORTADO_FRAUDE",
                "Bogotá D.C.",
                45.0,
                85_000_000.0, // Supera además el tope de $50.000.000 COP
                false,
                new TarifaAereaExpress()
        );
        ordenSospechosa.suscribir(new PanelMonitoreoTiempoReal());

        ComandoLogistico cmdAlistarSospechosa = new ComandoProcesarYAlistarOrden(
                ordenSospechosa,
                cadenaAdmision,
                new AlistamientoNacional(),
                gestorMementos,
                centroControl
        );
        invoker.ejecutarOperacion(cmdAlistarSospechosa);
        ordenSospechosa.mostrarFichaResumen();

        // =========================================================================
        // RESUMEN DE TRAZABILIDAD (COMMAND INVOKER + MEMENTO CARETAKER)
        // =========================================================================
        System.out.println("\n===================================================================================");
        System.out.println("                    AUDITORÍA FINAL DE COMANDOS Y MEMENTOS                         ");
        System.out.println("===================================================================================");
        invoker.mostrarBitacoraComandos();
        System.out.println();
        gestorMementos.mostrarHistorialSnapshots();
        System.out.println("===================================================================================");
        System.out.println("   FIN DE LA SIMULACIÓN - LOS 8 PATRONES DE COMPORTAMIENTO SE EJECUTARON CON ÉXITO ");
        System.out.println("===================================================================================");
    }
}
