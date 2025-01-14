package com.ribod.kdraw.data.database

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromOffsetList(points: List<Offset>): String {
        return Json.encodeToString(points.map { it.toSerializable() })
    }

    @TypeConverter
    fun toOffsetList(pointsString: String): List<Offset> {
        return Json.decodeFromString<List<OffSet>>(pointsString).map { it.toOffset() }
    }

    @TypeConverter
    fun fromColor(color: Color): String {
        return Json.encodeToString(color.value)
    }

    @TypeConverter
    fun toColor(colorString: String): Color {
        val colorValue: ULong = Json.decodeFromString(colorString)
        return Color(colorValue)
    }
}

fun Offset.toSerializable() = OffSet(x, y)
fun OffSet.toOffset() = Offset(x, y)

@Serializable
data class OffSet(val x: Float, val y: Float)