/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import org.apache.felix.dm.DependencyActivatorBase
import org.apache.felix.dm.DependencyManager
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.LibraryWeaselComponentRegistrar
import org.libraryweasel.vertx.api.HttpApp
import org.libraryweasel.vertx.api.WebEntryPoint
import org.osgi.framework.BundleContext

class Activator : DependencyActivatorBase() {
    override fun init(context: BundleContext, manager: DependencyManager) {
        val registrar = LibraryWeaselComponentRegistrar(manager)
        registrar.register(StaticFiles::class.java)
        registrar.register(ABCEditorEntryPoint::class.java)
        registrar.register(ABCHttpApp::class.java)
        registrar.register(ABCXodusStore::class.java)
    }

    override fun destroy(context: BundleContext, manager: DependencyManager) {
    }
}

@Component(HttpApp::class)
class StaticFiles : HttpApp {
    override val path: String = "/music/abc/"

    override fun initRouter(router: Router) {
        router.get().handler(StaticHandler.create("public", this.javaClass.classLoader))
    }
}

@Component(WebEntryPoint::class)
class ABCEditorEntryPoint : WebEntryPoint {
    override val kit: String = "Music"
    override val name: String = "ABC Document Editor"
    override val path: String = "/music/abc/"
    override val position: Float = 0F
}
