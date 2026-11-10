import java.util.ArrayList;
import java.util.List;

/**
 * Clase central del dominio unificado (AeroQuindío Smart Logistics).
 * Conecta directamente múltiples patrones de comportamiento:
 * - Es el SUBJECT (Sujeto) en el patrón OBSERVER.
 * - Es el CONTEXT (Contexto) en el patrón STATE.
 * - Es el CONTEXT (Contexto) en el patrón STRATEGY.
 * - Es el ORIGINATOR (Originador) en el patrón MEMENTO.
 * - Es el RECEIVER (Receptor) de las operaciones en el patrón COMMAND.
 */
public class OrdenEnvio {
    private final String codigoGuia;
    private final String cliente;
    private final String destino;
    private final double pesoKg;
    private final double valorDeclaradoCOP;
    private final boolean esInternacional;

    private boolean aprobadaPorAdmision;
    private EstadoOrden estadoActual;
    private EstrategiaTarifa estrategiaTarifa;
    private double costoEnvioCalculado;
    private String vehiculoAsignado;

    // Lista de suscriptores del patrón OBSERVER
    private final List<ObservadorOrden> observadores = new ArrayList<>();

    public OrdenEnvio(String codigoGuia, String cliente, String destino,
                      double pesoKg, double valorDeclaradoCOP,
                      boolean esInternacional, EstrategiaTarifa estrategiaInicial) {
        this.codigoGuia = codigoGuia;
        this.cliente = cliente;
        this.destino = destino;
        this.pesoKg = pesoKg;
        this.valorDeclaradoCOP = valorDeclaradoCOP;
        this.esInternacional = esInternacional;
        this.aprobadaPorAdmision = false;
        this.vehiculoAsignado = "Pendiente de asignación";
        this.estadoActual = new EstadoRegistrada();
        this.estrategiaTarifa = estrategiaInicial;
        this.costoEnvioCalculado = estrategiaInicial.calcularCosto(this);
    }

    // =========================================================================
    // MÉTODOS DEL PATRÓN OBSERVER (Sujeto / Publicador)
    // =========================================================================
    public void suscribir(ObservadorOrden observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(ObservadorOrden observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(String detalleEvento) {
        for (ObservadorOrden obs : observadores) {
            obs.actualizar(this, detalleEvento);
        }
    }

    // =========================================================================
    // MÉTODOS DEL PATRÓN STRATEGY (Cálculo dinámico de tarifa)
    // =========================================================================
    public boolean cambiarEstrategiaTarifa(EstrategiaTarifa nuevaEstrategia) {
        if (!estadoActual.permiteCambioModalidad()) {
            System.out.println("  [STATE -> STRATEGY BLOQUEADO] No se puede cambiar la modalidad de tarifa cuando la orden está en estado: "
                    + estadoActual.getNombreEstado());
            return false;
        }
        this.estrategiaTarifa = nuevaEstrategia;
        this.costoEnvioCalculado = nuevaEstrategia.calcularCosto(this);
        notificarObservadores("Cambio de modalidad a '" + nuevaEstrategia.getNombreModalidad()
                + "' -> Nuevo costo: $" + Formato.pesos(costoEnvioCalculado) + " COP");
        return true;
    }

    public void recalcularTarifa() {
        this.costoEnvioCalculado = this.estrategiaTarifa.calcularCosto(this);
    }

    // =========================================================================
    // MÉTODOS DEL PATRÓN STATE (Ciclo de vida del envío)
    // =========================================================================
    public void avanzarEstado() {
        this.estadoActual.avanzar(this);
    }

    public void reportarIncidencia(String motivo) {
        this.estadoActual.reportarIncidencia(this, motivo);
    }

    public void cambiarEstadoInterno(EstadoOrden nuevoEstado, String motivoTransicion) {
        String estadoAnterior = this.estadoActual.getNombreEstado();
        this.estadoActual = nuevoEstado;
        notificarObservadores("Transición de estado [" + estadoAnterior + " -> "
                + nuevoEstado.getNombreEstado() + "] (" + motivoTransicion + ")");
    }

    // =========================================================================
    // MÉTODOS DEL PATRÓN MEMENTO (Originator: Crear y Restaurar Snapshot)
    // =========================================================================
    public OrdenMemento crearMemento(String motivoSnapshot) {
        return new OrdenMemento(
                this.codigoGuia,
                this.estadoActual,
                this.estrategiaTarifa,
                this.costoEnvioCalculado,
                this.vehiculoAsignado,
                this.aprobadaPorAdmision,
                motivoSnapshot
        );
    }

    public void restaurarDesdeMemento(OrdenMemento memento) {
        this.estadoActual = memento.getEstadoGuardado();
        this.estrategiaTarifa = memento.getEstrategiaGuardada();
        this.costoEnvioCalculado = memento.getCostoGuardado();
        this.vehiculoAsignado = memento.getVehiculoGuardado();
        this.aprobadaPorAdmision = memento.isAprobadaGuardada();
        notificarObservadores("ROLLBACK (Memento restaurado): Estado='" + estadoActual.getNombreEstado()
                + "', Modalidad='" + estrategiaTarifa.getNombreModalidad()
                + "', Costo=$" + Formato.pesos(costoEnvioCalculado) + " COP");
    }

    // =========================================================================
    // GETTERS Y SETTERS DE APOYO AL DOMINIO
    // =========================================================================
    public String getCodigoGuia() { return codigoGuia; }
    public String getCliente() { return cliente; }
    public String getDestino() { return destino; }
    public double getPesoKg() { return pesoKg; }
    public double getValorDeclaradoCOP() { return valorDeclaradoCOP; }
    public boolean isEsInternacional() { return esInternacional; }
    public boolean isAprobadaPorAdmision() { return aprobadaPorAdmision; }
    public void setAprobadaPorAdmision(boolean aprobadaPorAdmision) { this.aprobadaPorAdmision = aprobadaPorAdmision; }
    public EstadoOrden getEstadoActual() { return estadoActual; }
    public EstrategiaTarifa getEstrategiaTarifa() { return estrategiaTarifa; }
    public double getCostoEnvioCalculado() { return costoEnvioCalculado; }
    public String getVehiculoAsignado() { return vehiculoAsignado; }
    public void setVehiculoAsignado(String vehiculoAsignado) { this.vehiculoAsignado = vehiculoAsignado; }

    public void mostrarFichaResumen() {
        System.out.println("  +-----------------------------------------------------------------------+");
        System.out.println("  | FICHA DE ORDEN: " + codigoGuia + " (" + (esInternacional ? "INTERNACIONAL" : "NACIONAL") + ")");
        System.out.println("  | Cliente:        " + cliente + " -> Destino: " + destino);
        System.out.println("  | Peso / Valor:   " + pesoKg + " kg | Valor declarado: $" + Formato.pesos(valorDeclaradoCOP) + " COP");
        System.out.println("  | Estado actual:  " + estadoActual.getNombreEstado());
        System.out.println("  | Modalidad:      " + estrategiaTarifa.getNombreModalidad() + " (" + estrategiaTarifa.getTiempoEstimado() + ")");
        System.out.println("  | Costo Flete:    $" + Formato.pesos(costoEnvioCalculado) + " COP");
        System.out.println("  | Unidad Flota:   " + vehiculoAsignado);
        System.out.println("  +-----------------------------------------------------------------------+");
    }
}
