package com.samio.core.controller.abstraction.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.samio.core.model.abstraction.AugmentableBaseEntity
import com.samio.core.model.abstraction.BaseEntity
import com.samio.core.persistence.concretion._pseudoProperty._PseudoPropertyRepository
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PathResolver(
    private val validator: com.samio.core.controller.abstraction.util.SearchParamValidation,
    private val converter: SearchParamConverter,
    @Autowired(required = false) private val _pseudoPropertyRepository: _PseudoPropertyRepository? = null,
    private val objectMapper: ObjectMapper,

    ) {
    fun <T : BaseEntity> resolvePath(params: SearchParam, root: Root<T>): ResolvedSearchParam {
        val segments = params.path.split(".")
        var currentPath: Path<*> = root
        var currentClass: Class<*> = root.javaType

        val registeredPseudoPropertyTypesMap =
            _pseudoPropertyRepository?.findAllByEntitySimpleName(
                currentClass.simpleName
            )
                ?.associate { it.key to it.typeDescriptor.type.typeInfo }
        segments.forEachIndexed { index, segment ->
            validator.validateFieldExistsAndIsAccessible(segment, currentClass)
            if (segment == AugmentableBaseEntity::pseudoProperties.name) {
                val jsonSegments = segments.drop(index + 1)

                val relevantSegment = jsonSegments.find { jsonSegment ->
                    jsonSegment == params.path.substringAfterLast(".")
                } ?: throw IllegalArgumentException("PseudoProperty not found")

                val expectedType = registeredPseudoPropertyTypesMap?.get(relevantSegment)
                    ?: throw IllegalArgumentException("PseudoProperty type not found")

                val rawSegmentValue = converter.convertAnyIfNeeded(params.searchValue, expectedType)

                val serializedSegmentValue = when (rawSegmentValue) {
                    is String -> "\"$rawSegmentValue\""
                    null -> null
                    else -> objectMapper.writeValueAsString(rawSegmentValue)
                }

                val result = ResolvedSearchParam(
                    deserializedValue = serializedSegmentValue,
                    jpaPath = currentPath.get<Any>(segment),
                    jsonSegments = jsonSegments
                )
                return result
            } else {
                currentPath = currentPath.get<Any>(segment)
                currentClass = currentPath.model.bindableJavaType
            }
        }
        val actualValue = converter.convertAnyIfNeeded(params.searchValue, currentClass)
        validator.validate(actualValue, currentPath.model.bindableJavaType, currentClass.kotlin, currentPath.toString())
        return ResolvedSearchParam(actualValue, jpaPath = currentPath, jsonSegments = emptyList())
    }

}
