package com.teovladusic.widgetsforstripe.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.R
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary
import com.teovladusic.widgetsforstripe.core.domain.model.StripeProjectWithMrr
import com.teovladusic.widgetsforstripe.core.ui.modifier.glowingEffect

@Composable
internal fun HomeMrrWidget(project: StripeProjectWithMrr?) {
    Column(
        modifier = Modifier
            .padding(all = Dimens.spacing_m)
            .size(height = 200.dp, width = 220.dp)
            .glowingEffect()
            .background(color = Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(all = Dimens.spacing_s),
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
            Text(
                text = project?.name.orEmpty(),
                style = TextStyle(
                    color = Color.White.copy(alpha = .5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = project?.formattedMrr ?: "0.00",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = "MRR",
                style = TextStyle(
                    color = Color.White.copy(alpha = .5f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(R.drawable.img_history),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            project?.timeRefreshedString?.let {
                Text(
                    text = " $it",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = .5f)
                    ),
                )
            }
        }
    }
}
