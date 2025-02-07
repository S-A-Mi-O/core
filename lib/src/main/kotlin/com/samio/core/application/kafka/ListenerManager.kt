package com.samio.core.application.kafka

import com.samio.core.application.cache.RedisService
import com.samio.core.application.kafka.handling.MainEventHandler
import com.samio.core.model.abstraction.BaseEntity
import com.samio.core.model.concretion.permission.Permission
import com.samio.core.model.concretion.permissionuserassociation.PermissionUserAssociation
import com.samio.core.service.concretion.RepositoryScanner
import jakarta.annotation.PostConstruct
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.DependsOn
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@DependsOn("repositoryScanner")
@Service
class ListenerManager<T : BaseEntity> @Autowired constructor(
    private val redisService: RedisService,
    private val repositoryScanner: RepositoryScanner,
    private val kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, Any>,
    private val mainEventHandler: MainEventHandler<T>,
    @Value("\${spring.application.name}") private val serviceName: String
) {
    private val log = LoggerFactory.getLogger(ListenerManager::class.java)

    val listenerContainers = mutableMapOf<String, MessageListenerContainer>()
    lateinit var downstreamEntities: List<String>

    //Todo: can this be replaced with init block?
    @PostConstruct
    fun init() {
        if (serviceName.isBlank()) {
            throw IllegalStateException(
                "The service name is not configured. " +
                        "Please set 'spring.application.name' in your application.yml or properties.")
        }
        log.info("Service name is: $serviceName")

        log.info("Scanning for downstream entities")
        downstreamEntities = repositoryScanner.getDownstreamEntityNames()
        log.info("Downstream entities found: $downstreamEntities")
        manageListeners()
    }

    //Todo: pollutes the log, count is broken
    @Scheduled(fixedRate = 30000, initialDelay = 10000)
    fun manageListeners() {
        if (serviceName == "user-service") {
            createKafkaListener("permissionUserAssociation")
        }
        if (downstreamEntities.isEmpty()) {
            log.warn("No downstream entities found. No listeners to manage.")
            return
        }

        val kafkaTopics = redisService.getKafkaRegistry()
        log.info("Kafka topics fetched from Redis: $kafkaTopics")

        downstreamEntities.forEach { prefixedEntity ->
            val topic = prefixedEntity.removePrefix("_").replaceFirstChar { it.lowercaseChar() }
            val topicDetails = kafkaTopics.topics[topic]

            if (topicDetails != null) {
                if (!listenerContainers.containsKey(topic)) {
                    try {
                        createKafkaListener(topic)
                        redisService.registerConsumer(topic)
                        log.info("Listener successfully created and registered for topic: $topic")
                    } catch (e: Exception) {
                        log.error("Failed to create listener for topic: $topic", e)
                    }
                }
            } else if (listenerContainers.containsKey(topic)
                && topic != Permission::class.simpleName?.replaceFirstChar { it.lowercase() }
                && topic != PermissionUserAssociation::class.simpleName?.replaceFirstChar { it.lowercase() }) {
                stopKafkaListener(prefixedEntity)
                log.info("Listener stopped for topic: $topic")
            }
        }
    }

    fun createKafkaListener(topic: String) {
        log.info("Creating Kafka listener for topic: $topic")
        val groupId = "$serviceName-$topic"

        val containerProperties = ContainerProperties(topic).apply {
            this.groupId = groupId
            this.messageListener = MessageListener<String, Any> { record ->
                log.info("Received message from topic $topic: ${record.value()}")
                try {
                    val event = record.value() as? EntityEvent
                        ?: throw IllegalArgumentException("Invalid event type received from topic $topic")
                    mainEventHandler.handle(event)
                } catch (e: Exception) {
                    log.error("Error while processing message from topic $topic", e)
                }
            }
        }

        val listenerContainer = KafkaMessageListenerContainer(
            kafkaListenerContainerFactory.consumerFactory,
            containerProperties
        )

        listenerContainer.start()
        listenerContainers[topic] = listenerContainer
        log.info("Kafka listener started for topic: $topic with group ID: $groupId")
    }


    fun stopKafkaListener(topic: String) {
        listenerContainers[topic]?.let { container ->
            try {
                container.stop()
                log.info("Successfully stopped listener for topic: $topic")
            } catch (e: WakeupException) {
                log.warn("Listener wakeup during stop for topic $topic", e)
            } catch (e: Exception) {
                log.error("Error stopping listener for topic: $topic", e)
            }
            listenerContainers.remove(topic)
        }
    }
}
