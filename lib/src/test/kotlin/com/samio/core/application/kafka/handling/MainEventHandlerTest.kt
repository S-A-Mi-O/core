package com.samio.core.application.kafka.handling

import com.samio.core.application.exception.FailedToHandleEventException
import com.samio.core.application.kafka.EntityEvent
import com.samio.core.application.kafka.handling.abstraction.ICreateHandler
import com.samio.core.application.validation.modification.ModificationType
import com.samio.core.model.abstraction.BaseEntity
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.context.ApplicationContext

class MainEventHandlerTest {

    private lateinit var applicationContext: ApplicationContext
    private lateinit var mainEventHandler: MainEventHandler<BaseEntity>

    @BeforeEach
    fun setUp() {
        applicationContext = mock(ApplicationContext::class.java)
        mainEventHandler = MainEventHandler(applicationContext)
    }

    @Test
    fun `should log and rethrow exception on failure`() {
        // Arrange
        val entityClassName = "TestEntity"
        val event = EntityEvent(
            entityClassName = entityClassName,
            id = java.util.UUID.randomUUID(),
            type = ModificationType.CREATE,
            properties = emptyMap()
        )
        val createHandler = mock(ICreateHandler::class.java) as ICreateHandler<BaseEntity>
        `when`(
            applicationContext.getBeansOfType(ICreateHandler::class.java)
        ).thenReturn(mapOf("createHandler" to createHandler))
        `when`(createHandler.applyChanges(event)).thenThrow(RuntimeException("Mocked exception"))

        // Act & Assert
        val exception = assertThrows<FailedToHandleEventException> {
            mainEventHandler.handle(event)
        }
        assertTrue(exception.message!!.contains("Failed to handle event"))
    }
}
