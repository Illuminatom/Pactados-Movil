# Ver calendario y filtrar por hábito

Ejecutar la app con Run en Android Studio y abrir **Mi racha → Ver Calendario**.
El icono de tres líneas abre el filtro. Seleccionar un hábito cambia su título,
las marcas de los días y el porcentaje de alarmas pospuestas. El menú indica la
selección actual y se cierra al elegir, pulsar fuera o volver atrás.

Se incluyen Tomar agua, Leer la odisea y Sacar al perro, como en la referencia.
Los datos son ejemplos de agosto (31 días, empezando en domingo), no el historial
real ni el mes actual. Se pueden editar en
`app/src/main/java/com/uniandes/pactados/data/CalendarHabit.kt`.
El porcentaje se calcula sobre los 31 días de ejemplo y se muestra como entero.

## Detalle por día

Cada número abre su detalle en `screens/calendarDay.kt`. El día de la semana,
número y fecha del reporte corresponden al número seleccionado (el 3 es martes,
siguiendo el agosto de la referencia). La flecha, el título Calendario y el botón
Atrás del sistema vuelven al mes conservando el filtro por hábito.
La fecha seleccionada y el filtro sobreviven a la recreación de la pantalla.

Como maqueta, todos los días muestran los progresos de la imagen: Tomar agua 50%,
Sacar al perro 100% y Leer la odisea 0% con alarma cancelada. Estos valores son
ilustrativos y no se calculan a partir de las marcas mensuales ni de alarmas reales.
El detalle muestra los tres hábitos, como en la referencia, independientemente
del filtro mensual. No requiere cambios adicionales en las pantallas de compañeros.

La interfaz está aislada en `screens/calendar.kt`, con previews de la pantalla y
del filtro. No requiere imágenes. Sus colores y componentes son locales.
El desenfoque de Compose se muestra en Android 12 o superior; en versiones
anteriores el menú funciona sobre el fondo sin desenfoque.

Únicos puntos de integración en archivos existentes: callback `onCalendarClick`
del botón en `streak.kt`, y ruta `calendario` en `MainActivity.kt`.
Se conserva la implementación de registros y las demás pantallas.

La prueba `CalendarScreenTest` comprueba que seleccionar hábitos cambia el
calendario y el resumen, y que al reabrir el menú se conserva la selección.
También recorre los 31 números, comprueba el retorno al filtro elegido y verifica
que la fecha y el hábito se conserven al restaurar el estado.
