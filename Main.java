import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<OrdenServicio> ordenes = new ArrayList<>();
    private static List<Cuadrilla> cuadrillas = new ArrayList<>();
    private static List<Jornada> jornadas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarDatos();
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- TECNIHOGAR: SISTEMA PRINCIPAL ---");
            System.out.println("1. Cotizar u Ordenar Servicio");
            System.out.println("2. Registrar Nueva Cuadrilla");
            System.out.println("3. Gestión de Jornadas (Crear, Asignar, Iniciar, Visitas)");
            System.out.println("4. Consultar Órdenes y Cuadrillas");
            System.out.println("5. Generar Reportes de Gerencia");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1: menuCotizarRegistrar(); break;
                    case 2: registrarCuadrilla(); break;
                    case 3: menuJornadas(); break;
                    case 4: consultarListados(); break;
                    case 5: generarReportes(); break;
                    case 6: salir = true; System.out.println("Saliendo del sistema..."); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void inicializarDatos() {
        // 3 Cuadrillas 
        cuadrillas.add(new Cuadrilla("C01", 10.0, true));
        cuadrillas.add(new Cuadrilla("C02", 8.0, false));
        cuadrillas.add(new Cuadrilla("C03", 12.0, true));

        // Diagnósticos
        ordenes.add(new Diagnostico("D01", "Juan Perez", "Zona 1", 1.0, Zona.METROPOLITANA));
        ordenes.add(new Diagnostico("D02", "Ana Lopez", "Mixco", 1.5, Zona.PERIFERICA));

        // 2 Instalaciones 
        ordenes.add(new Instalacion("I01", "Luis", "Zona 10", 2.0, Zona.METROPOLITANA, 5));
        ordenes.add(new Instalacion("I02", "Maria", "Villa Nueva", 4.0, Zona.PERIFERICA, 2));

        // 2 Instalaciones Certificadas
        ordenes.add(new InstalacionCertificada("IC01", "Carlos", "Zona 14", 3.0, Zona.METROPOLITANA, 4, 1500));
        ordenes.add(new InstalacionCertificada("IC02", "Sofia", "Carretera ES", 5.0, Zona.PERIFERICA, 6, 2000));

        // 2 Mantenimientos de AC
        ordenes.add(new MantenimientoAC("M01", "Empresa A", "Zona 4", 2.0, Zona.METROPOLITANA, 12000));
        ordenes.add(new MantenimientoAC("M02", "Empresa B", "Zona 9", 3.5, Zona.METROPOLITANA, 24000));
    }

    private static void menuCotizarRegistrar() {
        try {
            System.out.println("\n-- COTIZAR / REGISTRAR ORDEN --");
            System.out.print("Código: "); String cod = scanner.nextLine();
            
            // Validar código único
            for (OrdenServicio o : ordenes) {
                if (o.getCodigo().equalsIgnoreCase(cod)) {
                    throw new IllegalArgumentException("El código ya existe.");
                }
            }

            System.out.print("Cliente: "); String cliente = scanner.nextLine();
            System.out.print("Dirección: "); String direccion = scanner.nextLine();
            System.out.print("Horas Estimadas: "); double horas = Double.parseDouble(scanner.nextLine());
            System.out.print("Zona (1. Metro, 2. Periferica): "); 
            Zona zona = scanner.nextLine().equals("1") ? Zona.METROPOLITANA : Zona.PERIFERICA;

            System.out.println("Categorías: 1. Diagnóstico | 2. Instalación | 3. Inst. Certificada | 4. Mantenimiento AC");
            System.out.print("Seleccione categoría: ");
            int cat = Integer.parseInt(scanner.nextLine());

            OrdenServicio nuevaOrden = null;

            switch (cat) {
                case 1:
                    nuevaOrden = new Diagnostico(cod, cliente, direccion, horas, zona);
                    break;
                case 2:
                    System.out.print("Puntos a instalar: "); int puntos = Integer.parseInt(scanner.nextLine());
                    nuevaOrden = new Instalacion(cod, cliente, direccion, horas, zona, puntos);
                    break;
                case 3:
                    System.out.print("Puntos a instalar: "); int puntosCert = Integer.parseInt(scanner.nextLine());
                    System.out.print("Valor de materiales: "); double mat = Double.parseDouble(scanner.nextLine());
                    nuevaOrden = new InstalacionCertificada(cod, cliente, direccion, horas, zona, puntosCert, mat);
                    break;
                case 4:
                    System.out.print("Capacidad en BTU: "); int btu = Integer.parseInt(scanner.nextLine());
                    nuevaOrden = new MantenimientoAC(cod, cliente, direccion, horas, zona, btu);
                    break;
                default:
                    throw new IllegalArgumentException("Categoría inválida.");
            }

            // Cotización
            System.out.printf("\n--- COTIZACIÓN ---\nCosto Total: Q%.2f\nTiempo Estimado: %d días\n", 
                              nuevaOrden.calcularCostoTotal(), nuevaOrden.calcularTiempoDias());
            System.out.print("¿Desea registrarla formalmente? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                ordenes.add(nuevaOrden);
                System.out.println("Orden registrada exitosamente y el cobro se ha realizado.");
            } else {
                System.out.println("Cotización descartada. No se modificaron los ingresos.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error de formato en los datos ingresados.");
        }
    }

    private static void registrarCuadrilla() {
        try {
            System.out.print("\nCódigo Cuadrilla: "); String cod = scanner.nextLine();
            System.out.print("Capacidad de Horas: "); double cap = Double.parseDouble(scanner.nextLine());
            System.out.print("¿Certificada en Refrigeración? (S/N): "); 
            boolean cert = scanner.nextLine().equalsIgnoreCase("S");
            
            cuadrillas.add(new Cuadrilla(cod, cap, cert));
            System.out.println("Cuadrilla registrada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar cuadrilla: " + e.getMessage());
        }
    }

    private static void menuJornadas() {
        System.out.println("\n-- GESTIÓN DE JORNADAS --");
        System.out.println("1. Crear Jornada nueva");
        System.out.println("2. Iniciar una Jornada existente");
        System.out.println("3. Registrar resultado de visita en Jornada iniciada");
        System.out.print("Opción: ");
        
        try {
            int op = Integer.parseInt(scanner.nextLine());
            if (op == 1) {
                System.out.print("Ingrese código de Cuadrilla: "); String codC = scanner.nextLine();
                Cuadrilla c = cuadrillas.stream().filter(x -> x.getCodigo().equals(codC)).findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Cuadrilla no encontrada."));
                
                // Validar que la cuadrilla no tenga una jornada sin cerrar
                for (Jornada j : jornadas) {
                    if (j.getCuadrilla().getCodigo().equals(codC) && j.getEstado() != EstadoJornada.CERRADA) {
                        throw new IllegalStateException("Esta cuadrilla ya tiene una jornada sin cerrar.");
                    }
                }

                Jornada nueva = new Jornada(c);
                jornadas.add(nueva);
                
                boolean asignando = true;
                while (asignando) {
                    System.out.print("Ingrese codigo de orden a asignar o FIN para terminar ");
                    String codO = scanner.nextLine();
                    if (codO.equalsIgnoreCase("FIN")) break;

                    OrdenServicio o = ordenes.stream().filter(x -> x.getCodigo().equals(codO)).findFirst()
                                        .orElse(null);
                    if (o != null) {
                        try {
                            nueva.asignarOrden(o);
                            System.out.println("Orden asignada, capacidad usada: " + nueva.calcularHorasAsignadas() + " / " + c.getCapacidadHoras());
                        } catch (Exception e) {
                            System.out.println("No se pudo asignar: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Orden no encontrada.");
                    }
                }
            } else if (op == 2) {
                // Simplificación para iniciar la primera en preparación
                Jornada jprep = jornadas.stream().filter(j -> j.getEstado() == EstadoJornada.PREPARACION).findFirst()
                                .orElseThrow(() -> new IllegalStateException("No hay jornadas en preparación."));
                jprep.iniciarJornada();
                System.out.println("Jornada de la cuadrilla " + jprep.getCuadrilla().getCodigo() + " INICIADA. Las órdenes están en atención.");
            } else if (op == 3) {
                Jornada jini = jornadas.stream().filter(j -> j.getEstado() == EstadoJornada.INICIADA).findFirst()
                                .orElseThrow(() -> new IllegalStateException("No hay jornadas iniciadas."));
                System.out.print("Código de la orden atendida: "); String codO = scanner.nextLine();
                OrdenServicio o = jini.getOrdenes().stream().filter(x -> x.getCodigo().equals(codO)).findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Orden no está en esta jornada."));
                
                System.out.print("Visita Completada exitosamente?: ");
                boolean exitosa = scanner.nextLine().equalsIgnoreCase("S");
                jini.registrarResultadoVisita(o, exitosa);
                System.out.println("Resultado registrado. Nuevo estado de la orden: " + o.getEstado());
                if (jini.getEstado() == EstadoJornada.CERRADA) {
                    System.out.println("Todas las órdenes han sido reportadas. La Jornada ha sido CERRADA");
                }
            }
        } catch (Exception e) {
            System.out.println("Error en gestión de jornadas: " + e.getMessage());
        }
    }

    private static void consultarListados() {
        System.out.println("\n--- LISTADO DE ÓRDENES ---");
        for (OrdenServicio o : ordenes) {
            System.out.printf("[%s] - %s | Costo Base: Q%.2f | Estado: %s | Visitas: %d/%d\n", 
                              o.getCodigo(), o.getClass().getSimpleName(), o.calcularCostoBase(), 
                              o.getEstado(), o.getVisitasRealizadas(), o.getMaxVisitas());
        }
    }

    private static void generarReportes() {
        System.out.println("\n=== REPORTES DE GERENCIA ===");

        // Órdenes por Categoría y Estado
        System.out.println("\n[1] Órdenes por Categoría y Estado:");
        for (OrdenServicio o : ordenes) {
            System.out.println(" - " + o.getClass().getSimpleName() + " (" + o.getCodigo() + "): " + o.getEstado());
        }

        // Ingresos
        double ingresoRegistro = 0, ingresoVisitas = 0;
        double ingresoDiag = 0, ingresoInst = 0, ingresoCert = 0, ingresoAC = 0;

        for (OrdenServicio o : ordenes) {
            double base = o.calcularCostoBase();
            double extra = o.getIngresosPorVisitaExtra();
            
            ingresoRegistro += base;
            ingresoVisitas += extra;

            if (o instanceof Diagnostico) ingresoDiag += (base + extra);
            else if (o instanceof InstalacionCertificada) ingresoCert += (base + extra);
            else if (o instanceof Instalacion) ingresoInst += (base + extra);
            else if (o instanceof MantenimientoAC) ingresoAC += (base + extra);
        }

        System.out.println("\n[2] Ingreso Total: Q" + String.format("%.2f", ingresoRegistro + ingresoVisitas));
        System.out.println("    -> Ingresos por Registro: Q" + String.format("%.2f", ingresoRegistro));
        System.out.println("    -> Ingresos por Visitas Extra: Q" + String.format("%.2f", ingresoVisitas));
        System.out.println("\n    Ingresos por Categoría:");
        System.out.println("    - Diagnósticos: Q" + String.format("%.2f", ingresoDiag));
        System.out.println("    - Instalaciones: Q" + String.format("%.2f", ingresoInst));
        System.out.println("    - Instalaciones Certificadas: Q" + String.format("%.2f", ingresoCert));
        System.out.println("    - Mantenimiento AC: Q" + String.format("%.2f", ingresoAC));

        // Jornadas no cerradas
        System.out.println("\n[3] Jornadas no cerradas:");
        boolean hayAbiertas = false;
        for (Jornada j : jornadas) {
            if (j.getEstado() != EstadoJornada.CERRADA) {
                hayAbiertas = true;
                double horasAsignadas = j.calcularHorasAsignadas();
                double capMax = j.getCuadrilla().getCapacidadHoras();
                System.out.printf(" - Cuadrilla %s | Estado: %s | Horas asignadas: %.2f | Capacidad disp: %.2f\n", 
                                  j.getCuadrilla().getCodigo(), j.getEstado(), horasAsignadas, (capMax - horasAsignadas));
            }
        }
        if (!hayAbiertas) System.out.println(" - No hay jornadas pendientes por cerrar.");
    }
}