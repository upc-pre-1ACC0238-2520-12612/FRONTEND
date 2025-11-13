package com.stayclean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState as rememberM3DrawerState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.stayclean.app.ui.theme.StayCleanTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Manejador global para capturar excepciones no controladas y escribirlas en logcat
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("StayClean", "Uncaught exception on thread ${thread.name}", throwable)
        }

        enableEdgeToEdge()
        try {
            setContent {
                StayCleanTheme {
                    AppRoot()
                }
            }
        } catch (e: Throwable) {
            // Registro adicional por si algo falla sin llegar al manejador global
            Log.e("StayClean", "Error al inicializar UI", e)
            throw e
        }
    }
}

private enum class Screen { Inicio, Panel, Acciones, Puntos, Reportes, Perfil }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val drawerState = rememberM3DrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val currentScreen = remember { mutableStateOf(Screen.Inicio) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Menú", modifier = Modifier.padding(vertical = 8.dp))
                HorizontalDivider()
                NavigationItem("Inicio", Screen.Inicio, currentScreen, drawerState, scope)
                NavigationItem("Panel", Screen.Panel, currentScreen, drawerState, scope)
                NavigationItem("Acciones", Screen.Acciones, currentScreen, drawerState, scope)
                NavigationItem("Puntos", Screen.Puntos, currentScreen, drawerState, scope)
                NavigationItem("Reportes", Screen.Reportes, currentScreen, drawerState, scope)
                NavigationItem("Perfil", Screen.Perfil, currentScreen, drawerState, scope)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentScreen.value.name) },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú",
                            modifier = Modifier
                                .clickable {
                                    scope.launch { drawerState.open() }
                                }
                                .padding(12.dp)
                        )
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            when (currentScreen.value) {
                Screen.Inicio -> InicioScreen(Modifier.padding(innerPadding))
                Screen.Panel -> PanelScreen(Modifier.padding(innerPadding))
                Screen.Acciones -> AccionesScreen(Modifier.padding(innerPadding))
                Screen.Puntos -> PuntosScreen(Modifier.padding(innerPadding))
                Screen.Reportes -> ReportesScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState
                )
                Screen.Perfil -> PerfilScreen(Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
private fun NavigationItem(
    label: String,
    screen: Screen,
    currentScreen: androidx.compose.runtime.MutableState<Screen>,
    drawerState: androidx.compose.material3.DrawerState,
    scope: CoroutineScope
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = currentScreen.value == screen,
        onClick = {
            currentScreen.value = screen
            scope.launch { drawerState.close() }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun AppButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        // For accessibility let the content decide text color or fallback to onPrimary
        content()
    }
}

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Inicio - Resumen rápido")
        Spacer(modifier = Modifier.padding(8.dp))
        // KPI cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Card(modifier = Modifier.weight(1f).padding(end = 8.dp), shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "StayPoints")
                    Text(text = "1,250")
                }
            }
            Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Kg reciclados")
                    Text(text = "35 kg")
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Puntos cercanos")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(listOf("Ecopunto Central", "Reciclaje Norte", "Punto Este")) { p ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(6.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = p)
                            Text(text = "A 2.1 km")
                        }
                        AppButton(onClick = { /* abrir mapa */ }) { Text(text = "Ver", color = MaterialTheme.colorScheme.onPrimary) }
                    }
                }
            }
        }
    }
}

@Suppress("unused")
@Composable
fun PlaceholderScreen(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "$name - pantalla en desarrollo")
    }
}

@Composable
fun PanelScreen(modifier: Modifier = Modifier) {
    // Mock KPI and simple bar chart
    val sampleZones = listOf("Centro" to 120, "Norte" to 80, "Sur" to 50, "Este" to 30)

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Panel ambiental")
        Spacer(modifier = Modifier.height(8.dp))
        // KPIs row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Card(modifier = Modifier.weight(1f).padding(end = 8.dp), shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Kg mes")
                    Text(text = "280 kg")
                }
            }
            Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Alertas activas")
                    Text(text = "2")
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Residuos por zona")
        Spacer(modifier = Modifier.height(8.dp))
        // Simple bars
        LazyColumn {
            items(sampleZones) { (zone, value) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = zone, modifier = Modifier.weight(0.3f))
                    Box(modifier = Modifier.weight(0.7f)) {
                        // bar background
                        Card(shape = RoundedCornerShape(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                                val widthPercent = (value / 200f).coerceAtMost(1f)
                                Box(modifier = Modifier.fillMaxWidth(widthPercent)) {
                                    Text(text = "${value}kg")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Mis zonas")
        MisZonasScreen()
    }
}

@Composable
fun MisZonasScreen() {
    val zonas = remember { mutableStateListOf("Centro", "Norte") }
    val showAdd = remember { mutableStateOf(false) }
    val newZone = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        AppButton(onClick = { showAdd.value = true }) { Text(text = "Añadir zona", color = MaterialTheme.colorScheme.onPrimary) }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(zonas) { z ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(6.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = z)
                            Text(text = "Sensores: 2")
                        }
                        AppButton(onClick = { /* ver sensores */ }) { Text(text = "Ver", color = MaterialTheme.colorScheme.onPrimary) }
                        Spacer(modifier = Modifier.padding(6.dp))
                        AppButton(onClick = { zonas.remove(z) }) { Text(text = "Borrar", color = MaterialTheme.colorScheme.onPrimary) }
                    }
                }
            }
        }

        if (showAdd.value) {
            AlertDialog(
                onDismissRequest = { showAdd.value = false },
                title = { Text(text = "Añadir zona") },
                text = {
                    Column {
                        Text(text = "Ingresa el nombre de la zona:")
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.TextField(value = newZone.value, onValueChange = { newZone.value = it })
                    }
                },
                confirmButton = {
                    AppButton(onClick = {
                        if (newZone.value.isNotBlank()) {
                            zonas.add(newZone.value.trim())
                            newZone.value = ""
                        }
                        showAdd.value = false
                    }) { Text(text = "Agregar", color = MaterialTheme.colorScheme.onPrimary) }
                },
                dismissButton = {
                    AppButton(onClick = { showAdd.value = false }) { Text(text = "Cancelar", color = MaterialTheme.colorScheme.onPrimary) }
                }
            )
        }
    }
}

@Composable
fun AccionesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Acciones sostenibles")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(listOf("Compostaje doméstico", "Reducir uso de plástico", "Segregar en origen")) { a ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(6.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) { Text(text = a); Text(text = "Tip breve...") }
                        AppButton(onClick = { /* marcar completada */ }) { Text(text = "Ver", color = MaterialTheme.colorScheme.onPrimary) }
                    }
                }
            }
        }
    }
}

@Composable
fun PuntosScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Puntos de acopio")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(listOf("Ecopunto Central", "Reciclaje Norte", "Punto Este")) { p ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(6.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) { Text(text = p); Text(text = "Tipo: Plástico") }
                        AppButton(onClick = { /* ver ruta */ }) { Text(text = "Ruta", color = MaterialTheme.colorScheme.onPrimary) }
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Perfil de la empresa")
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(6.dp), shape = RoundedCornerShape(8.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Nombre: Empresa S.A.")
                Text(text = "RUT: 12345678-9")
                Text(text = "Contacto: contacto@empresa.com")
            }
        }
    }
}

data class Recojo(
    val zona: String,
    val tipo: String,
    val kg: Int,
    val fecha: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    // Datos de ejemplo
    val sample = listOf(
        Recojo("Centro", "Plástico", 120, "2024-01-15"),
        Recojo("Norte", "Papel", 80, "2024-02-20"),
        Recojo("Sur", "Vidrio", 50, "2024-03-10"),
        Recojo("Este", "Metal", 30, "2024-04-05")
    )

    val showDialog = remember { mutableStateOf(false) }
    val exporting = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Reportes", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Consulta información clave y exporta tus reportes para auditorías, normativas y toma de decisiones")
        Spacer(modifier = Modifier.height(12.dp))

        // Botones de exportación - estilo como en el mock
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Exportar PDF", color = MaterialTheme.colorScheme.onPrimary)
            }

            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(text = "Exportar Excel", color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Historial de Recojos", modifier = Modifier.padding(vertical = 8.dp))
        HorizontalDivider()

        // Encabezado de tabla
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(text = "Zona", modifier = Modifier.weight(0.25f))
            Text(text = "Tipo de residuos", modifier = Modifier.weight(0.25f))
            Text(text = "Kg recolectado", modifier = Modifier.weight(0.2f))
            Text(text = "Última recolección", modifier = Modifier.weight(0.3f))
        }
        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(sample) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(6.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = item.zona, modifier = Modifier.weight(0.25f))
                        Text(text = item.tipo, modifier = Modifier.weight(0.25f))
                        Text(text = "${item.kg} kg", modifier = Modifier.weight(0.2f))
                        Text(text = item.fecha, modifier = Modifier.weight(0.3f))
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "¿Quiere exportar el siguiente documento?") },
            text = { Text(text = "Selecciona Exportar para iniciar la generación del documento.") },
            confirmButton = {
                AppButton(onClick = {
                    showDialog.value = false
                    exporting.value = true
                    scope.launch {
                        snackbarHostState.showSnackbar("Exportando...")
                        delay(1500)
                        val success = kotlin.random.Random.nextBoolean()
                        exporting.value = false
                        if (success) {
                            snackbarHostState.showSnackbar("Exportación finalizada")
                        } else {
                            snackbarHostState.showSnackbar("No se pudo exportar el documento")
                        }
                    }
                }) { Text(text = "Exportar", color = MaterialTheme.colorScheme.onPrimary) }
            },
            dismissButton = {
                AppButton(onClick = { showDialog.value = false }) { Text(text = "Cancelar", color = MaterialTheme.colorScheme.onPrimary) }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportesPreview() {
    StayCleanTheme {
        ReportesScreen(snackbarHostState = remember { SnackbarHostState() })
    }
}

@Preview(showBackground = true)
@Composable
fun AppRootPreview() {
    StayCleanTheme {
        AppRoot()
    }
}
