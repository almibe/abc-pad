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
        //TODO move to blocking thread
        logger.debug("in GET for /documents/")
        val allDocuments = abcManager.allABCDocumentDetails(it.account).collectList().block()
        JsonEndPointResult(gson.toJsonTree(allDocuments).asJsonObject)
    }

    val postDocumentEndPoint = JsonEndPoint("/documents", "post") {
        //TODO move to blocking thread
        logger.debug("in POST for /documents/ with content: {}", it.requestBody)
        if (it.requestBody != null && it.requestBody!!.has("document")) {
            val document = it.requestBody!!.getAsJsonPrimitive("document").asString
            val result = abcManager.createABCDocument(it.account, document).block()
            if (result != null) {
                val result = JsonObject()
                result.addProperty("result", "success") //TODO maybe return ID or full document?
                JsonEndPointResult(result)
            } else {
                val result = JsonObject()
                result.addProperty("result", "error")
                JsonEndPointResult(result)
            }
        } else {
            val result = JsonObject()
            result.addProperty("result", "error")
            JsonEndPointResult(result)
        }
    }

    val getSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "get") {
        val id: Long? = it.pathParameters["id"]?.toLong()
        logger.debug("in GET for /documents/$id/")
        if (id != null) {
            //TODO move to blocking thread
            val document = abcManager.fetchABCDocument(it.account, id).block()
            JsonEndPointResult(gson.toJsonTree(document).asJsonObject)
        } else {
            val result = JsonObject()
            result.addProperty("result", "error")
            JsonEndPointResult(result)
        }
    }

    val patchSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "patch") {
        val id: Long? = it.pathParameters["id"]?.toLong()
        val request = it.requestBody
        if (id != null && request != null && request.has("document")) {
            //TODO move to blocking thread
            logger.debug("in PATCH for /documents/ with content: {}", it.requestBody)
            val document = request.getAsJsonPrimitive("document").asString
            val result = abcManager.updateABCDocument(it.account, id, document).block()
            if (result != null) {
                val result = JsonObject()
                result.addProperty("result", "success") //TODO maybe return ID or full document?
                JsonEndPointResult(result)
            } else {
                val result = JsonObject()
                result.addProperty("result", "error")
                JsonEndPointResult(result)
            }
        } else {
            val result = JsonObject()
            result.addProperty("result", "error")
            JsonEndPointResult(result)
        }
    }

    val deleteSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "delete") {
        TODO()
//    val id: Long? = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).parameters["id"]?.toLong()
//} else if (exchange.requestMethod.equalToString("delete") && id != null) {
//    //TODO move to blocking thread
//    exchange.responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
//    logger.debug("in DELETE for /documents/ with content: {}", id)
//    val response = exchange.responseSender
//    val result = abcManager.removeABCDocument(exchange.securityContext.authenticatedAccount, id).block()
//    if (result != null && result) {
//        response.send(gson.toJson(mapOf(Pair("result", "success"))))
//    } else {
//        response.send(gson.toJson(mapOf(Pair("result", "error"))))
//    }
//}
//})
    }
}
