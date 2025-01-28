package com.samio.core.application.springboot

import com.samio.core.controller.annotation.ControllerFor
import com.samio.core.persistence.annotation.PersistenceAdapterFor
import com.samio.core.service.annotation.EventServiceFor
import com.samio.core.service.annotation.RestServiceFor
import mu.KotlinLogging
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata
import java.util.*

class TemplateBeanRegistrar : ImportBeanDefinitionRegistrar {

    val log = KotlinLogging.logger {}

    override fun registerBeanDefinitions(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanner()

        scanner.findClassesWithAnnotation(RestServiceFor::class).forEach { clazz ->
            registerWithEntityClass(clazz, registry)
        }
        scanner.findClassesWithAnnotation(EventServiceFor::class).forEach { clazz ->
            registerWithEntityClass(clazz, registry)
        }
        scanner.findClassesWithAnnotation(PersistenceAdapterFor::class).forEach { clazz ->
            val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).beanDefinition
            registry.registerBeanDefinition(
                clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                beanDefinition
            )
        }
        scanner.findClassesWithAnnotation(ControllerFor::class).forEach { clazz ->
            val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz).beanDefinition
            registry.registerBeanDefinition(
                clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
                beanDefinition
            )
        }
    }

    fun registerWithEntityClass(
        clazz: Class<*>,
        registry: BeanDefinitionRegistry,
    ) {
        val entityClass = when {
            clazz.isAnnotationPresent(RestServiceFor::class.java) -> {
                clazz.getAnnotation(RestServiceFor::class.java).entity
            }

            clazz.isAnnotationPresent(EventServiceFor::class.java) -> {
                clazz.getAnnotation(EventServiceFor::class.java).entity
            }

            else -> throw IllegalStateException("No valid annotation found on class ${clazz.name} in registrar")
        }

        val parentConstructor = clazz.superclass.constructors.firstOrNull()
            ?: throw IllegalStateException("No constructor found for parent class of ${clazz.simpleName}")

        val dependencies = parentConstructor.parameterTypes.map { paramType ->
            when (paramType) {
                entityClass::class.java -> entityClass
                else -> SpringContextProvider.applicationContext.getBean(paramType)
            }
        }

        val beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz)
        dependencies.forEach { dep -> beanDefinition.addConstructorArgValue(dep) }
        val normalizedBeanName = clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }
        log.info("Registering bean with name: $normalizedBeanName")
        registry.registerBeanDefinition(
            clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            beanDefinition.beanDefinition
        )
    }

}
