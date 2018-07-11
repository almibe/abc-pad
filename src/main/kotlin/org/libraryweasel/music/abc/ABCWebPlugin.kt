/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.web.api.EndPointRoute
import org.libraryweasel.web.api.StaticFiles
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(ABCStaticFiles::class.java)
val gson = Gson()

@Component(StaticFiles::class)
class ABCStaticFiles: StaticFiles {
    override val classLoader: ClassLoader = this.javaClass.classLoader
    override val rootPath: String = abcBasePath
}

@Component(EndPointRoute::class)
class GetAllDocumentsRoute : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = "/documents"
    override fun handler(routingContext: RoutingContext) {
        logger.debug("in GET for /documents/")
        val allDocuments = abcManager.allABCDocumentDetails(routingContext.user())
        routingContext.response().end(gson.toJson(allDocuments))
    }
}

@Component(EndPointRoute::class)
class PostDocumentRoute : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.POST
    override val rootPath: String = "/documents"
    override fun handler(routingContext: RoutingContext) {
        logger.debug("in POST for /documents/ with content: {}", routingContext.bodyAsString)
        if (routingContext.request() != null && routingContext.bodyAsJson.getString("document").isNotEmpty()) {
            val document = routingContext.bodyAsString
            val result = abcManager.createABCDocument(routingContext.user(), document)
            routingContext.response().statusCode = 201
            routingContext.response().end(gson.toJson(result))
        } else {
            val result = JsonObject()
            result.addProperty("error", "Missing document property in body.")
            routingContext.response().statusCode = 422
            routingContext.response().end(gson.toJson(result))
        }
    }
}

@Component(EndPointRoute::class)
class GetSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = "/documents/:id"
    override fun handler(routingContext: RoutingContext) {
        val id: Long? = routingContext.get("id")
        logger.debug("in GET for /documents/$id/")
        if (id != null) {
            val document = abcManager.fetchABCDocument(routingContext.user(), id)
            routingContext.response().end(gson.toJson(document))
        } else {
            val result = JsonObject()
            result.addProperty("error", "Invalid id parameter.")
            routingContext.response().statusCode = 422
            routingContext.response().end(result.toString())
        }
    }
}

@Component(EndPointRoute::class)
class PatchSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.PATCH
    override val rootPath: String = "/documents/:id"
    override fun handler(routingContext: RoutingContext) {
        val id: Long? = routingContext.get("id")
        val request = routingContext.bodyAsJson
        if (id != null && request != null && request.containsKey("document")) {
            logger.debug("in PATCH for /documents/ with content: {}", routingContext.bodyAsString)
            val document = request.getString("document")
            val result = abcManager.updateABCDocument(routingContext.user(), id, document)
            routingContext.response().end(gson.toJson(result))
        } else {
            val result = JsonObject()
            result.addProperty("error", "Invalid id or document.")
            routingContext.response().statusCode = 422
            routingContext.response().end(result.toString())
        }
    }
}

@Component(EndPointRoute::class)
class DeleteSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.DELETE
    override val rootPath: String = "/documents/:id"
    override fun handler(routingContext: RoutingContext) {
        val id: Long? = routingContext.get("id")
        if (id != null) {
            logger.debug("in DELETE for /documents/ with content: {}", id)
            val removed = abcManager.removeABCDocument(routingContext.user(), id)
            val result = JsonObject()
            result.addProperty("removed", removed)
            routingContext.response().end(gson.toJson(result))
        } else {
            val result = JsonObject()
            result.addProperty("error", "Invalid id parameter.")
            routingContext.response().statusCode = 422
            routingContext.response().end(result.toString())
        }
    }
}
