package com.quispe.lab09_ejercicio.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.quispe.lab09_ejercicio.Models.ProductModel
import com.quispe.lab09_ejercicio.Interfaz.ProductApiService



@Composable
fun ScreenProductos(navController: NavHostController, servicio: ProductApiService) {
    var productos = remember { mutableStateListOf<ProductModel>() }
    var isLoading by remember { mutableStateOf(false) }
    var skip by remember { mutableStateOf(0) }
    val limit = 10
    var isEndReached by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(skip) {
        if (!isEndReached && !isLoading) {
            isLoading = true
            try {
                val response = servicio.getProducts(limit = limit, skip = skip)
                productos.addAll(response.products)
                if (response.products.size < limit) isEndReached = true
            } catch (e: Exception) {
                Log.e("ScreenProductos", "Error: ${e.message}")
            }
            isLoading = false
        }
    }

    LazyColumn(state = listState) {
        items(productos) { producto ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("productoDetalle/${producto.id}") }
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = producto.thumbnail,
                    contentDescription = producto.title,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(producto.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(producto.brand, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("Precio: $${producto.price}", style = MaterialTheme.typography.bodyMedium)
                }
                Icon(Icons.Outlined.ArrowForward, contentDescription = "Ver detalle", modifier = Modifier.align(Alignment.CenterVertically))
            }
            Divider()
        }

        // Loader mientras se cargan más
        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    // Detectar si se llegó al final del scroll
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index == productos.size - 1 && !isLoading && !isEndReached) {
                    skip += limit
                }
            }
    }
}

