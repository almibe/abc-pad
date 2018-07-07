/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.kotlintest.specs.StringSpec
import jetbrains.exodus.entitystore.PersistentEntityStores
import org.libraryweasel.xodus.api.EntityStoreInstanceManager

class ABCWebPluginTest : StringSpec({
    val webPlugin = ABCWebPlugin()
    val persistentEntityStore = PersistentEntityStores.newInstance(createTempDir())
    val entityStoreInstanceManager = EntityStoreInstanceManager { persistentEntityStore }
    val abcDocumentStore = ABCXodusStore(entityStoreInstanceManager)
    webPlugin.abcManager = abcDocumentStore

    "test getAllDocumentsEndPoint with no documents" {
//        val input = JsonEndPointInput()
//        val result = webPlugin.getAllDocumentsEndPoint.handler(input)
//        result.statusCode shouldBe 200
    }

    "test postDocumentEndPoint" {

    }

    "test getAllDocumentsEndPoint with documents" {

    }

    "test getSingleDocumentEndPoint" {

    }

    "test patchSingleDocumentEndPoint" {

    }

    "test deleteSingleDocumentEndPoint" {

    }

    "test getAllDocumentsEndPoint after deleting documents" {

    }
})
