package com.teovladusic.widgetsforstripe.core.ui.widget.revenue

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
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
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.data.repository.GlanceWidgetRepository
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.ui.main.MainActivity

class StripeRevenueLargeGridGraphWidget : GlanceAppWidget() {
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
        val charges112DaysAgo = project?.charges112DaysAgoAmount ?: return

        Column(
            modifier = GlanceModifier
                .background(Color.Black)
                .fillMaxSize()
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = GlanceModifier.height(12.dp))

            for (week in 0 until 7) {
                Row(modifier = GlanceModifier.padding(horizontal = 2.dp)) {
                    Row {
                        repeat(10) { day ->
                            val dayIndex = week * 16 + day
                            val revenue = charges112DaysAgo[dayIndex]

                            Image(
                                provider = ImageProvider(R.drawable.shape_grid_graph_box),
                                colorFilter = ColorFilter.tint(ColorProvider(revenue.color)),
                                contentDescription = null,
                                modifier = GlanceModifier.padding(2.dp).size(20.dp)
                            )
                        }
                    }
                    Row {
                        repeat(6) {
                            val day = 10 + it
                            val dayIndex = week * 16 + day
                            val revenue = charges112DaysAgo[dayIndex]

                            Image(
                                provider = ImageProvider(R.drawable.shape_grid_graph_box),
                                colorFilter = ColorFilter.tint(ColorProvider(revenue.color)),
                                contentDescription = null,
                                modifier = GlanceModifier.padding(2.dp).size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = GlanceModifier.height(12.dp))
        }
    }
}

class StripeRevenueLargeGridGraphWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = StripeRevenueLargeGridGraphWidget()
}
