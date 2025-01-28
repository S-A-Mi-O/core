package com.samio.core.persistence.concretion.permissionuserassociation

import com.samio.core.model.concretion.permissionuserassociation.PermissionUserAssociation
import com.samio.core.persistence.abstraction.EntityRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import java.util.*

@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
interface PermissionUserAssociationRepository : EntityRepository<PermissionUserAssociation, UUID>