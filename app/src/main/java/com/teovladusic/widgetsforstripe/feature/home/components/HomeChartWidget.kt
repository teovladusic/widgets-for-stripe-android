package com.teovladusic.widgetsforstripe.feature.home.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.ui.modifier.glowingEffect

@Composable
internal fun HomeChartWidget(
    projectWithCharges: StripeProjectWithCharges?,
    width: Dp
) {
    Column(
        modifier = Modifier
            .size(height = 200.dp, width = width)
            .glowingEffect()
            .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(all = Dimens.spacing_s)
    ) {
        Row {
            Text(
                text = "$ ",
                style = TextStyle(
                    color = Primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Column {
                Text(
                    text = projectWithCharges?.formattedTotalRevenueLast7Days ?: "0.00",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "Last 7 days",
                    style = TextStyle(
                        color = Color.White.copy(alpha = .5f),
                        fontSize = 12.sp
                    )
                )
            }
        }
        projectWithCharges?.let { LineChart(it, width) }
    }
}

@Composable
private fun LineChart(project: StripeProjectWithCharges, width: Dp) {
    val bitmap = drawGraph(project)

    Image(
        modifier = Modifier
            .width(width)
            .fillMaxSize(),
        bitmap = bitmap.asImageBitmap(),
        contentScale = ContentScale.Fit,
        contentDescription = null
    )
}

private fun drawGraph(project: StripeProjectWithCharges): Bitmap {
    val width = 600
    val height = 300
    val padding = 25
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        color = android.graphics.Color.parseColor("#5459FD")
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    val pointPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#5459FD")
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 20f
        isAntiAlias = true
    }

    val path = Path()
    val maxRevenue = project.revenueForLast7Days.maxOf { it.second }

    val spacing = (width - 2 * padding) / (project.revenueForLast7Days.size - 1)
    val points = mutableListOf<Pair<Float, Float>>()

    // Calculate the positions for each point, including padding
    for (i in project.revenueForLast7Days.indices) {
        val x = i * spacing.toFloat() + padding
        val y =
            height - (project.revenueForLast7Days[i].second.toFloat() / maxRevenue * (height - 2 * padding)) - padding
        points.add(Pair(x, y))
    }

    // Start drawing the path using Bezier curves
    path.moveTo(points[0].first, points[0].second)

    for (i in 1 until points.size) {
        val (prevX, prevY) = points[i - 1]
        val (curX, curY) = points[i]

        // Control points for Bezier curves
        val midX = (prevX + curX) / 2

        // Draw a cubic Bezier curve between two points
        path.cubicTo(midX, prevY, midX, curY, curX, curY)
    }

    // Draw the smooth path
    canvas.drawPath(path, paint)

    // Draw points and text after the path to ensure they appear on top
    points.forEachIndexed { index, (x, y) ->
        canvas.drawCircle(x, y, 6f, pointPaint)

        val revenueText = "${project.revenueForLast7Days[index].second / 100}"
        canvas.drawText(revenueText, x, y - 10f, textPaint)
    }

    return bitmap
}
