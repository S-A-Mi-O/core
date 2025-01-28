package com.samio.core.application.springboot

import com.samio.core.application.kafka.DynamicTopicRegistration
import com.samio.core.service.concretion.DiscoveryService
import com.samio.core.service.concretion.RepositoryScanner
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ApplicationStartup @Autowired constructor(
    private val dynamicTopicRegistration: DynamicTopicRegistration,
    private val repositoryScanner: RepositoryScanner,
    private val serviceLevelAccess: ServiceLevelAccess,
    private val discoveryService: DiscoveryService
) {

    @PostConstruct
    fun init() {
        println("SERVICE LEVEL RESTRICTIONS: $serviceLevelAccess")
        val upstreamEntityNames = repositoryScanner.getUpstreamEntityNames()
        dynamicTopicRegistration.declareKafkaTopics(upstreamEntityNames)
        val enrichedEndpointMetadata = discoveryService.extractEndpointMetadata()
        discoveryService.registerEndpointInfoToEureka(enrichedEndpointMetadata)
    }

}