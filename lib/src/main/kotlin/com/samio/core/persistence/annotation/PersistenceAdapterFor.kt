package com.samio.core.persistence.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PersistenceAdapterFor(val entity: KClass<*>)
