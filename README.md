LABORATORIO #3 HERENCIA
El menú debe permitir:
Cotizar y registrar órdenes de cualquier categoría.
Registrar cuadrillas.
Crear jornadas, asignar y retirar órdenes, iniciar una jornada y registrar los resultados de las
visitas.
Consultar órdenes y cuadrillas.
Generar los reportes descritos.
El menú debe permanecer disponible hasta que el usuario decida salir.
El programa debe iniciar con estos datos, todos sin ingresos registrados:
Al menos dos órdenes de cada categoría, pendientes.
Al menos tres cuadrillas, de las cuales al menos una certificada en refrigeración y al menos una
no certificada.
Elija datos que permitan observar todas las reglas de cobro, incluyendo una instalación cuyas
horas por puntos superen sus horas estimadas.
La aplicación debe rechazar con un mensaje claro, y sin modificar el estado previo, cualquier
operación inválida. Esto incluye:
Valores numéricos no positivos o fuera de rango.
Códigos vacíos o repetidos.
Un diagnóstico de más de 1.5 horas o un mantenimiento de aire acondicionado en la zona
periférica.
Asignaciones que excedan la capacidad o que no sean compatibles con la cuadrilla.
Operaciones sobre órdenes o jornadas en un estado que no las permite.
Las entradas con formato incorrecto no deben terminar el programa. Los montos se muestran con
dos decimales. Los datos solo se conservan enmemoria;noserequieren archivos de datos, bases
de datos ni interfaz gráfica.