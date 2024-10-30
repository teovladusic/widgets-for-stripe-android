package com.teovladusic.widgetsforstripe.core.util.constants

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.net.Uri
import timber.log.Timber

object AndroidIntentLauncher {

    fun openSendEmailForm(
        context: Context,
        mailTo: String? = null,
        subject: String? = null,
        body: String? = null
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            addFlags(FLAG_ACTIVITY_NO_HISTORY)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

            data = Uri.parse("mailto:")

            mailTo?.let { putExtra(Intent.EXTRA_EMAIL, arrayOf(it)) }
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            body?.let { putExtra(Intent.EXTRA_TEXT, it) }
        }

        context.startActivity(emailIntent)
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            addFlags(FLAG_ACTIVITY_NO_HISTORY)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
