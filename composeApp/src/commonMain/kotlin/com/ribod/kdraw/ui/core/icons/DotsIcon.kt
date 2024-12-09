package com.ribod.kdraw.ui.core.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val DotsIcon: ImageVector
    get() {
        if (_Background_dot_small != null) {
            return _Background_dot_small!!
        }
        _Background_dot_small = ImageVector.Builder(
            name = "Background_dot_small",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(300f, 720f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(360f, 660f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(300f, 600f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(240f, 660f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(300f, 720f)
                moveToRelative(0f, -360f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(360f, 300f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(300f, 240f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(240f, 300f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(300f, 360f)
                moveToRelative(0f, 180f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(360f, 480f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(300f, 420f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(240f, 480f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(300f, 540f)
                moveToRelative(360f, 180f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(720f, 660f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(660f, 600f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(600f, 660f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(660f, 720f)
                moveToRelative(0f, -360f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(720f, 300f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(660f, 240f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(600f, 300f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(660f, 360f)
                moveTo(200f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-560f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(200f, 120f)
                horizontalLineToRelative(560f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(840f, 200f)
                verticalLineToRelative(560f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(760f, 840f)
                close()
                moveToRelative(0f, -80f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(-560f)
                horizontalLineTo(200f)
                close()
                moveToRelative(0f, -560f)
                verticalLineToRelative(560f)
                close()
                moveToRelative(460f, 340f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(720f, 480f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(660f, 420f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(600f, 480f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(660f, 540f)
                moveTo(480f, 360f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(540f, 300f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(480f, 240f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(420f, 300f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(480f, 360f)
                moveToRelative(0f, 360f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(540f, 660f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(480f, 600f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(420f, 660f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(480f, 720f)
                moveToRelative(0f, -180f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(540f, 480f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(480f, 420f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(420f, 480f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(480f, 540f)
            }
        }.build()
        return _Background_dot_small!!
    }

private var _Background_dot_small: ImageVector? = null
