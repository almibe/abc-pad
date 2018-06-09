/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.undertow.Handlers
import io.undertow.server.HttpHandler
import org.libraryweasel.http.*
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.web.api.WebPlugin
import org.slf4j.LoggerFactory

@Component(WebPlugin::class)
class ABCWebPlugin : WebPlugin {
    override val rootPath: String = abcBasePath
    @Service @Volatile
    lateinit var abcManager: ABCManager

    val logger = LoggerFactory.getLogger(ABCWebPlugin::class.java)
    val gson = Gson()

    override val handler: HttpHandler = createHandler()

    private fun createHandler(): HttpHandler {
        val pathHandler =  Handlers.pathTemplate()

        pathHandler.addJsonEndPoint(getAllDocumentsEndPoint)
        pathHandler.addJsonEndPoint(postDocumentEndPoint)
        pathHandler.addJsonEndPoint(getSingleDocumentEndPoint)
        pathHandler.addJsonEndPoint(patchSingleDocumentEndPoint)
        pathHandler.addJsonEndPoint(deleteSingleDocumentEndPoint)

        pathHandler.addStaticResources(StaticResources("/*", this.javaClass.classLoader, "/public/"))
        return pathHandler
    }

    private fun start() {
    }

    val getAllDocumentsEndPoint = JsonEndPoint("/documents", "get") {
        logger.debug("in GET for /documents/")
        val allDocuments = abcManager.allABCDocumentDetails(it.account)
        JsonEndPointResult(allDocuments)
    }

    val postDocumentEndPoint = JsonEndPoint<Any>("/documents", "post") {
        logger.debug("in POST for /documents/ with content: {}", it.requestBody)
        if (it.requestBody != null && it.requestBody!!.has("document")) {
            val document = it.requestBody!!.getAsJsonPrimitive("document").asString
            val result = abcManager.createABCDocument(it.account, document)
            JsonEndPointResult(statusCode = 201, responseBody = result)
        } else {
            val result = JsonObject()
            result.addProperty("error", "Missing document property in body.")
            JsonEndPointResult(statusCode = 422, responseBody = result)
        }
    }

    val getSingleDocumentEndPoint = JsonEndPoint<Any>("/documents/{id}", "get") {
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

    val patchSingleDocumentEndPoint = JsonEndPoint<Any>("/documents/{id}", "patch") {
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

    val deleteSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "delete") {
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
