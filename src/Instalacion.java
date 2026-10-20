public class Instalacion extends OrdenServicio {
    protected int puntos;

    public Instalacion(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int puntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos a instalar debe ser un entero positivo.");
        }
        this.puntos = puntos;
    }

    @Override
    public double calcularCostoBase() {
        double tarifaBase = (zona == Zona.METROPOLITANA) ? 100.0 : 175.0;
        double horasPorPuntos = puntos * 0.75;
        double horasFacturables = Math.max(horasEstimadas, horasPorPuntos);
        
        // Se cobra Q90 por cada hora o fracción
        int horasACobrar = (int) Math.ceil(horasFacturables); 
        return tarifaBase + (horasACobrar * 90.0);
    }

    @Override
    public int calcularTiempoDias() {
        return (zona == Zona.METROPOLITANA) ? 3 : 5;
    }

    @Override
    public int getMaxVisitas() {
        return 3;
    }
}