package com.samio.core.controller.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AccessRestrictedToRoles(val roles: Array<String>)