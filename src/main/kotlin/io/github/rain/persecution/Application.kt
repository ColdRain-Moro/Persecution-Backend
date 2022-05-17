package io.github.rain.persecution

import io.github.rain.persecution.data.db.DBHandler
import io.github.rain.persecution.plugins.configureRouting
import io.github.rain.persecution.plugins.configureSecurity
import io.github.rain.persecution.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()

        // 连接MySQL
        DBHandler.init()
    }.start(wait = true)
}
