package com.samio.core.persistence.concretion.permissionuserassociation

import com.samio.core.model.concretion.permissionuserassociation.PermissionUserAssociation
import com.samio.core.persistence.abstraction.EntityPersistenceAdapter
import com.samio.core.persistence.annotation.PersistenceAdapterFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@PersistenceAdapterFor(PermissionUserAssociation::class)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
class PermissionUserAssociationAdapter : EntityPersistenceAdapter<PermissionUserAssociation>()