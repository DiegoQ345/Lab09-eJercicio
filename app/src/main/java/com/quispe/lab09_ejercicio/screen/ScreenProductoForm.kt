package com.quispe.lab09_ejercicio.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quispe.lab09_ejercicio.Interfaz.ProductApiService
import com.quispe.lab09_ejercicio.Models.ProductModel
import kotlinx.coroutines.launch

@Composable
fun ScreenProductoForm(
    navController: NavHostController,
    servicio: ProductApiService,
    productoExistente: ProductModel? = null
) {
    var titulo by remember { mutableStateOf(productoExistente?.title ?: "") }
    var descripcion by remember { mutableStateOf(productoExistente?.description ?: "") }
    var precio by remember { mutableStateOf(productoExistente?.price?.toString() ?: "") }

    val esEdicion = productoExistente != null
    val scope = rememberCoroutineScope() // Para lanzar la corrutina

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (esEdicion) "Editar Producto" else "Nuevo Producto",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val nuevo = ProductModel(
                id = productoExistente?.id,
                title = titulo,
                description = descripcion,
                price = precio.toDoubleOrNull() ?: 0.0,
                discountPercentage = productoExistente?.discountPercentage,
                rating = productoExistente?.rating,
                stock = productoExistente?.stock,
                brand = productoExistente?.brand,
                category = productoExistente?.category,
                thumbnail = productoExistente?.thumbnail,
                images = productoExistente?.images
            )

            scope.launch {
                try {
                    if (esEdicion) servicio.updateProduct(nuevo.id ?: 0, nuevo)
                    else servicio.addProduct(nuevo)
                    navController.popBackStack()
                } catch (e: Exception) {
                    Log.e("ProductoForm", "Error: ${e.message}")
                }
            }
        }) {
            Text(if (esEdicion) "Guardar Cambios" else "Agregar Producto")
        }
    }
}
