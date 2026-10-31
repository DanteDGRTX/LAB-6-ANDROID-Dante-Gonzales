package com.example.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Laboratorio06App() } }
    }
}

@Composable
fun Laboratorio06App() {
    val sections = listOf("Inicio", "Herramientas", "Favoritos", "Acerca de")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Build, Icons.Filled.Favorite, Icons.Filled.Info)
    var selectedSection by remember { mutableStateOf(sections.first()) }
    var previousSection by remember { mutableStateOf(sections.first()) }
    var clicks by remember { mutableIntStateOf(0) }
    val drawerState = androidx.compose.material3.rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scaffoldBindings = LabScaffoldBindings(
        section = selectedSection,
        clicks = clicks,
        onMenuClick = { scope.launch { drawerState.open() } },
        onBackClick = { selectedSection = previousSection },
        onSearchClick = { selectedSection = "Herramientas" },
        onProfileClick = {
            previousSection = selectedSection
            selectedSection = "Perfil"
        },
        onNavigate = { selectedSection = it },
        onIncrement = { clicks++ }
    )

    CompositionLocalProvider(LocalLabScaffoldBindings provides scaffoldBindings) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = "Laboratorio 06",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(24.dp)
                    )
                    HorizontalDivider()
                    sections.forEachIndexed { index, section ->
                        androidx.compose.material3.NavigationDrawerItem(
                            label = { Text(section) },
                            selected = selectedSection == section,
                            icon = { Icon(icons[index], contentDescription = null) },
                            onClick = {
                                selectedSection = section
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                }
            }
        ) {
            CustomScaffold()
        }
    }
}

private data class LabScaffoldBindings(
    val section: String,
    val clicks: Int,
    val onMenuClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onSearchClick: () -> Unit,
    val onProfileClick: () -> Unit,
    val onNavigate: (String) -> Unit,
    val onIncrement: () -> Unit
)

private val LocalLabScaffoldBindings = staticCompositionLocalOf<LabScaffoldBindings> {
    error("LabScaffoldBindings no fue proporcionado")
}

@Composable
fun CustomScaffold() {
    val bindings = LocalLabScaffoldBindings.current
    Scaffold(
        // Barra superior
        topBar = {
            CustomTopBar(
                title = bindings.section,
                showBackButton = bindings.section == "Perfil",
                onMenuClick = bindings.onMenuClick,
                onBackClick = bindings.onBackClick,
                onSearchClick = bindings.onSearchClick,
                onProfileClick = bindings.onProfileClick,
                onNavigate = bindings.onNavigate
            )
        },

        // Barra inferior
        bottomBar = { CustomBottomBar() },

        // Botón flotante personalizado
        floatingActionButton = { CustomFAB() },

        // Contenido principal
        content = { padding ->
            CustomContent(padding)
        }
    )
}

@Composable
fun CustomFAB() {
    val bindings = LocalLabScaffoldBindings.current
    FloatingActionButton(onClick = bindings.onIncrement) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Add, contentDescription = "Incrementar contador")
            Spacer(Modifier.width(8.dp))
            Text("Sumar")
        }
    }
}

/** Barra inferior del Scaffold siguiendo el bloque de código del laboratorio. */
@Composable
fun CustomBottomBar() {
    val bindings = LocalLabScaffoldBindings.current
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { bindings.onNavigate("Personalizar") }) {
                Icon(Icons.Filled.Tune, contentDescription = "Abrir personalización")
            }
            IconButton(onClick = { bindings.onNavigate("Explorar") }) {
                Icon(
                    Icons.Filled.Explore,
                    contentDescription = "Abrir exploración",
                )
            }
            IconButton(onClick = { bindings.onNavigate("Favoritos") }) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Abrir favoritos",
                )
            }
            IconButton(onClick = { bindings.onNavigate("Papelera") }) {
                Icon(
                    Icons.Filled.DeleteForever,
                    contentDescription = "Abrir papelera",
                )
            }
        }
    }
}

/** Barra superior del Scaffold: menú lateral, título, búsqueda y acceso al perfil. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    showBackButton: Boolean,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = if (showBackButton) onBackClick else onMenuClick) {
                Icon(
                    imageVector = if (showBackButton) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu,
                    contentDescription = if (showBackButton) "Volver" else "Abrir menú lateral"
                )
            }
        },
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Buscar")
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Abrir perfil de usuario"
                )
            }
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Inicio") },
                    onClick = { onNavigate("Inicio"); menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Acerca de") },
                    onClick = { onNavigate("Acerca de"); menuExpanded = false }
                )
            }
        }
    )
}

@Composable
private fun UserProfileContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Avatar del usuario",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp).height(88.dp).width(88.dp)
        )
        Text("Perfil de usuario", style = MaterialTheme.typography.headlineSmall)
        Text("Información de ejemplo", style = MaterialTheme.typography.bodyMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Nombre", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("Usuario de prueba", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider()
                Text("Correo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("usuario@ejemplo.com", style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider()
                Text("Tipo de cuenta", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("Cuenta local de demostración", style = MaterialTheme.typography.bodyLarge)
            }
        }
        Text("Toca la flecha de la barra superior para regresar.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CustomContent(padding: PaddingValues) {
    val bindings = LocalLabScaffoldBindings.current
    val section = bindings.section
    val clicks = bindings.clicks
    if (section == "Perfil") {
        UserProfileContent(modifier = Modifier.padding(padding))
        return
    }

    when (section) {
        "Personalizar" -> {
            DestinationScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Filled.Tune,
                title = "Personalización",
                description = "Aquí puedes encontrar opciones para personalizar la aplicación."
            )
            return
        }
        "Explorar" -> {
            DestinationScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Filled.Explore,
                title = "Explorar",
                description = "Esta vista reúne accesos y secciones para explorar."
            )
            return
        }
        "Favoritos" -> {
            DestinationScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Filled.Star,
                title = "Favoritos",
                description = "Aquí se mostrarán los elementos que marques como favoritos."
            )
            return
        }
        "Papelera" -> {
            DestinationScreen(
                modifier = Modifier.padding(padding),
                icon = Icons.Filled.DeleteForever,
                title = "Papelera",
                description = "Los elementos eliminados aparecerán en esta sección."
            )
            return
        }
    }

    Column(
        modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Plantilla básica con Scaffold", style = MaterialTheme.typography.headlineSmall)
        Text("Explora los elementos principales de una interfaz en Jetpack Compose.")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Sección actual: $section", style = MaterialTheme.typography.titleMedium)
                Text("El Scaffold organiza las barras y el contenido de la pantalla.")
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Floating Action Button", style = MaterialTheme.typography.titleMedium)
                Text("Has presionado el botón $clicks ${if (clicks == 1) "vez" else "veces"}.")
            }
        }
        Spacer(Modifier.height(4.dp))
        Text("Prueba el menú superior, el menú lateral y la navegación inferior.")
    }
}

@Composable
private fun DestinationScreen(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp).height(72.dp).width(72.dp)
        )
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
