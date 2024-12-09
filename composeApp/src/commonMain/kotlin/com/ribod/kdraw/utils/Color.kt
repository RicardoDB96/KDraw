package com.ribod.kdraw.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.pow

/**
 * Ajusta el color según el brillo del fondo
 * @param color Color original
 * @param backgroundColor Color del fondo
 * @return Color ajustado
 */
fun adjustColorForBackground(color: Color, backgroundColor: Color): Color {
    // Calcular la luminosidad del fondo
    val backgroundLuminosity = calculateRelativeLuminance(backgroundColor)

    // Si el fondo es claro (luminosidad > 0.5), devolver el color original
    return if (backgroundLuminosity > 0.5) {
        color
    } else {
        // Para fondos oscuros
        when {
            // Si es negro o muy oscuro, invertir a blanco
            isVeryDarkColor(color) -> Color.White
            // Otros colores oscuros, aclarar
            else -> lightenColor(color)
        }
    }
}

/**
 * Determina si un color es muy oscuro
 * @param color Color a evaluar
 * @return true si el color es considerado muy oscuro
 */
fun isVeryDarkColor(color: Color): Boolean {
    val luminance = calculateRelativeLuminance(color)
    return luminance < 0.1 // Umbral para considerarlo muy oscuro
}

/**
 * Calcula la luminosidad relativa de un color
 * @param color Color a analizar
 * @return Valor de luminosidad entre 0 y 1
 */
fun calculateRelativeLuminance(color: Color): Double {
    val r = color.red
    val g = color.green
    val b = color.blue

    val rSRGB = if (r <= 0.03928) r / 12.92 else ((r + 0.055) / 1.055).pow(2.4)
    val gSRGB = if (g <= 0.03928) g / 12.92 else ((g + 0.055) / 1.055).pow(2.4)
    val bSRGB = if (b <= 0.03928) b / 12.92 else ((b + 0.055) / 1.055).pow(2.4)

    return 0.2126 * rSRGB + 0.7152 * gSRGB + 0.0722 * bSRGB
}

/**
 * Aclara un color para mejorar el contraste en fondos oscuros
 * @param color Color original
 * @return Color aclarado
 */
fun lightenColor(color: Color): Color {
    return Color(
        red = (color.red + (1f - color.red) * 0.5).coerceIn(0.0, 1.0).toFloat(),
        green = (color.green + (1f - color.green) * 0.5).coerceIn(0.0, 1.0).toFloat(),
        blue = (color.blue + (1f - color.blue) * 0.5).coerceIn(0.0, 1.0).toFloat(),
        alpha = color.alpha
    )
}