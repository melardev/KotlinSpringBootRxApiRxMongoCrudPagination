package com.melardev.spring.rest.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

// Not needed
@Document
abstract class TimeStampedDocument(
        @Id var id: String? = null,
        var createdAt: LocalDateTime? = null,
        var updatedAt: LocalDateTime? = null
) {

}
