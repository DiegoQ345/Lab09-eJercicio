package com.quispe.lab09_ejercicio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quispe.lab09_ejercicio.Interfaz.ProductApiService
import com.quispe.lab09_ejercicio.ui.theme.Lab09EjercicioTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.quispe.lab09_ejercicio.Models.ProductModel
import com.quispe.lab09_ejercicio.screen.ScreenProductoDetalle
import com.quispe.lab09_ejercicio.screen.ScreenProductoForm
import com.quispe.lab09_ejercicio.screen.ScreenProductos
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgPrincipal9()
        }
    }
}


@Composable
fun ProgPrincipal9() {
    val urlBase = "https://dummyjson.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val servicio = retrofit.create(ProductApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues ->
            Contenido(paddingValues, navController, servicio)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "DummyJSON Products",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(containerColor = Color.LightGray) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.List, contentDescription = "Productos") },
            label = { Text("Productos") },
            selected = navController.currentDestination?.route == "productos",
            onClick = { navController.navigate("productos") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicio: ProductApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        val scope = rememberCoroutineScope()

        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") {
                ScreenInicio()
            }

            composable("productos") {
                ScreenProductos(navController, servicio)
            }

            composable("nuevoProducto") {
                ScreenProductoForm(navController, servicio)
            }

            composable(
                "editarProducto/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id") ?: 0
                var producto by remember { mutableStateOf<ProductModel?>(null) }

                // Llamada a la API usando CoroutineScope en lugar de LaunchedEffect
                LaunchedEffect(Unit) {
                    scope.launch {
                        producto = servicio.getProductById(id)
                    }
                }

                producto?.let { productoCargado ->
                    ScreenProductoForm(navController, servicio, productoCargado)
                }
            }

            composable(
                "productoDetalle/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments?.getInt("id") ?: 0
                ScreenProductoDetalle(navController, servicio, id)
            }
        }
    }
}



@Composable
fun ScreenInicio() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Bienvenido a la app de Productos", style = MaterialTheme.typography.headlineMedium)
    }
}







