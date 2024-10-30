package com.teovladusic.widgetsforstripe.core.data.api_result

internal enum class ApiErrorType(val message: String) {
    BadRequest("Bad Request"),
    Unauthorized("Unauthorized"),
    Forbidden("Forbidden"),
    NotFound("Not found");

    companion object {
        fun fromStatusCode(code: Int) = when (code) {
            400 -> BadRequest
            401 -> Unauthorized
            403 -> Forbidden
            404 -> NotFound
            else -> null
        }
    }
}
