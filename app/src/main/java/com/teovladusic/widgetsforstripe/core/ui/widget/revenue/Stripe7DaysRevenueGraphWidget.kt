package com.teovladusic.widgetsforstripe.core.ui.widget.revenue

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.teovladusic.widgetsforstripe.core.data.repository.GlanceWidgetRepository
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.ui.main.MainActivity

class Stripe7DaysRevenueGraphWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        val context = LocalContext.current
        val repository = remember { GlanceWidgetRepository.getInstance(context) }
        val selectedProject by repository.selectedProject.collectAsState(null)
        Widget(project = selectedProject)
    }

    @Composable
    private fun Widget(project: StripeProjectWithCharges?) {
        Column(
            modifier = GlanceModifier
                .clickable(onClick = actionStartActivity<MainActivity>())
                .fillMaxHeight()
                .background(color = Color.Black)
                .padding(all = Dimens.spacing_s),
        ) {
            Row {
                Text(
                    text = "$ ",
                    style = TextStyle(
                        color = ColorProvider(Primary),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Column {

                    Text(
                        text = project?.formattedTotalRevenueLast7Days ?: "0.00",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "Last 7 days",
                        style = TextStyle(
                            color = ColorProvider(Color.White.copy(alpha = .5f)),
                            fontSize = 12.sp
                        )
                    )
                }
            }
            project?.let { LineChart(it) }
        }
    }

    @Composable
    private fun LineChart(project: StripeProjectWithCharges) {
        val bitmap = drawGraph(project)

        Image(
            modifier = GlanceModifier.fillMaxSize(),
            provider = ImageProvider(bitmap),
            contentScale = androidx.glance.layout.ContentScale.Fit,
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
}

class Stripe7DaysRevenueGraphWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = Stripe7DaysRevenueGraphWidget()
}