package com.teovladusic.widgetsforstripe.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithCharges
import com.teovladusic.widgetsforstripe.core.ui.modifier.glowingEffect
import java.time.LocalDateTime

@Composable
fun HomeRevenueWidget(project: StripeProjectWithCharges?, width: Dp) {
    Column(
        modifier = Modifier
            .size(height = 200.dp, width = width)
            .glowingEffect()
            .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(all = Dimens.spacing_s),
    ) {
        Row {
            Text(
                text = "$ ",
                color = Primary,
                fontFamily = NunitoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = project?.name.orEmpty(),
                color = Color.White.copy(alpha = .5f),
                fontFamily = NunitoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                text = project?.formattedTotalRevenueLast30Days ?: "0.00",
                color = Color.White,
                fontSize = 30.sp,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 34.sp
            )
            Text(
                text = stringResource(R.string.last_30_days),
                color = Color.White.copy(alpha = .5f),
                fontSize = 14.sp,
                fontFamily = NunitoFontFamily,
                maxLines = 1,
                lineHeight = 14.sp,
                modifier = Modifier.graphicsLayer { translationY = -2.dp.toPx() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " ${project?.timeRefreshedString.orEmpty()}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = .5f)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(100.dp)
    ) {
        HomeRevenueWidget(
            project = StripeProjectWithCharges(
                id = "",
                name = "test",
                charges = emptyList(),
                timeRefreshed = LocalDateTime.now()
            ),
            width = 150.dp
        )
    }
}
