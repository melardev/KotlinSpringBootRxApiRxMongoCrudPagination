package com.melardev.spring.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RxCrudApplication

fun main(args: Array<String>) {
    runApplication<RxCrudApplication>(*args)
}
