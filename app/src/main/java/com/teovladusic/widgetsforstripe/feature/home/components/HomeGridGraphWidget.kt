package com.teovladusic.widgetsforstripe.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.ui.modifier.glowingEffect
import java.time.LocalDateTime

@Composable
fun HomeGridGraphWidget(
    project: StripeProjectWithCharges?,
    size: HomeGridGraphWidgetSize
) {
    val charges = when (size) {
        HomeGridGraphWidgetSize.Large -> project?.charges112DaysAgoAmount ?: return
        HomeGridGraphWidgetSize.Small -> project?.charges49DaysAgoAmount ?: return
    }

    Column(
        modifier = Modifier
            .glowingEffect()
            .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(all = Dimens.spacing_s),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(size.rowsCount) { week ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(size.columnsCount) { day ->
                    val dayIndex = week * size.columnsCount + day
                    val revenue = charges[dayIndex]

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = revenue.second, shape = RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Column {
        Spacer(Modifier.height(Dimens.spacing_xl))
        LazyRow {
            item {
                HomeGridGraphWidget(
                    project = StripeProjectWithCharges(
                        id = "",
                        name = "test",
                        charges = emptyList(),
                        timeRefreshed = LocalDateTime.now()
                    ),
                    size = HomeGridGraphWidgetSize.Large
                )
            }
        }
    }
}

enum class HomeGridGraphWidgetSize {
    Large, Small;
}

val HomeGridGraphWidgetSize.columnsCount: Int
    get() = when (this) {
        HomeGridGraphWidgetSize.Large -> 16
        HomeGridGraphWidgetSize.Small -> 7
    }

val HomeGridGraphWidgetSize.rowsCount: Int
    get() = when (this) {
        HomeGridGraphWidgetSize.Large -> 7
        HomeGridGraphWidgetSize.Small -> 7
    }
