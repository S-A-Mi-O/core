package com.samio.core.model.abstraction

import com.samio.core.controller.abstraction.util.TypeDescriptor

interface IPseudoProperty {
    var entitySimpleName: String
    var key: String
    var typeDescriptor: TypeDescriptor
}