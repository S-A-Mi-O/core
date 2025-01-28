package com.samio.core.controller.abstraction

import com.samio.core.controller.abstraction.request.SearchRequest
import com.samio.core.model.abstraction.BaseEntity
import com.samio.core.service.abstraction.DownstreamRestServiceTemplate
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

abstract class DownstreamRestControllerTemplate<T : BaseEntity> {

    @Autowired
    private lateinit var service: DownstreamRestServiceTemplate<T>

    @Operation(summary = "Retrieve an entity by ID.")
    @GetMapping("/{id}")
    open fun getSingle(
        @PathVariable id: UUID
    ): ResponseEntity<T> {
        return ResponseEntity.ok(service.getSingle(id))
    }

    @Operation(summary = "Retrieve multiple entities. Takes a set of IDs and returns a page of entities. Default values are page=0 and size=1000.")
    @GetMapping
    open fun getMultiple(
        @RequestParam ids: List<UUID>,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1000") size: Int,
    ): ResponseEntity<Page<T>> {
        return ResponseEntity.ok(service.getMultiple(ids, page, size))
    }

    @Operation(summary = "Retrieve all entities. Returns a page of entities. Default values are page=0 and size=1000.")
    @GetMapping("/all")
    open fun getAllPaged(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1000") size: Int,
    ): ResponseEntity<Page<T>> {
        return ResponseEntity.ok(service.getAllPaged(page, size))
    }

    @Operation(summary = "Search for entities. Use the SearchRequest object to specify search criteria. Default values are page=0 and size=1000.")
    @GetMapping("/search")
    open fun search(
        @RequestBody request: SearchRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "1000") size: Int,
    ): ResponseEntity<Page<T>> {
        return ResponseEntity.ok(service.search(request, page, size))
    }
}
