package com.teovladusic.widgetsforstripe.core.ui.modifier

import android.graphics.Paint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

fun Modifier.glowingEffect(): Modifier = then(
    Modifier.drawBehind {
        drawContext.canvas.nativeCanvas.apply {
            drawRoundRect(
                0f,
                0f,
                size.width,
                size.height,
                12.dp.toPx(),
                12.dp.toPx(),
                Paint().apply {
                    color = Primary.toArgb()
                    setShadowLayer(
                        12.dp.toPx(),
                        0.dp.toPx(), 0.dp.toPx(),
                        Primary.toArgb()
                    )
                }
            )
        }
    }
)
