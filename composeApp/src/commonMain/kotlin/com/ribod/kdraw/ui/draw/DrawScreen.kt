package com.ribod.kdraw.ui.draw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawScreen(modifier: Modifier = Modifier, id: Long, onBackPressed: () -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(id.toString())
        Button(onClick = onBackPressed) {
            Text("Back")
        }
    }
}