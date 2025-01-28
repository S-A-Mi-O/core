package com.samio.core.application.kafka

import com.samio.core.application.validation.modification.ModificationType
import java.util.*

data class EntityEvent(
    val entityClassName: String,
    val id: UUID,
    val type: ModificationType,
    val properties: Map<String, Any?>,
)
