package com.samio.core.model.concretion.permissionuserassociation

import com.samio.core.model.abstraction.BaseEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import java.util.*

@Schema(description = "Represents a permission entity.")
@Entity
@Table(
    name = "permission_user_association",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["service_of_origin", "permission_id", "user_id"])
    ]
)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
open class PermissionUserAssociation(
    @Column(name = "service_of_origin", nullable = false)
    open var serviceOfOrigin: String = "",
    @Column(name = "permission_id", nullable = false)
    open var permissionId: UUID? = null,
    @Column(name = "user_id", nullable = false)
    open var userId: UUID? = null
) : BaseEntity()