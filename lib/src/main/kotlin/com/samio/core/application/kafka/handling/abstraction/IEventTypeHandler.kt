package com.samio.core.application.kafka.handling.abstraction

import com.samio.core.application.kafka.EntityEvent
import com.samio.core.model.abstraction.BaseEntity

interface IEventTypeHandler<T: BaseEntity> {
    fun applyChanges(event: EntityEvent) {
    }
}