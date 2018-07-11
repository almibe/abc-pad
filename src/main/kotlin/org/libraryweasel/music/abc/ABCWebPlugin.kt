/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.web.api.BlankRoute
import org.libraryweasel.web.api.EndPointRoute
import org.libraryweasel.web.api.StaticFiles
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(AllDocumentsRoute::class.java)
val gson = Gson()

@Component(StaticFiles::class)
class StaticResources: StaticFiles {
    override val classLoader: ClassLoader = this.javaClass.classLoader
    override val rootPath: String = "/documents"
}

@Component(BlankRoute::class)
class AllDocumentsRoute : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.GET
    override val rootPath: String = "/documents"
    override fun handler(routingContext: RoutingContext) {
        logger.debug("in GET for /documents/")
        val allDocuments = abcManager.allABCDocumentDetails(routingContext.user())
        routingContext.response().end(gson.toJson(allDocuments))
    }
}

@Component(BlankRoute::class)
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

@Component(BlankRoute::class)
class getSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    val id: Long? = it.pathParameters["id"]?.toLong()
    logger.debug("in GET for /documents/$id/")
    if (id != null) {
        val document = abcManager.fetchABCDocument(it.account, id)
        JsonEndPointResult(gson.toJsonTree(document).asJsonObject)
    } else {
        val result = JsonObject()
        result.addProperty("error", "Invalid id parameter.")
        JsonEndPointResult(statusCode = 422, responseBody = result)
    }
}



@Component(BlankRoute::class)
class patchSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.
            override val rootPath: String = "/documents/{id}"
    override fun handler(routingContext: RoutingContext) {
    }
    val id: Long? = it.pathParameters["id"]?.toLong()
    val request = it.requestBody
    if (id != null && request != null && request.has("document")) {
        logger.debug("in PATCH for /documents/ with content: {}", it.requestBody)
        val document = request.getAsJsonPrimitive("document").asString
        val result = abcManager.updateABCDocument(it.account, id, document)
        JsonEndPointResult(result)
    } else {
        val result = JsonObject()
        result.addProperty("error", "Invalid id or document.")
        JsonEndPointResult(statusCode = 422, responseBody = result)
    }

}

@Component(BlankRoute::class)
class deleteSingleDocumentEndPoint : EndPointRoute() {
    @Service @Volatile lateinit var abcManager: ABCManager
    override val httpMethod: HttpMethod = HttpMethod.
            override val rootPath: String = "/documents/{id}"
    override fun handler(routingContext: RoutingContext) {

        val id: Long? = it.pathParameters["id"]?.toLong()
        if (id != null) {
            logger.debug("in DELETE for /documents/ with content: {}", id)
            val removed = abcManager.removeABCDocument(it.account, id)
            val result = JsonObject()
            result.addProperty("removed", removed)
            JsonEndPointResult(result)
        } else {
            val result = JsonObject()
            result.addProperty("error", "Invalid id parameter.")
            JsonEndPointResult(statusCode = 422, responseBody = result)
        }
    }
}
