package com.samio.core.controller.abstraction.util


import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.samio.core.application.validation.type.TypeCategory
import com.samio.core.application.validation.type.ValueType

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "category"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = TypeDescriptor.PrimitiveDescriptor::class, name = "PRIMITIVE"),
    JsonSubTypes.Type(value = TypeDescriptor.TimeDescriptor::class, name = "TIME"),
    JsonSubTypes.Type(value = TypeDescriptor.CollectionDescriptor::class, name = "COLLECTION"),
    JsonSubTypes.Type(value = TypeDescriptor.MapDescriptor::class, name = "MAP"),
    JsonSubTypes.Type(value = TypeDescriptor.ComplexObjectDescriptor::class, name = "COMPLEX"),
    JsonSubTypes.Type(value = TypeDescriptor.EnumDescriptor::class, name = "ENUM"),
)
sealed class TypeDescriptor {
    abstract val category: String
    abstract val type: ValueType

    data class PrimitiveDescriptor(
        override val category: String = TypeCategory.PRIMITIVE.name,
        override val type: ValueType,
        val isNullable: Boolean
    ) : TypeDescriptor()

    data class TimeDescriptor(
        override val category: String = TypeCategory.TIME.name,
        override val type: ValueType,
        val isNullable: Boolean
    ) : TypeDescriptor()

    data class CollectionDescriptor(
        override val category: String = TypeCategory.COLLECTION.name,
        override val type: ValueType,
        val itemDescriptor: TypeDescriptor,
        val minElements: Int,
        val maxElements: Int?
    ) : TypeDescriptor()

    data class MapDescriptor(
        override val category: String = TypeCategory.MAP.name,
        override val type: ValueType,
        val keyDescriptor: TypeDescriptor,
        val valueDescriptor: TypeDescriptor,
        val minEntries: Int,
        val maxEntries: Int?
    ) : TypeDescriptor()

    data class ComplexObjectDescriptor(
        override val category: String = TypeCategory.COMPLEX.name,
        override val type: ValueType,
        val isNullable: Boolean,
        val fields: Map<String, TypeDescriptor>
    ) : TypeDescriptor()

    data class EnumDescriptor(
        override val category: String = TypeCategory.ENUM.name,
        override val type: ValueType,
        val isNullable: Boolean,
        val enumValues: List<String>
    ) : TypeDescriptor()
}

