package com.samio.core.application.kafka.handling.abstraction

import com.samio.core.model.abstraction.BaseEntity

interface ICreateHandler<T: BaseEntity> : IEventTypeHandler<T>
