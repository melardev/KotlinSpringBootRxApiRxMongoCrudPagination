package com.melardev.spring.rest.dtos.responses

import com.melardev.spring.rest.entities.Todo
import java.time.LocalDateTime


data class TodoDetailsResponse(val id: String?, val title: String, val description: String?, val isCompleted: Boolean, val createdAt: LocalDateTime, val updatedAt: LocalDateTime) : SuccessResponse() {

    @JvmOverloads
    constructor(todo: Todo, message: String? = null) : this(todo.id, todo.title, todo.description, todo.isCompleted, todo.createdAt!!, todo.updatedAt!!) {
        addFullMessage(message!!)
    }
}
