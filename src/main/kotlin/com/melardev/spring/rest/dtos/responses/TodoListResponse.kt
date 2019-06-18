package com.melardev.spring.rest.dtos.responses

import com.melardev.spring.rest.entities.Todo
import java.util.stream.Collectors.toList

class TodoListResponse(val pageMeta: PageMeta, val todos: Collection<TodoSummaryDto>) : SuccessResponse() {
    companion object {
        @JvmStatic
        fun build(pageMeta: PageMeta, todoList: MutableList<Todo>): TodoListResponse {

            val dtos = todoList.stream().map { TodoSummaryDto.build(it) }.collect(toList())
            return TodoListResponse(pageMeta, dtos)
        }
    }
}
