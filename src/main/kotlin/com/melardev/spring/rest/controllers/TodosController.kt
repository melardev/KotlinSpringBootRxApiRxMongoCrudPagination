package com.melardev.spring.rest.controllers

import com.melardev.spring.rest.dtos.responses.*
import com.melardev.spring.rest.entities.Todo
import com.melardev.spring.rest.repositories.TodosRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/todos")
class TodosController(@Autowired
                      private val todosRepository: TodosRepository) {

    @GetMapping
    fun getAll(request: ServerHttpRequest,
               @RequestParam(value = "page", defaultValue = "1") page: Int,
               @RequestParam(value = "page_size", defaultValue = "5") pageSize: Int): Mono<out AppResponse> {
        val todos = todosRepository.findAllHqlSummary(PageRequest.of(page - 1, pageSize))
        return getResponseFromTodosFlux(todos, todosRepository.count(), request, page, pageSize)
    }

    @GetMapping("/pending")
    fun getPending(request: ServerHttpRequest,
                   @RequestParam(value = "page", defaultValue = "1") page: Int,
                   @RequestParam(value = "page_size", defaultValue = "5") pageSize: Int): Mono<out AppResponse> {
        val todos = todosRepository.findByIsCompletedFalseHql(PageRequest.of(page - 1, pageSize))
        return getResponseFromTodosFlux(todos, todosRepository.countByIsCompletedIsFalse(), request, page, pageSize)
    }

    @GetMapping("/completed")
    fun getCompleted(request: ServerHttpRequest,
                     @RequestParam(value = "page", defaultValue = "1") page: Int,
                     @RequestParam(value = "page_size", defaultValue = "5") pageSize: Int): Mono<out AppResponse> {
        val todos = todosRepository.findByIsCompletedIsTrueHql(PageRequest.of(page - 1, pageSize))
        return getResponseFromTodosFlux(todos, todosRepository.countByHqlCompletedTrue(), request, page, pageSize)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: String): Mono<ResponseEntity<out AppResponse>> {
        return this.todosRepository.findById(id)
                .map { ResponseEntity.ok(TodoDetailsResponse(it)) as ResponseEntity<out AppResponse> }
                .defaultIfEmpty(ResponseEntity(ErrorResponse("Todo not found"), HttpStatus.NOT_FOUND))
    }

    @PostMapping
    fun create(@Valid @RequestBody todo: Todo): Mono<ResponseEntity<out AppResponse>> {
        return todosRepository.save(todo).map { ResponseEntity(TodoDetailsResponse(it, "Todo created successfully"), HttpStatus.CREATED) }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody todoInput: Todo): Mono<ResponseEntity<out AppResponse>> {
        // We return either Mono<ResponseEntity<TodoDetailsResponse>>
        // or Mono<ResponseEntity<ErrorResponse>>
        return todosRepository.findById(id)
                .flatMap { todoFromDb ->

                    val title = todoInput.title
                    todoFromDb.title = title

                    val description = todoInput.description
                    if (description != null)
                        todoFromDb.description = description

                    todoFromDb.isCompleted = todoInput.isCompleted
                    todosRepository.save(todoFromDb).map { ResponseEntity.ok(TodoDetailsResponse(it, "Todo Updated successfully")) as ResponseEntity<out AppResponse> }
                }.defaultIfEmpty(ResponseEntity(ErrorResponse("Todo not found"), HttpStatus.NOT_FOUND))
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): Mono<ResponseEntity<out AppResponse>> {
        return todosRepository.findById(id)
                .flatMap { ot ->
                    todosRepository.delete(ot)
                            .then(Mono.just(ResponseEntity(SuccessResponse("You have successfully deleted the todo"),
                                    HttpStatus.NO_CONTENT) as ResponseEntity<out AppResponse>))
                }
                .defaultIfEmpty(ResponseEntity(ErrorResponse("Todo not found"), HttpStatus.NOT_FOUND))
    }

    @DeleteMapping
    fun deleteAll(): Mono<ResponseEntity<out AppResponse>> {
        return todosRepository.deleteAll()
                .then(Mono.just(ResponseEntity(SuccessResponse("All todos deleted successfully"), HttpStatus.OK) as ResponseEntity<out AppResponse>))
    }

    private fun getResponseFromTodosFlux(todoFlux: Flux<Todo>, countMono: Mono<Long>, request: ServerHttpRequest, page: Int, pageSize: Int): Mono<AppResponse> {
        return todoFlux.collectList().flatMap<AppResponse> { todoList ->
            countMono
                    .map<PageMeta> { totalItemsCount -> PageMeta.build(todoList, request.uri.path, page, pageSize, totalItemsCount) }
                    .map<AppResponse> { pageMeta -> TodoListResponse.build(pageMeta, todoList) }
        }
    }
}
