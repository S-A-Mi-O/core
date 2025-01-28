package com.samio.core.persistence.concretion.permission

import com.samio.core.model.concretion.permission.Permission
import com.samio.core.persistence.abstraction.EntityPersistenceAdapter
import com.samio.core.persistence.annotation.PersistenceAdapterFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@PersistenceAdapterFor(Permission::class)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
class PermissionPersistenceAdapter : EntityPersistenceAdapter<Permission>()