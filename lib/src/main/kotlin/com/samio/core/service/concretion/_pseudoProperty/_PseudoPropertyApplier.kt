package com.samio.core.service.concretion._pseudoProperty

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.util.concurrent.RateLimiter
import com.samio.core.application.kafka.EntityEventProducer
import com.samio.core.application.validation.modification.ModificationType
import com.samio.core.model.abstraction.AugmentableBaseEntity
import com.samio.core.persistence.abstraction.PersistencePort
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Service
@Suppress("UNCHECKED_CAST", "unused", "ClassName")
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
//Todo: rate limit for other methods as well
open class _PseudoPropertyApplier(
    private val beanFactory: BeanFactory,
    private val eventProducer: EntityEventProducer,
) {
    private val objectMapper = jacksonObjectMapper()
    private val rateLimiter = RateLimiter.create(100.0)

    val log = KotlinLogging.logger {}

    @Async
    @Transactional
    open fun addPseudoPropertyToAllEntitiesOfType(
        entityClass: Class<out AugmentableBaseEntity>,
        key: String
    ) {
        val adapter = getAdapter(entityClass)
        val pageSize = 100
        var page = 0

        do {
            val pagedEntities = adapter.getAllPaged(page, pageSize)

            if (pagedEntities.hasContent()) {
                val entities = pagedEntities.content

                entities.forEach { entity ->
                    entity.getPseudoProperty(key)?.let {
                       log.info { "Entities of type ${entityClass.simpleName} already contain the key '$key'. Cannot override." }
                       return
                    } ?:entity.addPseudoProperty(key, null)
                }

                adapter.saveAll(entities)

                entities.forEach { entity ->
                    rateLimiter.acquire()
                    eventProducer.emit(
                        entity.javaClass.simpleName,
                        entity.id,
                        ModificationType.CREATE,
                        getChanges(entity)
                    )
                }
            }
            page++
        } while (pagedEntities.hasNext())
    }


    @Transactional
    @Async
    open fun renamePseudoPropertyForAllEntitiesOfType(
        entityClass: Class<out AugmentableBaseEntity>,
        oldKey: String,
        newKey: String
    ) {
        val adapter = getAdapter(entityClass)
        val pageSize = 100
        var page = 0

        do {
            val pagedEntities = adapter.getAllPaged(page, pageSize)
            if (pagedEntities.hasContent()) {
                val entities = pagedEntities.content

                entities.forEach { entity ->
                        entity.renamePseudoProperty(oldKey, newKey)
                }

                adapter.saveAll(entities)
                entities.forEach { entity ->
                    eventProducer.emit(
                        entity.javaClass.simpleName,
                        entity.id,
                        ModificationType.UPDATE,
                        getChanges(entity)
                    )
                }
            }

            page++
        } while (pagedEntities.hasNext())
    }

    @Transactional
    open fun deletePseudoPropertyForAllEntitiesOfType(
        entityClass: Class<out AugmentableBaseEntity>,
        key: String
    ) {
        val adapter = getAdapter(entityClass)
        val pageSize = 1000
        var page = 0

        do {
            val pagedEntities = adapter.getAllPaged(page, pageSize)

            if (pagedEntities.hasContent()) {
                val entities = pagedEntities.content

                entities.forEach { it.removePseudoProperty(key) }

                adapter.saveAll(entities)
                entities.forEach { entity ->
                    eventProducer.emit(
                        entity.javaClass.simpleName,
                        entity.id,
                        ModificationType.DELETE,
                        getChanges(entity)
                    )
                }
            }

            page++
        } while (pagedEntities.hasNext())
    }

    private fun getAdapter(entityClass: Class<*>): PersistencePort<AugmentableBaseEntity> {
        val adapterName = "${entityClass.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }}PersistenceAdapter"
        val adapter = try {
            beanFactory.getBean(adapterName)
        } catch (e: NoSuchBeanDefinitionException) {
            throw IllegalArgumentException("Adapter for entity class '${entityClass.simpleName}' not found.", e)
        }
        return adapter as? PersistencePort<AugmentableBaseEntity>
            ?: throw IllegalArgumentException("Adapter for '${entityClass.simpleName}' is not a IEntityPersistenceAdapter.")
    }

    private fun getChanges(entity: AugmentableBaseEntity): MutableMap<String, Any?> =
        mutableMapOf(entity::pseudoProperties.name to entity.pseudoProperties)
}
