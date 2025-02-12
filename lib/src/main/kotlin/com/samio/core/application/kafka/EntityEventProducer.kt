package com.samio.core.application.kafka

import com.samio.core.application.cache.RedisService
import com.samio.core.application.validation.modification.ModificationType
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class EntityEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val redisService: RedisService,
) {
    val log = KotlinLogging.logger {}

    fun emit(
        entityClassName: String,
        id: UUID,
        modificationType: ModificationType,
        properties: MutableMap<String, Any?>
    ) {
        val kafkaRegistry = redisService.getKafkaRegistry()
        val topic = entityClassName.replaceFirstChar { it.lowercaseChar() }
        if (kafkaRegistry.topics.containsKey(topic)) {
            val event = EntityEvent(
                entityClassName = entityClassName,
                id = id,
                type = modificationType,
                properties = properties
            )

            kafkaTemplate.send(topic, event)
            log.info("Produced event for topic: $topic, event: $event")
        } else {
            throw IllegalArgumentException("Topic $topic is not registered in Redis.")
        }
    }

}