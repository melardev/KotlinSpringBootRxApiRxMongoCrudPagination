package com.melardev.spring.rest.dtos.responses

open class SuccessResponse constructor(message: String? = null) : AppResponse(true) {

    init {
        if (message != null)
            addFullMessage(message)
    }
}
