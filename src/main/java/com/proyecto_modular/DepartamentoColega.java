/**
 * PATRÓN MEDIATOR (Bloque Base #4) - Rol: Colleague (Clase Abstracta Colega).
 * Representa un departamento de la organización que se comunica exclusivamente a través del Mediador.
 */
public abstract class DepartamentoColega {
    protected MediadorLogistico mediador;
    protected final String nombreDepartamento;

    public DepartamentoColega(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public void setMediador(MediadorLogistico mediador) {
        this.mediador = mediador;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }
}
