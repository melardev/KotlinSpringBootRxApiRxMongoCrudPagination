package com.melardev.spring.rest.entities

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "todos")
data class Todo(
        var title: String,
        var isCompleted: Boolean = false
) : TimeStampedDocument() {

    var description: String? = null

    constructor(id: String?, title: String, description: String?, completed: Boolean, createdAt: LocalDateTime?, updatedAt: LocalDateTime?)
            : this(title, completed) {
        this.id = id
        this.title = title
        this.description = description
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }


    constructor(id: String?, title: String, completed: Boolean, createdAt: LocalDateTime, updatedAt: LocalDateTime)
            : this(id, title, null, completed, createdAt, updatedAt)

    @JvmOverloads
    constructor(title: String, description: String, completed: Boolean = false)
            : this(null, title, description, completed, null, null)
}