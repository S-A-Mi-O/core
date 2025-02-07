package com.samio.core.persistence.abstraction

import com.samio.core.model.abstraction.BaseEntity
import com.samio.core.persistence.annotation.PersistenceAdapterFor
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import jakarta.persistence.Tuple
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.Lock
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Suppress("UNCHECKED_CAST")
abstract class EntityPersistenceAdapter<T : BaseEntity> : PersistencePort<T> {
    private var entityClass: KClass<T> = this::class.findAnnotation<PersistenceAdapterFor>()?.let { it.entity as KClass<T> }
        ?: throw IllegalStateException("No valid annotation found on class ${this::class.simpleName}")

    @Autowired
    private lateinit var repository: EntityRepository<T, UUID>

    @Autowired
    private lateinit var entityManager: EntityManager

    val log = KotlinLogging.logger {}

    override fun save(entity: T): T {
        val result = repository.save(entity) as T
        log.info { "Entity saved: $result" }
        return result
    }

    override fun saveAll(entities: List<T>): List<T> {
        val result = repository.saveAll(entities) as List<T>
        log.info { "Entities saved: $result" }
        return result
    }

    override fun delete(id: UUID) {
        try {
            repository.deleteById(id)
            log.info { "Entity with id $id deleted" }
        } catch (e: NoSuchElementException) {
            log.warn { "${e.message}" }
            log.debug { "${e.stackTrace}" }
        }

    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun getById(id: UUID): T {
        return repository.findById(id).orElseThrow { NoSuchElementException("Entity not found") }
    }

    override fun getAllByIds(ids: List<UUID>, page: Int , size: Int): Page<T> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Tuple::class.java)
        val root = criteriaQuery.from(entityClass.java)
        val pageable = PageRequest.of(page, size)


        val entityAlias = "entity"
        val countAlias = "totalCount"

        criteriaQuery.multiselect(
            root.alias(entityAlias),
            criteriaBuilder.count(root).alias(countAlias)
        )
        criteriaQuery.groupBy(root.get<UUID>("id"))
        criteriaQuery.where(root.get<UUID>("id").`in`(ids))

        val resultList = entityManager.createQuery(criteriaQuery)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList


        val entities = resultList.map { it.get("entity", entityClass.java) }
        val totalCount = resultList.firstOrNull()?.get("totalCount", Long::class.java) ?: 0L

        return PageImpl(entities, pageable, totalCount)
    }

    override fun getAllPaged(page: Int, size: Int): Page<T> {
        val pageable = PageRequest.of(page, size)
        return repository.findAll(pageable)
    }
}
