package com.samio.core.application.cache.values

data class TopicDetails(
    var producer: Microservice,
    val consumers: MutableSet<Microservice>
)