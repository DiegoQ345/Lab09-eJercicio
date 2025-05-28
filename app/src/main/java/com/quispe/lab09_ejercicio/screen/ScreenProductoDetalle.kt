package com.quispe.lab09_ejercicio.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.quispe.lab09_ejercicio.Models.ProductModel
import com.quispe.lab09_ejercicio.Interfaz.ProductApiService
import kotlinx.coroutines.launch

@Composable
fun ScreenProductoDetalle(navController: NavHostController, servicio: ProductApiService, id: Int) {
    var producto by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(id) {
        try {
            val p = servicio.getProductById(id)
            producto = p
        } catch (e: Exception) {
            Log.e("ScreenProductoDetalle", "Error: ${e.message}")
        }
    }

    if (producto == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                producto!!.title ?: "Sin título",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Marca: ${producto!!.brand ?: "Sin marca"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                "Precio: $${producto!!.price ?: 0.0}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = producto!!.thumbnail ?: "",
                contentDescription = producto!!.title ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("Descripción:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(producto!!.description ?: "Sin descripción", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Calificación: ${producto!!.rating ?: 0.0}", style = MaterialTheme.typography.bodyMedium)
            Text("Stock disponible: ${producto!!.stock ?: 0}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Imágenes:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)



            LazyRow {
                items(producto!!.images ?: emptyList()) { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = producto!!.title ?: "",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(end = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    scope.launch {
                        try {
                            servicio.deleteProduct(producto!!.id!!)
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Log.e("EliminarProducto", "Error: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar")
            }

        }
    }
}

