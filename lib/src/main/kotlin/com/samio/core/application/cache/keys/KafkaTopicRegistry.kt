package com.samio.core.application.cache.keys

import com.fasterxml.jackson.annotation.JsonProperty
import com.samio.core.application.cache.values.TopicDetails

data class KafkaTopicRegistry(
    @JsonProperty("kafka-topic-registry") val topics: MutableMap<String, TopicDetails?> = mutableMapOf(),
)