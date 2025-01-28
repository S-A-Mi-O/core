package com.samio.core.service.concretion.permission

import com.samio.core.model.concretion.permission.Permission
import com.samio.core.service.abstraction.RestServiceTemplate
import com.samio.core.service.annotation.RestServiceFor
import jakarta.transaction.Transactional
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@RestServiceFor(Permission::class)
@Transactional
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
open class PermissionRestService : RestServiceTemplate<Permission>()