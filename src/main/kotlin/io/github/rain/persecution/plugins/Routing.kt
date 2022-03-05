package io.github.rain.persecution.plugins

import io.github.rain.persecution.routes.setupClassificationRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        setupClassificationRoutes()
    }
}
