package com.samio.core.application.kafka

import com.samio.core.application.cache.RedisService
import mu.KotlinLogging
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.stereotype.Service

@Service
class DynamicTopicRegistration @Autowired constructor(
    private val redisService: RedisService,
    private val kafkaAdmin: KafkaAdmin
) {

    @Value("\${kafka.default.partitions:1}")
    val defaultPartitions: Int = 1

    @Value("\${kafka.default.replication-factor:1}")
    val defaultReplicationFactor: Int = 1

    val log = KotlinLogging.logger {}

    fun declareKafkaTopics(upstreamEntityNames: List<String>) {
        log.info { "Registering Kafka topics: $upstreamEntityNames" }
        redisService.registerAsTopics(upstreamEntityNames)
        upstreamEntityNames.forEach { topicName ->
            val topic: NewTopic = TopicBuilder.name(topicName)
                .partitions(defaultPartitions)
                .replicas(defaultReplicationFactor)
                .build()

            kafkaAdmin.createOrModifyTopics(topic)
        }
    }
}