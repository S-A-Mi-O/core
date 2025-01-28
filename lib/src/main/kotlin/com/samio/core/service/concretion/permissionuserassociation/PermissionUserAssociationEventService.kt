package com.samio.core.service.concretion.permissionuserassociation

import com.samio.core.model.concretion.permissionuserassociation.PermissionUserAssociation
import com.samio.core.service.abstraction.EventServiceTemplate
import com.samio.core.service.annotation.EventServiceFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@EventServiceFor(PermissionUserAssociation::class)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
open class PermissionUserAssociationEventService : EventServiceTemplate<PermissionUserAssociation>()