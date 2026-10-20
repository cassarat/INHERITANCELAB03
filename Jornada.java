import java.util.ArrayList;
import java.util.List;

public class Jornada {
    private Cuadrilla cuadrilla;
    private List<OrdenServicio> ordenes;
    private EstadoJornada estado;

    public Jornada(Cuadrilla cuadrilla) {
        if (cuadrilla == null) {
            throw new IllegalArgumentException("La jornada debe tener una cuadrilla asignada.");
        }
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.estado = EstadoJornada.PREPARACION;
    }

    // Calcula sumando las horas estimadas
    public double calcularHorasAsignadas() {
        double total = 0;
        for (OrdenServicio orden : ordenes) {
            total += orden.getHorasEstimadas();
        }
        return total;
    }

    public void asignarOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.PREPARACION) {
            throw new IllegalStateException("Solo se pueden asignar órdenes a jornadas en preparación.");
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden asignar órdenes que estén pendientes.");
        }
        
        //  Capacidad
        if (calcularHorasAsignadas() + orden.getHorasEstimadas() > cuadrilla.getCapacidadHoras()) {
            throw new IllegalArgumentException("La orden excede la capacidad de horas de la cuadrilla.");
        }
        
        //Certificación para Mantenimientos
        if (orden instanceof MantenimientoAC && !cuadrilla.isCertificadaRefrigeracion()) {
            throw new IllegalArgumentException("Mantenimiento de AC requiere cuadrilla certificada en refrigeración.");
        }
        
        ordenes.add(orden);
    }

    public void retirarOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.PREPARACION) {
            throw new IllegalStateException("No se pueden retirar órdenes de una jornada iniciada o cerrada.");
        }
        ordenes.remove(orden);
    }

    public void iniciarJornada() {
        if (ordenes.isEmpty()) {
            throw new IllegalStateException("No puede iniciar una jornada vacía.");
        }
        this.estado = EstadoJornada.INICIADA;
        for (OrdenServicio orden : ordenes) {
            orden.setEstado(EstadoOrden.EN_ATENCION);
            //genera el cargo de Q40 
            orden.registrarCargoVisitaExtra(); 
        }
    }

    public void registrarResultadoVisita(OrdenServicio orden, boolean completada) {
        if (estado != EstadoJornada.INICIADA) {
            throw new IllegalStateException("La jornada debe estar iniciada para registrar resultados.");
        }
        if (!ordenes.contains(orden)) {
            throw new IllegalArgumentException("La orden no pertenece a esta jornada.");
        }
        if (orden.getEstado() != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException("Esta orden ya tiene un resultado registrado o no está en atención.");
        }

        orden.incrementarVisita();

        if (completada) {
            orden.setEstado(EstadoOrden.COMPLETADA);
        } else {
            // Regla de visitas: si falla, regresa a PENDIENTE a menos que alcance el máximo
            if (orden.getVisitasRealizadas() >= orden.getMaxVisitas()) {
                orden.setEstado(EstadoOrden.CANCELADA);
            } else {
                orden.setEstado(EstadoOrden.PENDIENTE); 
            }
        }

        verificarCierreJornada();
    }

    private void verificarCierreJornada() {
        boolean todasConResultado = true;
        for (OrdenServicio orden : ordenes) {
            if (orden.getEstado() == EstadoOrden.EN_ATENCION) {
                todasConResultado = false;
                break;
            }
        }
        // Una jornada queda cerrada cuando todas sus órdenes tienen resultado registrado
        if (todasConResultado) {
            this.estado = EstadoJornada.CERRADA;
        }
    }

    // Getters
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public List<OrdenServicio> getOrdenes() { return ordenes; }
    public EstadoJornada getEstado() { return estado; }
}