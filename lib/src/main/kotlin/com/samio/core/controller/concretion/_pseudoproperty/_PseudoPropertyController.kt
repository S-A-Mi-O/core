package com.samio.core.controller.concretion._pseudoproperty

import com.samio.core.controller.abstraction.DownstreamRestControllerTemplate
import com.samio.core.controller.annotation.ControllerFor
import com.samio.core.model.concretion._pseudoProperty._PseudoProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pseudo-properties")
@ControllerFor(_PseudoProperty::class)
@ConditionalOnProperty(name = ["pseudo-properties"], havingValue = "true", matchIfMissing = false)
@Suppress("ClassName")
class _PseudoPropertyController: DownstreamRestControllerTemplate<_PseudoProperty>()