/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import io.undertow.Handlers
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.resource.ClassPathResourceManager
import org.libraryweasel.http.JsonEndPoint
import org.libraryweasel.http.addJsonEndPoint
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.web.api.WebPlugin
import org.slf4j.LoggerFactory

val getAllDocumentsEndPoint = JsonEndPoint("/documents", "get") {
    TODO()
//    if (exchange.requestMethod.equalToString("get")) {
//        //TODO move to blocking thread
//        logger.debug("in GET for /documents/")
//        val response = exchange.responseSender
//        val allDocuments = abcManager.allABCDocumentDetails(exchange.securityContext.authenticatedAccount)
//        exchange.responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
//        response.send(gson.toJson(allDocuments))
}

val postDocumentEndPoint = JsonEndPoint("/documents", "post") {
    TODO()
//} else if (exchange.requestMethod.equalToString("post")) {
    //TODO move to blocking thread
//    exchange.responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
//    exchange.requestReceiver.receiveFullString { exchange: HttpServerExchange?, message: String? ->
//        logger.debug("in POST for /documents/ with content: {}", message)
//        val response = exchange!!.responseSender
//        val request = gson.fromJson(message, JsonObject::class.java)
//        if (request.has("document")) {
//            val document = request.getAsJsonPrimitive("document").asString
//            val result = abcManager.createABCDocument(exchange.securityContext.authenticatedAccount, document).block()
//            if (result != null) {
//                response.send(gson.toJson(mapOf(Pair("result", "success")))) //TODO maybe return ID?
//            } else {
//                response.send(gson.toJson(mapOf(Pair("result", "error"))))
//            }
//        }
//    }
//}
//})
}

val getSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "get") {
    TODO()
//    val id: Long? = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).parameters["id"]?.toLong()
//    if (exchange.requestMethod.equalToString("get") && id != null) {
//        //TODO move to blocking thread
//        exchange.responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
//        logger.debug("in GET for /documents/$id/")
//        val response = exchange.responseSender
//        val document = abcManager.fetchABCDocument(exchange.securityContext.authenticatedAccount, id)
//        response.send(gson.toJson(document))
}

val patchSingleDocumentEndPoint = JsonEndPoint("/documents/{id}", "patch") {
    TODO()
//    val id: Long? = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).parameters["id"]?.toLong()
//} else if (exchange.requestMethod.equalToString("patch") && id != null) {
//    //TODO move to blocking thread
//    exchange.responseHeaders.put(Headers.CONTENT_TYPE, "application/json")
//
//    exchange.requestReceiver.receiveFullString { exchange: HttpServerExchange, message: String ->
//        logger.debug("in PATCH for /documents/ with content: {}", message)
//        val response = exchange.responseSender
//        val request = gson.fromJson(message, JsonObject::class.java)
//        if (request.has("document")) {
//            val document = request.getAsJsonPrimitive("document").asString
//            val result = abcManager.updateABCDocument(exchange.securityContext.authenticatedAccount, id, document).block()
//            if (result != null) {
//                response.send(gson.toJson(mapOf(Pair("result", "success"))))
//            } else {
//                response.send(gson.toJson(mapOf(Pair("result", "error"))))
//            }
//        }
//    }
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

        pathHandler.add("/*", Handlers.resource(ClassPathResourceManager(this.javaClass.classLoader, "/public/")))
        return pathHandler
    }

    private fun start() {
    }
}
