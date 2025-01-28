package com.samio.core.controller.concretion.permission

import com.samio.core.controller.abstraction.RestControllerTemplate
import com.samio.core.controller.annotation.ControllerFor
import com.samio.core.model.concretion.permission.Permission
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Permissions API", description = "Manages permissions in the system")
@RestController
@RequestMapping("/permissions")
@ControllerFor(Permission::class)
@ConditionalOnProperty(name = ["permissions"], havingValue = "true", matchIfMissing = false)
class PermissionController : RestControllerTemplate<Permission>()