package com.samio.core.controller.abstraction.util

import com.samio.core.application.exception.InvalidAttributeException
import com.samio.core.application.exception.ValueTypeMismatchException
import com.samio.core.service.concretion.ReflectionService
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
@Suppress("unused")
class SearchParamValidation(
    private val deserializer: com.samio.core.controller.abstraction.util.SearchParamConverter,
    private val reflectionService: ReflectionService,
) {

    fun validate(value: Any?, expectedType: Class<*>, declaringClass: KClass<*>, attributePath: String) {
        if (value == null && reflectionService.getClassMemberProperties(declaringClass)
                .find { it.name == attributePath }
                ?.returnType
                ?.isMarkedNullable == true
        ) {
            throw ValueTypeMismatchException(
                attributePath = attributePath,
                expectedType = expectedType.simpleName ?: "Unknown",
                actualType = "null"
            )
        }

        if (value is Collection<*>) {
            validateCollectionElements(value, expectedType, attributePath)
        } else if (!expectedType.isInstance(value)) {
            throw ValueTypeMismatchException(
                attributePath = attributePath,
                expectedType = expectedType.simpleName ?: "Unknown",
                actualType = value?.let { value::class.simpleName ?: "Unknown" } ?: "null"
            )
        }
    }

    private fun validateCollectionElements(collection: Collection<*>, expectedType: Class<*>, attributePath: String) {
        collection.forEach { element ->
            if (element != null && !expectedType.isInstance(element)) {
                throw ValueTypeMismatchException(
                    attributePath = attributePath,
                    expectedType = expectedType.simpleName ?: "Unknown",
                    actualType = element::class.simpleName ?: "Unknown"
                )
            }
        }
    }

    fun validateFieldExistsAndIsAccessible(segment: String, currentClass: Class<*>) {
        var classToCheck: Class<*>? = currentClass

        while (classToCheck != null) {
            try {
                val field = classToCheck.getDeclaredField(segment)
                field.isAccessible = true
                return
            } catch (e: NoSuchFieldException) {
                classToCheck = classToCheck.superclass
            }
        }

        throw InvalidAttributeException(segment, currentClass.simpleName)
    }
}
