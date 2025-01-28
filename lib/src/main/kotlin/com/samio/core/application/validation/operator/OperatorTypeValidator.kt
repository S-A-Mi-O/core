package com.samio.core.application.validation.operator

import com.samio.core.controller.abstraction.util.SearchParam
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

object OperatorTypeValidator : ConstraintValidator<ValidOperator, SearchParam> {

    override fun isValid(searchParam: SearchParam, context: ConstraintValidatorContext): Boolean {
        val operator = searchParam.operator
        val value = searchParam.searchValue
        return value == null || operator.isSupportedType(value::class)
    }
}