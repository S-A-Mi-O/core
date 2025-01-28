package com.samio.core.application.exception

class ValueTypeMismatchException(attributePath: String, expectedType: String, actualType: String) :
    RuntimeException("Type mismatch for '$attributePath': expected $expectedType, got $actualType")