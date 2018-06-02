/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.undertow.security.idm.Account
import jetbrains.exodus.entitystore.PersistentEntityStores
import org.libraryweasel.music.abc.api.ABCDocumentDetails
import org.libraryweasel.xodus.api.EntityStoreInstanceManager
import java.security.Principal

class ABCXodusStoreTest : StringSpec({
    val account = object : Account {
        override fun getRoles(): MutableSet<String> = mutableSetOf()
        override fun getPrincipal(): Principal = Principal { "test" }
    }

    val persistentEntityStore = PersistentEntityStores.newInstance(createTempDir())
    val entityStoreInstanceManager = EntityStoreInstanceManager { persistentEntityStore }
    val abcDocumentStore = ABCXodusStore(entityStoreInstanceManager)

    "start with zero documents" {
        val result = abcDocumentStore.allABCDocumentDetails(account)
        result.count().block() shouldBe 0L
    }

    "test adding one documents" {
        val doc = abcDocumentStore.createABCDocument(account, "T: Grand Overture - Op.61\nC: Mauro Giuliani\nK: C").block()!!
        doc.document shouldBe "T: Grand Overture - Op.61\nC: Mauro Giuliani\nK: C"
        abcDocumentStore.allABCDocumentDetails(account).count().block() shouldBe 1L
    }

    "test adding two more documents" {
        abcDocumentStore.createABCDocument(account, "T: In C\nC: Terry Riley\nK: C")
        abcDocumentStore.createABCDocument(account, "T: A Rainbow in Curved Air\nC: Terry Riley")
        abcDocumentStore.allABCDocumentDetails(account).count().block() shouldBe 3L
    }

    "test if documents were added to store" {
        val result: Long = entityStoreInstanceManager.instance.computeInReadonlyTransaction {
            it.getAll("music.abc.Document").size()
        }
        result shouldBe 3L
    }

    "test details for two documents and fetch" {
        val result: List<ABCDocumentDetails> = abcDocumentStore.allABCDocumentDetails(account).collectList().block()!!
        result.size shouldBe 3
        result.filter { it.composer == "Terry Riley" }.size shouldBe 2
        result.filter { it.title == "In C" }.size shouldBe 1
        result.filter { it.title == "A Rainbow in Curved Air" }.size shouldBe 1
    }

//    "edit documents" {
//        "test".length shouldBe 4
//    }
//
//    "test new details and fetch" {
//        "test".length shouldBe 4
//    }
//
//    "remove one document" {
//        "test".length shouldBe 4
//    }
//
//    "test details of remaining document and fetch" {
//        "test".length shouldBe 4
//    }
//
//    "add document for another user" {
//        "test".length shouldBe 4
//    }
//
//    "make sure that accounts are being used with details and fetching" {
//        "test".length shouldBe 4
//    }
})
