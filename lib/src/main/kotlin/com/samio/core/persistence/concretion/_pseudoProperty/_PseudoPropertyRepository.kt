package com.samio.core.persistence.concretion._pseudoProperty

import com.samio.core.model.concretion._pseudoProperty._PseudoProperty
import com.samio.core.persistence.abstraction.EntityRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import java.util.*

@Suppress("ClassName")
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
interface _PseudoPropertyRepository : EntityRepository<_PseudoProperty, UUID> {
    fun findAllByEntitySimpleName(entitySimpleName: String): List<_PseudoProperty>
}

