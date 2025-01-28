package com.samio.core.controller.abstraction.util

import jakarta.persistence.criteria.Path

data class ResolvedSearchParam(
    val deserializedValue: Any?,
    val jpaPath: Path<*>,
    val jsonSegments: List<String>
)