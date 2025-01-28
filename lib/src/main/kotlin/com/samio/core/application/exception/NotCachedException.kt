package com.samio.core.application.exception

class NotCachedException : RuntimeException("Not cached") {
    override fun toString(): String {
        return "NotCachedException(message=${message})"
    }
}