package com.teovladusic.widgetsforstripe.core.design_system.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teovladusic.widgetsforstripe.core.design_system.theme.NunitoFontFamily
import com.teovladusic.widgetsforstripe.core.design_system.theme.Primary

@Composable
fun WFSTextInput(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(color = 0xFF1C1C1C),
            unfocusedContainerColor = Color(color = 0xFF1C1C1C),
            disabledContainerColor = Color(color = 0xFF1C1C1C),
            cursorColor = Primary,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = .5f),
                fontFamily = NunitoFontFamily,
                fontSize = 16.sp
            )
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    WFSTextInput(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth(),
        text = "",
        placeholder = "Placeholder",
        onValueChange = {})
}
