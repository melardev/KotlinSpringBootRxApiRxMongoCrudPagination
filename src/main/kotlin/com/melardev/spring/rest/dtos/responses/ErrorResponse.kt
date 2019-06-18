package com.melardev.spring.rest.dtos.responses

class ErrorResponse(errorMessage: String) : AppResponse(false) {

    init {
        addFullMessage(errorMessage)
    }

}
