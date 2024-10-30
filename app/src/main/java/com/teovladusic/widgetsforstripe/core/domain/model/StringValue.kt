package com.teovladusic.widgetsforstripe.core.domain.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.teovladusic.widgetsforstripe.R

sealed class StringValue {

    data class DynamicString(val value: String) : StringValue()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : StringValue()

    @Composable
    fun asString(): String {
        return LocalContext.current.getString()
    }

    fun toString(context: Context): String {
        return context.getString()
    }

    private fun Context.getString(): String {
        return when (this@StringValue) {
            is DynamicString -> value
            is StringResource -> getString(resId, args)
        }
    }

    companion object {
        val unknownError = StringResource(R.string.unknown_error)
    }
}
