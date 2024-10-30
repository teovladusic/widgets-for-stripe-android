package com.teovladusic.widgetsforstripe.core.design_system.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

@Composable
fun WFSPrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    shape: Shape = RoundedCornerShape(12.dp),
    enabled: Boolean = true,
    backgroundColor: Color = Primary,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.height(54.dp),
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = Color(color = 0xFF1C1C1C),
            contentColor = Color.White,
            disabledContentColor = Color.White.copy(alpha = .5f)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontFamily = NunitoFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun Preview() {
    WFSPrimaryButton(text = "widgets for stripe") { }
}
