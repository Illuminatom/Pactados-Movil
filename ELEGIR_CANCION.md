# Elegir canción

Abrir **Crear Alarma** o **Editar Alarma → Elegir Cancion**.
Buscar por título o artista, seleccionar una tarjeta y pulsar **Usar canción**.
La selección aparece en el formulario y se guarda al crear o guardar la alarma.
Volver sin confirmar conserva la canción anterior. Se preservan los datos escritos
en el formulario durante la navegación.

La nueva pantalla está en `screens/chooseSong.kt`. Usa naranja `#ECA052`, crema
`#FCF6EE`, tarjetas redondeadas y selección con borde naranja. Tiene búsqueda,
estado sin resultados y botón de confirmación deshabilitado hasta seleccionar.

El catálogo contiene títulos de ejemplo; no incluye archivos de audio, reproducción
ni conexión a plataformas musicales. Las alarmas se guardan en el repositorio en
memoria que ya utiliza el proyecto, no de manera persistente al cerrar el proceso.

Integración limitada a `MainActivity.kt` (ruta y devolución del resultado) y
`alarmForm.kt` (recibir la canción y conservar el borrador con rememberSaveable).
No se cambia el diseño de los formularios ni el repositorio de los compañeros.

Pruebas: búsqueda, confirmación, cancelación, conservación del formulario y guardado
de una alarma con la canción elegida.
