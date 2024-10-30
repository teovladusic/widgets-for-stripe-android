package com.teovladusic.widgetsforstripe.core.util.review

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NativeReviewManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val manager = ReviewManagerFactory.create(context)

    suspend fun requestReview(activity: Activity) {
        val reviewInfo = manager.requestReviewFlow().await()
        manager.launchReviewFlow(activity, reviewInfo)
    }
}
