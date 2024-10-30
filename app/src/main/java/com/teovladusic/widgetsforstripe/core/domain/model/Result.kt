package com.teovladusic.widgetsforstripe.core.domain.model

import com.teovladusic.widgetsforstripe.core.data.api_result.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(val throwable: Throwable? = null, val message: String? = null) :
        Result<Nothing>
}

suspend inline fun <T, K> ApiResult<K>.handleErrorAndMapSuccess(
    crossinline mapSuccess: suspend (ApiResult.Success<K>) -> T
): Result<T> = withContext(Dispatchers.Default) {
    try {
        when (this@handleErrorAndMapSuccess) {
            is ApiResult.Error -> Result.Failure(message = message)
            is ApiResult.Exception -> Result.Failure(throwable)
            is ApiResult.Success -> Result.Success(mapSuccess(this@handleErrorAndMapSuccess))
        }
    } catch (e: Exception) {
        Timber.e(e)
        Result.Failure(e)
    }
}

val Result.Failure.errorStringValue: StringValue?
    get() = message?.let { StringValue.DynamicString(it) }
