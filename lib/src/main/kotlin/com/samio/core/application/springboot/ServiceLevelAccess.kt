package com.samio.core.application.springboot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "service-level-access")
data class ServiceLevelAccess(
    var restrictedTo: List<String> = emptyList()
)