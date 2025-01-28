package com.samio.core.service.concretion.permissionuserassociation

import com.samio.core.model.concretion.permissionuserassociation.PermissionUserAssociation
import com.samio.core.service.abstraction.RestServiceTemplate
import com.samio.core.service.annotation.RestServiceFor
import jakarta.transaction.Transactional
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@RestServiceFor(PermissionUserAssociation::class)
@Transactional
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
open class PermissionUserAssociationRestService : RestServiceTemplate<PermissionUserAssociation>()
