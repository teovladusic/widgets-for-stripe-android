package com.teovladusic.widgetsforstripe.core.navigation.bottom_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.core.design_system.theme.Dimens
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

@Composable
fun BottomBar(selectedDestination: String, onClick: (BottomBarDestination) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val color = if (destination.route == selectedDestination) Primary
            else Color.White.copy(alpha = .5f)
            Column(
                Modifier
                    .weight(1f)
                    .clickable { onClick(destination) }
                    .padding(horizontal = Dimens.spacing_m, vertical = Dimens.spacing_xxs),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = destination.icon,
                    contentDescription = null,
                    tint = color
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = destination.name,
                    color = color,
                    fontSize = 11.sp,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BottomBar(selectedDestination = "home_screen", onClick = {})
}
