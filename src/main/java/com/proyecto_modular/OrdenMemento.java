import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PATRÓN MEMENTO (Bloque Investigativo #3) - Rol: Memento (Instantánea Inmutable).
 * Almacena una copia encapsulada del estado interno de una OrdenEnvio en un instante dado,
 * permitiendo restaurarla posteriormente sin exponer ni romper el encapsulamiento.
 */
public class OrdenMemento {
    private final String codigoGuia;
    private final EstadoOrden estadoGuardado;
    private final EstrategiaTarifa estrategiaGuardada;
    private final double costoGuardado;
    private final String vehiculoGuardado;
    private final boolean aprobadaGuardada;
    private final String motivoSnapshot;
    private final String fechaHoraSnapshot;

    public OrdenMemento(String codigoGuia,
                        EstadoOrden estadoGuardado,
                        EstrategiaTarifa estrategiaGuardada,
                        double costoGuardado,
                        String vehiculoGuardado,
                        boolean aprobadaGuardada,
                        String motivoSnapshot) {
        this.codigoGuia = codigoGuia;
        this.estadoGuardado = estadoGuardado;
        this.estrategiaGuardada = estrategiaGuardada;
        this.costoGuardado = costoGuardado;
        this.vehiculoGuardado = vehiculoGuardado;
        this.aprobadaGuardada = aprobadaGuardada;
        this.motivoSnapshot = motivoSnapshot;
        this.fechaHoraSnapshot = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getCodigoGuia() { return codigoGuia; }
    public EstadoOrden getEstadoGuardado() { return estadoGuardado; }
    public EstrategiaTarifa getEstrategiaGuardada() { return estrategiaGuardada; }
    public double getCostoGuardado() { return costoGuardado; }
    public String getVehiculoGuardado() { return vehiculoGuardado; }
    public boolean isAprobadaGuardada() { return aprobadaGuardada; }
    public String getMotivoSnapshot() { return motivoSnapshot; }
    public String getFechaHoraSnapshot() { return fechaHoraSnapshot; }

    @Override
    public String toString() {
        return "Memento[" + codigoGuia + " | Hora: " + fechaHoraSnapshot
                + " | Motivo: '" + motivoSnapshot + "' | Estado: " + estadoGuardado.getNombreEstado()
                + " | Modalidad: " + estrategiaGuardada.getNombreModalidad()
                + " | Costo: $" + Formato.pesos(costoGuardado) + "]";
    }
}
