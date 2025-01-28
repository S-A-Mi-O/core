package com.samio.core.persistence.concretion.permission

import com.samio.core.model.concretion.permission.Permission
import com.samio.core.persistence.abstraction.EntityRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import java.util.*

@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
interface PermissionRepository : EntityRepository<Permission, UUID>