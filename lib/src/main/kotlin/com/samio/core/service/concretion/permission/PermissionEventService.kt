package com.samio.core.service.concretion.permission

import com.samio.core.model.concretion.permission.Permission
import com.samio.core.service.abstraction.EventServiceTemplate
import com.samio.core.service.annotation.EventServiceFor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@EventServiceFor(Permission::class)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
open class PermissionEventService : EventServiceTemplate<Permission>()