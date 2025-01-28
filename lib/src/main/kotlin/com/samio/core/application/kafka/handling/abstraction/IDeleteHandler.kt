package com.samio.core.application.kafka.handling.abstraction

import com.samio.core.model.abstraction.BaseEntity

interface IDeleteHandler<T: BaseEntity> : IEventTypeHandler<T>