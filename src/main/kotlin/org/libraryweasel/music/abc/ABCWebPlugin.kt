/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Route
import io.vertx.ext.web.handler.StaticHandler
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.web.api.WebRoute
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger(ABCLogger::class.java)
val gson = Gson()

object ABCLogger

@Component(WebRoute::class)
class ABCStaticResources: WebRoute {
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = "$abcBasePath*"

    override fun initRoute(route: Route) {
        route.handler(StaticHandler.create("webroot", this.javaClass.classLoader))
    }
}

@Component(WebRoute::class)
class GetAllDocumentsRoute : WebRoute {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = abcBasePath + "documents"

    override fun initRoute(route: Route) {
        route.produces("application/json")
        route.handler {routingContext ->
            logger.debug("in GET for /documents/")
            val allDocuments = abcManager.allABCDocumentDetails(routingContext.user())
            routingContext.response()
                .putHeader("content-type", "application/json")
                .end(gson.toJson(allDocuments))
        }
    }
}

@Component(WebRoute::class)
class PostDocumentRoute : WebRoute {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.POST
    override val rootPath: String = abcBasePath + "documents"

    override fun initRoute(route: Route) {
        route.produces("application/json")
        route.handler { routingContext ->
            logger.debug("in POST for /documents/ with content: {}", routingContext.bodyAsString)
            if (routingContext.request() != null && routingContext.bodyAsJson.getString("document").isNotEmpty()) {
                val document = routingContext.bodyAsJson.getString("document").replace("\\n", "\n")
                val result = abcManager.createABCDocument(routingContext.user(), document)
                routingContext.response().statusCode = 201
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(gson.toJson(result))
            } else {
                val result = JsonObject()
                result.addProperty("error", "Missing document property in body.")
                routingContext.response().statusCode = 422
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(gson.toJson(result))
            }
        }
    }
}

@Component(WebRoute::class)
class GetSingleDocumentEndPoint : WebRoute {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = abcBasePath + "documents/:id"

    override fun initRoute(route: Route) {
        route.produces("application/json")
        route.handler { routingContext ->
            val id: Long? = routingContext.get("id")
            logger.debug("in GET for /documents/$id/")
            if (id != null) {
                val document = abcManager.fetchABCDocument(routingContext.user(), id)
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(gson.toJson(document))
            } else {
                val result = JsonObject()
                result.addProperty("error", "Invalid id parameter.")
                routingContext.response().statusCode = 422
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(result.toString())
            }
        }
    }

}

@Component(WebRoute::class)
class PatchSingleDocumentEndPoint : WebRoute {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.PATCH
    override val rootPath: String = abcBasePath + "documents/:id"

    override fun initRoute(route: Route) {
        route.produces("application/json")
        route.handler { routingContext ->
            val id: Long? = routingContext.get("id")
            val request = routingContext.bodyAsJson
            if (id != null && request != null && request.containsKey("document")) {
                logger.debug("in PATCH for /documents/ with content: {}", routingContext.bodyAsString)
                val document = request.getString("document").replace("\\n", "\n")
                val result = abcManager.updateABCDocument(routingContext.user(), id, document)
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(gson.toJson(result))
            } else {
                val result = JsonObject()
                result.addProperty("error", "Invalid id or document.")
                routingContext.response().statusCode = 422
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(result.toString())
            }
        }
    }
}

@Component(WebRoute::class)
class DeleteSingleDocumentEndPoint : WebRoute {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.DELETE
    override val rootPath: String = abcBasePath + "documents/:id"

    override fun initRoute(route: Route) {
        route.handler { routingContext ->
            val id: Long? = routingContext.get("id")
            if (id != null) {
                logger.debug("in DELETE for /documents/ with content: {}", id)
                val removed = abcManager.removeABCDocument(routingContext.user(), id)
                val result = JsonObject()
                result.addProperty("removed", removed)
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(gson.toJson(result))
            } else {
                val result = JsonObject()
                result.addProperty("error", "Invalid id parameter.")
                routingContext.response().statusCode = 422
                routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(result.toString())
            }
        }
    }
}
