package com.samio.core.service.concretion._pseudoProperty

import com.samio.core.model.concretion._pseudoProperty._PseudoProperty
import com.samio.core.service.abstraction.EventServiceTemplate
import com.samio.core.service.annotation.EventServiceFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Suppress("ClassName")
@Service
@EventServiceFor(_PseudoProperty::class)
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
open class _PseudoPropertyEventService: EventServiceTemplate<_PseudoProperty>()