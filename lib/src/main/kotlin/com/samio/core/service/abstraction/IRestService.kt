package com.samio.core.service.abstraction

import com.samio.core.controller.abstraction.request.CreateRequest
import com.samio.core.controller.abstraction.request.SearchRequest
import com.samio.core.controller.abstraction.request.UpdateRequest
import com.samio.core.model.abstraction.BaseEntity
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import java.util.*

interface IRestService<T: BaseEntity> {
    fun update(request: UpdateRequest): T?
    fun getSingle(id: UUID): T
    fun getMultiple(ids: List<UUID>, page: Int, size: Int): Page<T>
    fun getAllPaged(page: Int, size: Int): Page<T>
    fun search(request: SearchRequest, page: Int, size: Int): Page<T>
    fun create(request: CreateRequest): T?
    fun delete(id: UUID) : HttpStatus
}