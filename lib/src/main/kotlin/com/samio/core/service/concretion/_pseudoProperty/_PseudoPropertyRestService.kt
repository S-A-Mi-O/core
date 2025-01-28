package com.samio.core.service.concretion._pseudoProperty

import com.samio.core.model.concretion._pseudoProperty._PseudoProperty
import com.samio.core.service.abstraction.DownstreamRestServiceTemplate
import com.samio.core.service.annotation.RestServiceFor
import jakarta.transaction.Transactional
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Suppress("ClassName")
@Transactional
@Service
@RestServiceFor(_PseudoProperty::class)
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
open class _PseudoPropertyRestService : DownstreamRestServiceTemplate<_PseudoProperty>()