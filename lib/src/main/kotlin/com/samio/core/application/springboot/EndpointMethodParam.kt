package com.samio.core.application.springboot

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EndpointMethodParam @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("position") val position: Int,
    @JsonProperty("typeSimpleName") val typeSimpleName: String
)
