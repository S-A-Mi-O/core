package com.samio.core.application.springboot

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class EndpointMetadata @JsonCreator constructor(
    @JsonProperty("path") val path: String,
    @JsonProperty("method") val method: String,
    @JsonProperty("roles") val roles: Set<String>,
    @JsonProperty("pathVariables") val pathVariables: List<EndpointMethodParam>,
    @JsonProperty("requestParameters") val requestParameters: List<EndpointMethodParam>
)