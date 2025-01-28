package com.samio.core.service.abstraction

import com.samio.core.application.kafka.EntityEvent
import com.samio.core.model.abstraction.BaseEntity

interface IEventService<T: BaseEntity> {
    fun createByEvent(event: EntityEvent)
    fun updateByEvent(event: EntityEvent)
    fun deleteByEvent(event: EntityEvent)
}