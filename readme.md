# AeroQuindío Smart Logistics — Sistema Integrado con 8 Patrones de Comportamiento (GoF)

Proyecto de arquitectura de software en **Java** que resuelve la gestión integral, validación de seguridad, cotización dinámica, alistamiento en bodega, coordinación interdepartamental, monitoreo en tiempo real y recuperación ante contingencias (_Rollback_) de órdenes de envío mediante un **flujo de negocio unificado** que conecta **8 patrones de diseño de comportamiento (GoF)**.

---

## 1. Dominio del Problema y Justificación Arquitectónica

En un operador logístico de carga nacional e internacional (**AeroQuindío Smart Logistics**), una orden de envío (`OrdenEnvio`) atraviesa múltiples subsistemas críticos desde su registro hasta su entrega final. En una arquitectura tradicional acoplada, esto genera:

1. Clases gigantes llenas de condicionales (`if/else` o `switch`) para calcular tarifas o validar estados del paquete.
2. Acoplamiento tipo "espagueti" (muchos-a-muchos) entre **Bodega**, **Flota de Transporte** y **Facturación**.
3. Imposibilidad de revertir (_Undo / Rollback_) cambios operativos o falsas alarmas en ruta sin corromper los datos.
4. Duplicación de código entre los protocolos de alistamiento nacional e internacional.

Para resolver estos problemas sin caer en ejercicios aislados, **los 8 patrones operan sobre el mismo objeto central (`OrdenEnvio`)**, donde la salida o evento de cada patrón alimenta directamente al siguiente.

---

## 2. Mapa de los 8 Patrones de Comportamiento Implementados

| #     | Bloque            | Patrón GoF                  | Clases e Interfaces en el Proyecto                                                                                                                                | Problema que Resuelve y Conexión en el Flujo                                                                                                                                                                                                                                 |
| :---- | :---------------- | :-------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1** | **Base**          | **Observer**                | `ObservadorOrden`, `NotificadorClienteSMS`, `PanelMonitoreoTiempoReal`, `MonitorAuditoriaSLA`, `OrdenEnvio` (_Subject_)                                           | Notifica automáticamente en tiempo real al cliente, al dashboard de control y a la auditoría SLA cada vez que **State** cambia de etapa, **Strategy** recalcula el costo o **Memento** restaura un estado previo.                                                            |
| **2** | **Base**          | **Strategy**                | `EstrategiaTarifa`, `TarifaTerrestreEstandar`, `TarifaAereaExpress`, `TarifaPrioritariaRefrigerada`                                                               | Permite intercambiar dinámicamente el algoritmo de cálculo de flete, seguro y tiempos estimados. Es invocado por **Command**, validado por **State** y notificado a Facturación vía **Mediator**.                                                                            |
| **3** | **Base**          | **Command**                 | `ComandoLogistico`, `ComandoCambiarModalidad`, `ComandoProcesarYAlistarOrden`, `ComandoReportarIncidencia`, `ComandoConfirmarEntrega`, `GestorOperacionesInvoker` | Encapsula cada operación logística como un objeto ejecutable con historial y soporte de `deshacer()` (_Undo_). Antes de mutar la orden, solicita un snapshot al patrón **Memento**.                                                                                          |
| **4** | **Base**          | **State**                   | `EstadoOrden`, `EstadoRegistrada`, `EstadoEnAlistamiento`, `EstadoEnTransito`, `EstadoIncidenciaRuta`, `EstadoEntregada`                                          | Controla el ciclo de vida de la `OrdenEnvio` sin condicionales. Bloquea cambios de **Strategy** cuando el envío ya está `EN_TRANSITO` e impide entregar paquetes que estén en `INCIDENCIA_EN_RUTA`.                                                                          |
| **5** | **Base**          | **Mediator**                | `MediadorLogistico`, `CentroControlLogistico`, `DepartamentoColega`, `ModuloBodega`, `ModuloFlotaTransporte`, `ModuloFacturacion`                                 | Desacopla la comunicación entre Bodega, Flota y Facturación. Cuando un **Command**, **State** o **Template Method** finaliza una acción, notifica al `CentroControlLogistico` para que coordine a los demás módulos.                                                         |
| **6** | **Investigativo** | **Chain of Responsibility** | `ValidadorAdmision`, `ValidadorDatosYPeso`, `ValidadorControlAntifraude`, `ValidadorDisponibilidadBodega`                                                         | Filtra la orden a través de 3 eslabones secuenciales (datos/peso, antifraude/tope de póliza y cupo en bodega). Solo si aprueba los 3 eslabones se autoriza pasar a **State** (`EN_ALISTAMIENTO`) y ejecutar el **Template Method**.                                          |
| **7** | **Investigativo** | **Template Method**         | `ProtocoloAlistamientoTemplate`, `AlistamientoNacional`, `AlistamientoInternacionalAduana`                                                                        | Define en un método `final` el esqueleto invariante de 5 pasos para alistar un paquete en bodega, delegando en las subclases el embalaje, la documentación legal y el _hook_ de inspección aduanera. Al terminar, dispara **State** (`EN_TRANSITO`) y avisa al **Mediator**. |
| **8** | **Investigativo** | **Memento**                 | `OrdenMemento`, `GestorHistorialMementos` (_Caretaker_), `OrdenEnvio` (_Originator_)                                                                              | Captura instantáneas inmutables del estado interno de la orden (estado, estrategia, costo, unidad asignada) antes de cada **Command**. Permite restaurar la orden exactamente a su punto previo cuando se ejecuta `deshacerUltimaOperacion()`.                               |

---

## 3. Flujo Unificado de Principio a Fin (Interconexión de Subsistemas)

El archivo `Main.java` ejecuta una simulación continua dividida en **6 fases conectadas**:

1. **Fase 1 — Inicialización del Ecosistema (`Mediator` + `Chain of Responsibility` + `Command Invoker` + `Memento Caretaker`)**:
   - Se instancia el `CentroControlLogistico` (**Mediator**) enlazando `ModuloBodega`, `ModuloFlotaTransporte` y `ModuloFacturacion`.
   - Se enlaza la cadena de validación: `ValidadorDatosYPeso -> ValidadorControlAntifraude -> ValidadorDisponibilidadBodega` (**Chain of Responsibility**).
2. **Fase 2 — Creación de la Orden, Suscripción y Cambio de Tarifa (`State` + `Observer` + `Command` + `Memento` + `Strategy` + `Mediator`)**:
   - Nace la orden de exportación `UQ-EXP-2026-001` (120 kg de café especial hacia Miami, valor declarado `$18.000.000 COP`) en estado `REGISTRADA` (**State**) con `TarifaTerrestreEstandar` (**Strategy** = `$530.000 COP`).
   - Se suscriben los 3 observadores (**Observer**).
   - Se ejecuta `ComandoCambiarModalidad` (**Command**) hacia `TarifaPrioritariaRefrigerada`:
     - Guarda una copia del estado inicial en `GestorHistorialMementos` (**Memento**).
     - Verifica con `EstadoRegistrada` (**State**) si permite cambiar tarifa (`true`).
     - Aplica la nueva **Strategy** (`$2.160.000 COP`), dispara avisos a los 3 **Observers** y notifica al **Mediator** para que `ModuloFacturacion` reliquide la factura.
3. **Fase 3 — Validación en Cadena y Alistamiento en Bodega (`Command` -> `Chain of Responsibility` -> `Memento` -> `Mediator` -> `State` -> `Template Method` -> `Observer`)**:
   - Se ejecuta `ComandoProcesarYAlistarOrden` (**Command**):
     - Evalúa la orden en los 3 filtros de **Chain of Responsibility** (todos aprueban).
     - Guarda un **Memento** antes de alistar, avisa al **Mediator** para reservar estantería en `ModuloBodega` y pasa el **State** a `EN_ALISTAMIENTO`.
     - Ejecuta `AlistamientoInternacionalAduana.ejecutarProtocoloAlistamiento(...)` (**Template Method**): pesaje común -> contenedor isotérmico -> DEX DIAN -> _Hook_ fitosanitario ICA -> paso final común que transiciona el **State** a `EN_TRANSITO` y notifica al **Mediator**, el cual ordena a `ModuloFlotaTransporte` asignar la aeronave refrigerada `HK-5410-UQ` y a `ModuloFacturacion` emitir el cobro.
   - **Prueba de restricción `State -> Strategy`**: Se intenta cambiar la tarifa cuando el avión ya está `EN_TRANSITO`. `EstadoEnTransito.permiteCambioModalidad()` retorna `false` y bloquea el cambio.
4. **Fase 4 — Contingencia en Ruta y Rollback Automático (`Command` + `State` + `Mediator` + `Memento Undo`)**:
   - Se reporta una tormenta eléctrica mediante `ComandoReportarIncidencia` (**Command**): guarda el **Memento** previo (`EN_TRANSITO`), cambia el **State** a `INCIDENCIA_EN_RUTA`, alerta a los **Observers** y el **Mediator** activa ruta alterna en Flota y congela el cobro en Facturación.
   - Se intenta confirmar la entrega mientras está en incidencia: el patrón **State** (`EstadoIncidenciaRuta`) bloquea la entrega.
   - Superada la emergencia climática, `invoker.deshacerUltimaOperacion()` (**Command Undo**) extrae el último `OrdenMemento` (**Memento**), restaura la orden a `EN_TRANSITO` y el **Mediator** normaliza el vuelo y la facturación.
5. **Fase 5 — Entrega Final en Destino (`Command` -> `Memento` -> `State` -> `Observer` -> `Mediator`)**:
   - Se ejecuta `ComandoConfirmarEntrega` (**Command**), pasando la orden al estado terminal `ENTREGADA` (**State**), notificando a los **Observers** y ordenando vía **Mediator** liberar la aeronave en `ModuloFlotaTransporte` y emitir paz y salvo en `ModuloFacturacion`.
6. **Fase 6 — Caso de Control Negativo (`Chain of Responsibility`)**:
   - Se envía una segunda orden (`UQ-NAC-2026-999`) con cliente `"CLIENTE_REPORTADO_FRAUDE"` y valor de `$85.000.000 COP`. El segundo eslabón (`ValidadorControlAntifraude`) detiene el flujo inmediatamente e impide que pase a bodega o despacho.
