package com.samio.core.service.concretion

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
class RepositoryScanner @Autowired constructor(
    private val applicationContext: ApplicationContext,
) {
    val log = KotlinLogging.logger {}
    private fun getPrimaryEntityNames(filterCondition: (String) -> Boolean): List<String> {
        val result = applicationContext.getBeanNamesForType(JpaRepository::class.java)
            .map { it.dropLast(10) }
            .filter(filterCondition)
        log.info { "Found ${result.size} repositories with primary entities: $result" }
        return result
    }

    fun getUpstreamEntityNames(): List<String> {
        return getPrimaryEntityNames { !it.startsWith("_") }
    }

    fun getDownstreamEntityNames(): List<String> {
        return getPrimaryEntityNames { it.startsWith("_") }
    }
}