package com.samio.core.persistence.concretion._pseudoProperty

import com.samio.core.model.concretion._pseudoProperty._PseudoProperty
import com.samio.core.persistence.abstraction.EntityPersistenceAdapter
import com.samio.core.persistence.annotation.PersistenceAdapterFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Suppress("ClassName")
@Service
@PersistenceAdapterFor(_PseudoProperty::class)
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
class _PseudoPropertyPersistenceAdapter : EntityPersistenceAdapter<_PseudoProperty>()