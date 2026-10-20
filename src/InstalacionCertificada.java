public class InstalacionCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionCertificada(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int puntos, double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);
        if (valorMateriales <= 0) {
            throw new IllegalArgumentException("El valor de los materiales debe ser un número positivo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCostoBase() {
        // herencia para no duplicar la lógica de los puntos
        double costoBaseInstalacion = super.calcularCostoBase();
        return costoBaseInstalacion + 120.0 + (valorMateriales * 0.05);
    }

    @Override
    public int calcularTiempoDias() {
        return super.calcularTiempoDias() + 2;
    }

    @Override
    public int getMaxVisitas() {
        return 2; //  solo permite 2 visitas como max
    }
}
