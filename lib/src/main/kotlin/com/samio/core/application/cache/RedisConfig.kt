package com.samio.core.application.cache

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer


@Configuration
open class RedisConfig {

    @Value("\${spring.data.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    private var redisPort: Int = 0

    @Bean
    open fun redisConnectionFactory(): RedisConnectionFactory {
        println("RedisConfig initialized with host=$redisHost and port=$redisPort")
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    //Todo: is this still used anywhere?
    @Bean
    open fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, ByteArray> {
        return RedisTemplate<String, ByteArray>().apply {
            this.connectionFactory = connectionFactory
            this.keySerializer = RedisSerializer.string()
            this.valueSerializer = RedisSerializer.byteArray()
            afterPropertiesSet()
        }
    }
}
