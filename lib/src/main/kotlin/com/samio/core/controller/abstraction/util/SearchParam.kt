package com.samio.core.controller.abstraction.util

import com.samio.core.application.validation.operator.ValidOperator

@ValidOperator
data class SearchParam(
    val operator: Operator,
    val searchValue: Any? = null,
    val path: String // "username", "userInfo.address.city", "userInfo.pseudoProperties.occupation.company"
)
