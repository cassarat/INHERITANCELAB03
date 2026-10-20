public class MantenimientoAC extends OrdenServicio {
    private int capacidadBTU;

    public MantenimientoAC(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int capacidadBTU) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("Solo se aceptan mantenimientos de aire acondicionado en la zona metropolitana.");
        }
        if (capacidadBTU < 9000 || capacidadBTU > 60000) {
            throw new IllegalArgumentException("La capacidad del equipo debe estar entre 9,000 y 60,000 BTU.");
        }
        this.capacidadBTU = capacidadBTU;
    }

    @Override
    public double calcularCostoBase() {
        double tarifaBase = 100.0; // Tarifa base para la zona metro
        return tarifaBase + (horasEstimadas * 110.0) + 80.0; // Q80 de gas refrigerante
    }

    @Override
    public int calcularTiempoDias() {
        return 2;
    }

    @Override
    public int getMaxVisitas() {
        return 1; // Solo permite 1 visita por el gas
    }
}
