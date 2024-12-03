package com.ribod.kdraw

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.ribod.kdraw.di.initKoin
import com.ribod.kdraw.ui.core.theme.KDrawTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.styling.Undecorated
import org.jetbrains.jewel.intui.standalone.styling.defaults
import org.jetbrains.jewel.intui.standalone.styling.undecorated
import org.jetbrains.jewel.intui.standalone.theme.IntUiTheme
import org.jetbrains.jewel.intui.standalone.theme.darkThemeDefinition
import org.jetbrains.jewel.intui.standalone.theme.default
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.defaults
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.ui.component.styling.DropdownColors
import org.jetbrains.jewel.ui.component.styling.DropdownMetrics
import org.jetbrains.jewel.ui.component.styling.DropdownStyle
import org.jetbrains.jewel.ui.component.styling.IconButtonColors
import org.jetbrains.jewel.ui.component.styling.IconButtonMetrics
import org.jetbrains.jewel.ui.component.styling.IconButtonStyle
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.DecoratedWindowScope
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarColors
import org.jetbrains.jewel.window.styling.TitleBarIcons
import org.jetbrains.jewel.window.styling.TitleBarMetrics
import org.jetbrains.jewel.window.styling.TitleBarStyle

fun main() {
    initKoin()
    application {
        IntUiTheme(
            theme = JewelTheme.darkThemeDefinition(),
            styling = ComponentStyling.default().decoratedWindow(),
        ) {
            DecoratedWindow(onCloseRequest = ::exitApplication, title = "KDraw") {
                TitleBarView()
                App()
            }
        }
    }
}

@Composable
private fun DecoratedWindowScope.TitleBarView() {
    KDrawTheme {
        val colors = TitleBarColors(
            background = MaterialTheme.colorScheme.surfaceContainerLow,
            inactiveBackground = MaterialTheme.colorScheme.surfaceContainerLow,
            content = Color.Unspecified,
            border = MaterialTheme.colorScheme.background,
            fullscreenControlButtonsBackground = Color(0xFF575A5C),
            titlePaneButtonHoveredBackground = Color(0x1AFFFFFF),
            titlePaneButtonPressedBackground = Color(0x1AFFFFFF),
            titlePaneCloseButtonHoveredBackground = Color(0xFFE81123),
            titlePaneCloseButtonPressedBackground = Color(0xFFF1707A),
            iconButtonHoveredBackground = Color(0xFF393B40),
            iconButtonPressedBackground = Color(0xFF393B40),
            dropdownPressedBackground = Color(0x1AFFFFFF),
            dropdownHoveredBackground = Color(0x1AFFFFFF),
        )
        TitleBar(
            modifier = Modifier.newFullscreenControls(),
            //gradientStartColor = MaterialTheme.colorScheme.primary,
            style = TitleBarStyle(
                colors = colors,
                metrics = TitleBarMetrics.defaults(),
                icons = TitleBarIcons.defaults(),
                dropdownStyle =
                DropdownStyle.Undecorated.dark(
                    colors =
                    DropdownColors.Undecorated.dark(
                        content = colors.content,
                        contentFocused = colors.content,
                        contentHovered = colors.content,
                        contentPressed = colors.content,
                        contentDisabled = colors.content,
                        backgroundHovered = colors.dropdownHoveredBackground,
                        backgroundPressed = colors.dropdownPressedBackground,
                    ),
                    metrics =
                    DropdownMetrics.undecorated(
                        arrowMinSize = DpSize(20.dp, 24.dp),
                        minSize = DpSize(60.dp, 30.dp),
                        cornerSize = CornerSize(6.dp),
                        contentPadding = PaddingValues(
                            start = 10.dp,
                            end = 0.dp,
                            top = 3.dp,
                            bottom = 3.dp
                        ),
                    ),
                ),
                iconButtonStyle =
                titleBarIconButtonStyle(
                    colors.iconButtonHoveredBackground,
                    colors.iconButtonPressedBackground,
                    IconButtonMetrics.defaults(borderWidth = 0.dp),
                ),
                paneButtonStyle =
                titleBarIconButtonStyle(
                    colors.titlePaneButtonHoveredBackground,
                    colors.titlePaneButtonPressedBackground,
                    IconButtonMetrics.defaults(cornerSize = CornerSize(0.dp), borderWidth = 0.dp),
                ),
                paneCloseButtonStyle =
                titleBarIconButtonStyle(
                    colors.titlePaneCloseButtonHoveredBackground,
                    colors.titlePaneCloseButtonPressedBackground,
                    IconButtonMetrics.defaults(cornerSize = CornerSize(0.dp), borderWidth = 0.dp),
                ),
            ),
        ) {
            Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

private fun titleBarIconButtonStyle(
    hoveredBackground: Color,
    pressedBackground: Color,
    metrics: IconButtonMetrics
) =
    IconButtonStyle(
        IconButtonColors(
            foregroundSelectedActivated = Color.Unspecified,
            background = Color.Unspecified,
            backgroundDisabled = Color.Unspecified,
            backgroundSelected = Color.Unspecified,
            backgroundSelectedActivated = Color.Unspecified,
            backgroundFocused = Color.Unspecified,
            backgroundPressed = hoveredBackground,
            backgroundHovered = pressedBackground,
            border = Color.Unspecified,
            borderDisabled = Color.Unspecified,
            borderSelected = Color.Unspecified,
            borderSelectedActivated = Color.Unspecified,
            borderFocused = hoveredBackground,
            borderPressed = pressedBackground,
            borderHovered = Color.Unspecified,
        ),
        metrics,
    )
