package com.samio.core.service.abstraction

import com.samio.core.controller.abstraction.request.SearchRequest
import com.samio.core.model.abstraction.BaseEntity
import org.springframework.data.domain.Page
import java.util.*

interface IDownstreamRestService<T: BaseEntity> {
    fun getSingle(id: UUID): T
    fun getMultiple(ids: List<UUID>, page: Int, size: Int): Page<T>
    fun getAllPaged(page: Int, size: Int): Page<T>
    fun search(request: SearchRequest, page: Int, size: Int): Page<T>
}