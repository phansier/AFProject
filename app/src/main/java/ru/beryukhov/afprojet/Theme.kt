package ru.beryukhov.afprojet

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.tromian.game.afproject.R

@Composable
private fun DarkColorPalette() = darkColors(
    primary = colorResource(id = R.color.purple_200),
    primaryVariant = colorResource(id = R.color.purple_700),
    secondary = colorResource(id = R.color.teal_200)
)
@Composable
private fun LightColorPalette() = lightColors(
    primary = colorResource(id = R.color.menu_tint),
    primaryVariant = colorResource(id = R.color.purple_700),
    secondary = colorResource(id = R.color.teal_200)
)

@Composable
fun MyTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) {
        DarkColorPalette()
    } else {
        LightColorPalette()
    }

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}