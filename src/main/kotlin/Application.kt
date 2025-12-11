package com.moissdev

import config.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import routes.musicaRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    routing {
        musicaRoutes()

        get("/") {
            call.respondText("¡Servidor de Música Funcionando!")
        }
    }
}