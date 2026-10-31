# Laboratorio 06: Uso de Menús en una Aplicación Android

## I. Objetivo

Introducir el uso de menús en Jetpack Compose y construir una plantilla básica con `Scaffold`.

## II. Seguridad

- No ingresar con líquidos ni comida al aula de laboratorio.
- Al culminar la sesión, apagar correctamente la computadora y la pantalla, y ordenar las sillas utilizadas.

## III. Equipos y materiales

- Sistema operativo Windows 10 o superior con conexión a la red del laboratorio.
- Android Studio u otro entorno donde se puedan ejecutar aplicaciones móviles con Kotlin y Jetpack Compose.

## Desarrollo — Parte 1: ejercicio guiado

### Elementos de Scaffold

- **Top app bar:** barra superior con título, botón para abrir el Drawer y menú de opciones.
- **Perfil:** el icono `Icons.Outlined.AccountCircle` abre una vista de perfil de demostración; la flecha superior regresa a la sección anterior.
- **Content:** contenido principal, recibido mediante el parámetro `padding` del Scaffold.
- **FAB:** botón flotante que incrementa el contador de ejemplo.
- **Bottom bar:** barra inferior con navegación entre Inicio, Herramientas y Acerca de.
- **Drawer:** menú lateral expandible con acceso a las mismas secciones.

Estos elementos son opcionales y personalizables. La plantilla implementa cada elemento como parte de `Laboratorio06App` en `app/src/main/java/com/example/lab06/MainActivity.kt`.

La barra superior está definida aparte en la función `CustomTopBar(...)`, dentro del mismo archivo. Esta función usa `TopAppBar` y recibe acciones como parámetros para abrir el Drawer, buscar, mostrar el Perfil y regresar.

La barra inferior también está definida aparte en `CustomBottomBar()` en ese archivo. Usa `BottomAppBar` y una `Row` que ocupa todo el ancho con `Arrangement.SpaceEvenly`, para distribuir uniformemente cuatro iconos personalizados. Cada botón navega a una vista: Personalización, Explorar, Favoritos o Papelera.

La función que organiza todo se llama `CustomScaffold()`, como en el ejemplo. Dentro de ella están los parámetros `topBar`, `bottomBar`, `floatingActionButton` y `content`; el contenido recibe el `padding` del Scaffold y se pasa a `CustomContent(...)`.

### Vista de perfil y navegación

La acción de perfil guarda la sección activa antes de abrir `UserProfileContent`. Cuando se muestra Perfil, el icono de navegación cambia a una flecha de regreso que restaura la sección anterior. Los datos de nombre y correo son valores de muestra.

### Función que define el Scaffold

```kotlin
@Composable
fun Laboratorio06App() {
    ModalNavigationDrawer(
        drawerContent = { /* elementos del menú lateral */ }
    ) {
        Scaffold(
            topBar = { /* barra superior */ },
            bottomBar = { /* navegación inferior */ },
            floatingActionButton = { /* botón flotante */ }
        ) { padding ->
            LabContent(
                section = selectedSection,
                clicks = clicks,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
```

`topBar` presenta las acciones globales; `bottomBar` facilita cambiar de sección; `floatingActionButton` destaca la acción principal; y el bloque de contenido recibe `PaddingValues` para evitar que quede debajo de las barras.

### Ejecución

Abrir en Android Studio la carpeta `PlantillaScaffold`, esperar la sincronización de Gradle y ejecutar la configuración `app` en un emulador o dispositivo Android.
