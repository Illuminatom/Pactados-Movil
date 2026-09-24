# Ver registros

Implementación independiente en `app/src/main/java/com/uniandes/pactados/screens/records.kt`.
La integración modifica únicamente el callback de Ver Registros en `streak.kt` y
añade su destino en `MainActivity.kt`. El diseño de las otras pantallas no cambia.

## Ver la pantalla

Abrir `records.kt` en Android Studio y seleccionar **Split** o **Design**. Las previews
`Mis registros · referencia` y `Mis registros · teléfono` muestran la pantalla.
Para verla en el emulador, ejecutar de nuevo la app con **Run ▶**, entrar a
**Mi racha** y pulsar **Ver Registros**.

## Imágenes de ejemplo

Copiar las fotos PNG, JPG o WebP a **`app/src/main/res/drawable/`** con nombres en
minúsculas, sin espacios ni tildes: `registro_01.jpg`, `registro_02.jpg`, etc.
Preferiblemente usar fotos cuadradas de al menos 512 × 512 px. Se recortan al centro.
No es necesario subirlas a un servicio externo.

Las cuatro imágenes añadidas ya están asociadas a sus títulos en `records.kt`:

| Archivo | Título |
| --- | --- |
| `tomar_agua.png` | Tomar Agua |
| `leer_libro.png` | Leer Libro |
| `sacar_perro.png` | Sacar Perro |
| `ir_gimnasio.png` | Ir al Gimnasio |

Se renombraron los PNG a minúsculas con guiones bajos para cumplir las reglas de
recursos Android. El último registro muestra Tomar Agua. Las seis miniaturas usan
las cuatro fotos y repiten Leer Libro y Sacar Perro para mantener la referencia.

Para personalizar otros ejemplos, se pueden pasar los recursos de esta forma:

```kotlin
latestRecord: RecordExample = RecordExample(imageRes = R.drawable.registro_01),
previousRecords: List<RecordExample> = listOf(
    RecordExample(imageRes = R.drawable.registro_02),
    RecordExample(imageRes = R.drawable.registro_03),
    RecordExample(imageRes = R.drawable.registro_04),
    RecordExample(imageRes = R.drawable.registro_05),
    RecordExample(imageRes = R.drawable.registro_06),
    RecordExample(imageRes = R.drawable.registro_07)
)
```

`records_placeholder.xml` queda disponible como imagen alternativa de la referencia.
Los textos son ejemplos; no hay conexión a almacenamiento ni audio.

## Conexión de navegación

La conexión ya está aplicada:

1. En `StreakScreen`, se añadió el parámetro `onRecordsClick: () -> Unit = {}` y
   se reemplazó el TODO del botón `Ver Registros` por `onClick = onRecordsClick`.
2. En `MainActivity`, se pasa `onRecordsClick = { navController.navigate("mis_registros") }`
   a `StreakScreen`.
3. Se importó `RecordsScreen` y se añadió el destino `mis_registros` al `NavHost`.
   La flecha regresa a la pantalla anterior y Mi racha reutiliza su destino en la pila.
   Ejemplo básico del destino:

```kotlin
composable("mis_registros") {
    RecordsScreen(
        onBackClick = { navController.popBackStack() },
        onProfileClick = { navController.navigate("profile") },
        onHomeClick = { navController.navigate("home") },
        onStreakClick = { navController.navigate("streak") }
    )
}
```

## Referencia visual

Diseño basado en la captura de 295 × 664: encabezado naranja redondeado, título en
dos líneas, imagen principal de 148 × 148, cuadrícula de cuatro columnas y barra inferior.
Las medidas escalan con el ancho; el contenido tiene desplazamiento en pantallas bajas
y respeta la escala de texto del sistema. Los colores son locales a esta pantalla.
La coincidencia exacta de píxeles requiere comparar una captura renderizada en el mismo
tamaño y disponer de la tipografía original del diseño.
