package com.teovladusic.widgetsforstripe.core.ui.widget.repeating_revenue

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.data.repository.GlanceWidgetRepository
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import com.teovladusic.widgetsforstripe.core.ui.main.MainActivity

class StripeMonthlyRepeatingRevenueWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        val context = LocalContext.current
        val repository = remember { GlanceWidgetRepository.getInstance(context) }
        val selectedProject by repository.projectWithMrr.collectAsState(null)
        Widget(project = selectedProject)
    }

    @Composable
    private fun Widget(project: StripeProjectWithMrr?) {
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
                Text(
                    text = project?.name.orEmpty(),
                    style = TextStyle(
                        color = ColorProvider(Color.White.copy(alpha = .5f)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Column(
                modifier = GlanceModifier.defaultWeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project?.formattedMrr ?: "0.00",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    text = "MRR",
                    style = TextStyle(
                        color = ColorProvider(Color.White.copy(alpha = .5f)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.End
            ) {
                Image(
                    provider = ImageProvider(R.drawable.img_history),
                    contentDescription = null,
                    modifier = GlanceModifier.size(16.dp)
                )
                project?.timeRefreshedString?.let {
                    Text(
                        text = " $it",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = ColorProvider(Color.White.copy(alpha = .5f))
                        ),
                    )
                }
            }
        }
    }
}

class StripeMonthlyRepeatingRevenueWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = StripeMonthlyRepeatingRevenueWidget()
}