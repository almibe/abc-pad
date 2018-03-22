/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import com.google.gson.Gson
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.music.abcBasePath
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.vertx.api.PathRouter
import org.slf4j.LoggerFactory

@Component(PathRouter::class)
class ABCPathRouter : PathRouter {
    @Service @Volatile
    lateinit var abcManager: ABCManager

    override val path: String = abcBasePath
    val logger = LoggerFactory.getLogger(ABCPathRouter::class.java)
    val gson = Gson()

    override fun initRouter(router: Router) {
        router.route(HttpMethod.GET, "/documents")
                .produces("application/json")
                .blockingHandler {context ->
                    logger.debug("in GET for /documents")
                    val response = context.response()
                    val allDocuments = abcManager.allABCDocuments(context.user())
                    response.end(gson.toJson(allDocuments))
                }
        router.route(HttpMethod.GET, "/documents/:id/")
                .produces("application/json")
                .blockingHandler { context ->
                    val id = context.request().getParam("id").toLong()
                    logger.debug("in GET for /documents/$id/")
                    val response = context.response()
                    val document = abcManager.fetchABCDocument(context.user(), id)
                    response.end(gson.toJson(document))
                }
        router.route(HttpMethod.POST, "/documents/:id/")
                .produces("application/json")
                .blockingHandler { context: RoutingContext ->
                    logger.debug("in POST for /documents with content: {}", context.bodyAsJson.toString())
                    val response = context.response()
                    val request = context.bodyAsJson
                    if (request.containsKey("document")) {
                        val document = request.getString("document")
                        val id: Long = request.getLong("id")
                        val result = abcManager.updateABCDocument(context.user(), id, document)
                        if (result) {
                            response.end(gson.toJson(mapOf(Pair("result", "success"))))
                        } else {
                            response.end(gson.toJson(mapOf(Pair("result", "error"))))
                        }
                    } else {
                        context.next()
                    }
                }
        router.route(HttpMethod.PATCH, "/documents/:id/")
                .produces("application/json")
                .blockingHandler { context ->
                    //TODO handle PATCH update here
                }
        router.route(HttpMethod.DELETE, "/documents/:id/")
                .produces("application/json")
                .blockingHandler { context ->
                    val request = context.request()
                    val id = request.getParam("id").toLong()
                    logger.debug("in DELETE for /documents with content: {}", id)
                    val response = context.response()
                    val result = abcManager.removeABCDocument(context.user(), id)
                    if (result) {
                        response.end(gson.toJson(mapOf(Pair("result", "success"))))
                    } else {
                        response.end(gson.toJson(mapOf(Pair("result", "error"))))
                    }
                }
    }
}
