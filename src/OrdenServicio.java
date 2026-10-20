public abstract class OrdenServicio {
    protected String codigo;
    protected String cliente;
    protected String direccion;
    protected double horasEstimadas;
    protected Zona zona;
    protected EstadoOrden estado;
    protected int visitasRealizadas;
    protected double ingresosPorVisitaExtra; // Guarda los Q40 acumulados

    public OrdenServicio(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas estimadas deben ser positivas.");
        }
        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE; // Toda orden inicia como pendiente
        this.visitasRealizadas = 0;
        this.ingresosPorVisitaExtra = 0.0;
    }

    //Polimorfismo
    public abstract double calcularCostoBase();
    public abstract int calcularTiempoDias();
    public abstract int getMaxVisitas();

    // Costo total = costo del servicio + cobros por visitas adicionales
    public double calcularCostoTotal() {
        return calcularCostoBase() + ingresosPorVisitaExtra;
    }

    //  inicia la jornada, cobrando Q40 si es la 2da visita o superior
    public void registrarCargoVisitaExtra() {
        if (this.visitasRealizadas >= 1) {
            this.ingresosPorVisitaExtra += 40.0;
        }
    }

    // Getters  necesarios para las validaciones y reportes
    public String getCodigo() { return codigo; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public EstadoOrden getEstado() { return estado; }
    public double getIngresosPorVisitaExtra() { return ingresosPorVisitaExtra; }
    
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    
    public void incrementarVisita() {
        this.visitasRealizadas++;
    }
    
    public int getVisitasRealizadas() { return visitasRealizadas; }
}
