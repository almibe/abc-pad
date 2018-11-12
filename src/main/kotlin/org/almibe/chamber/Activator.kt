/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.chamber

import org.apache.felix.dm.DependencyActivatorBase
import org.apache.felix.dm.DependencyManager
import org.libraryweasel.music.abc.*
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.LibraryWeaselComponentRegistrar
import org.libraryweasel.web.api.WebEntryPoint
import org.osgi.framework.BundleContext

class Activator : DependencyActivatorBase() {
    override fun init(context: BundleContext, manager: DependencyManager) {
        val registrar = LibraryWeaselComponentRegistrar(manager)
        registrar.register(LoadABCEditorEntryPoint::class.java)
        registrar.register(NewABCEditorEntryPoint::class.java)
        registrar.register(GetAllDocumentsRoute::class.java)
        registrar.register(PostDocumentRoute::class.java)
        registrar.register(DeleteSingleDocumentEndPoint::class.java)
        registrar.register(GetSingleDocumentEndPoint::class.java)
        registrar.register(PatchSingleDocumentEndPoint::class.java)
        registrar.register(DeleteSingleDocumentEndPoint::class.java)
        registrar.register(ABCXodusStore::class.java)
        registrar.register(ABCStaticResources::class.java)
    }

    override fun destroy(context: BundleContext, manager: DependencyManager) {
    }
}

const val abcBasePath = "/music/abc/"
const val kitName = "Music"

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
