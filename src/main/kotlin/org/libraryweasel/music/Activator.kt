/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music

import org.apache.felix.dm.DependencyActivatorBase
import org.apache.felix.dm.DependencyManager
import org.libraryweasel.music.abc.ABCWebPlugin
import org.libraryweasel.music.abc.ABCXodusStore
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.LibraryWeaselComponentRegistrar
import org.libraryweasel.web.api.WebEntryPoint
import org.osgi.framework.BundleContext

class Activator : DependencyActivatorBase() {
    override fun init(context: BundleContext, manager: DependencyManager) {
        val registrar = LibraryWeaselComponentRegistrar(manager)
        registrar.register(LoadABCEditorEntryPoint::class.java)
        registrar.register(NewABCEditorEntryPoint::class.java)
        registrar.register(ABCWebPlugin::class.java)
        registrar.register(ABCXodusStore::class.java)
    }

    override fun destroy(context: BundleContext, manager: DependencyManager) {
    }
}

val abcBasePath = "/music/abc/"
val kitName = "Music"

@Component(WebEntryPoint::class)
class LoadABCEditorEntryPoint : WebEntryPoint {
    override val kit: String = kitName
    override val name: String = "Load ABC Document"
    override val path: String = "$abcBasePath#load"
    override val position: Float = 1F
}

@Component(WebEntryPoint::class)
class NewABCEditorEntryPoint : WebEntryPoint {
    override val kit: String = kitName
    override val name: String = "New ABC Document"
    override val path: String = abcBasePath
    override val position: Float = 0F
}
