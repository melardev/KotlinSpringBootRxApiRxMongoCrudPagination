package com.melardev.spring.rest.entities.auditors


import com.melardev.spring.rest.entities.TimeStampedDocument
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component

import java.time.LocalDateTime

@Component
class TimestampAuditor : AbstractMongoEventListener<TimeStampedDocument>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<TimeStampedDocument>) {
        if (event.source.createdAt == null)
            event.source.createdAt = LocalDateTime.now()
        event.source.updatedAt = LocalDateTime.now()
    }
}
