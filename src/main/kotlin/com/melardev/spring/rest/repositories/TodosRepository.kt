package com.melardev.spring.rest.repositories

import com.melardev.spring.rest.entities.Todo
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface TodosRepository : ReactiveCrudRepository<Todo, String> {

    // this will not work, Spring data would still treat this
    // method as a query method and the name is not meaningful at all
    // @Query(fields = "{description: 0}", exists = true)

    // Get all documents, all fields except description
    // @Query(fields = "{description: 0}", value = "{id: {$exists:true}}")
    // Get all documents, specified fields only. Id:1 is optional, if skipped then it will be 1 anyways
    @Query(fields = "{id: 1, title: 1, completed:1, createdAt: 1, updatedAt:1}", value = "{id: {\$exists:true}}")
    fun findAllHqlSummary(): Flux<Todo>

    @Query(fields = "{id: 1, title: 1, completed:1, createdAt: 1, updatedAt:1}", value = "{id: {\$exists:true}}")
    fun findAllHqlSummary(pageable: Pageable): Flux<Todo>

    // This is treated as a query method!!! even using @Query, because we have only set fields arg, and not value
    @Query(fields = "{description:0}")
    fun findByIsCompletedFalse(): Flux<Todo>

    // This is not a query method, why? notice the value arg is set.
    @Query(fields = "{description:0}", value = "{isCompleted: false}")
    fun findByIsCompletedFalseHql(): Flux<Todo>

    @Query(fields = "{description:0}", value = "{isCompleted: false}")
    fun findByIsCompletedFalseHql(pageable: Pageable): Flux<Todo>

    // This is a Spring Data query method
    @Query(fields = "{description:0}")
    fun findByIsCompletedIsTrue(): Flux<Todo>

    @Query(fields = "{description:0}")
    fun findByIsCompletedIsTrue(pageable: Pageable): Flux<Todo>

    @Query(fields = "{description:0}", value = "{isCompleted: true}")
    fun findByIsCompletedIsTrueHql(): Flux<Todo>

    @Query(fields = "{description:0}", value = "{isCompleted: true}")
    fun findByIsCompletedIsTrueHql(pageable: Pageable): Flux<Todo>

    fun findByIsCompleted(completed: Boolean): Flux<Todo>
    fun findByIsCompletedIs(completed: Boolean): Flux<Todo>

    @Query(fields = "{description:0}", value = "{isCompleted: ?0}")
    fun findByHqlIsCompleted(completed: Boolean): Flux<Todo>

    fun findByIsCompletedTrue(): Flux<Todo>

    fun findByIsCompletedIsFalse(): Flux<Todo>

    fun findByTitleContains(title: String): Flux<Todo>

    fun findByDescriptionContains(description: String): Flux<Todo>

    fun findByTitleAndDescription(title: String, description: String): Mono<Todo>

    fun findByCreatedAtAfter(date: LocalDateTime): List<Todo>

    fun findByCreatedAtBefore(date: LocalDateTime): Flux<Todo>

    fun findByDescriptionContains(description: Mono<String>): Flux<Todo>

    fun findByTitleAndDescription(title: Mono<String>, description: String): Mono<Todo>


    @Query(value = "{'isCompleted': false}", count = true)
    fun countByHqlCompletedFalse(): Mono<Long>

    fun countByIsCompletedIsFalse(): Mono<Long>

    fun countByIsCompletedFalse(): Mono<Long>

    @Query(value = "{'isCompleted': true}", count = true)
    fun countByHqlCompletedTrue(): Mono<Long>

    fun countByIsCompletedIsTrue(): Mono<Long>

    fun countByIsCompletedTrue(): Mono<Long>

    fun countByIsCompleted(completed: Boolean): Mono<Long>

    @Query(value = "{'isCompleted': ?0}", count = true)
    fun countByHqlCompleted(completed: Boolean): Mono<Long>

}