/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import jetbrains.exodus.entitystore.PersistentEntityStores
import org.libraryweasel.music.abc.api.ABCDocumentDetails
import org.libraryweasel.xodus.api.EntityStoreInstanceManager
import java.security.Principal

class ABCXodusStoreTest : StringSpec({
    val account = object : Account {
        override fun getRoles(): MutableSet<String> = mutableSetOf()
        override fun getPrincipal(): Principal = Principal { "test" }
    }

    val account2 = object : Account {
        override fun getRoles(): MutableSet<String> = mutableSetOf()
        override fun getPrincipal(): Principal = Principal { "test2" }
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

    "edit documents" {
        val result = abcDocumentStore.allABCDocumentDetails(account).collectList().block()!!
        val id = result.first { it.title.contains("Grand") }.id
        val content = abcDocumentStore.fetchABCDocument(account, id).block()!!.document
        abcDocumentStore.updateABCDocument(account, id, "$content\nabc")

        val testResult: List<ABCDocumentDetails> = abcDocumentStore.allABCDocumentDetails(account).collectList().block()!!
        testResult.size shouldBe 3
    }

    "test new details and fetch" {
        val results = abcDocumentStore.allABCDocumentDetails(account).collectList().block()!!.filter { it.title.contains("Grand") }
        results.size shouldBe 1
        val doc = abcDocumentStore.fetchABCDocument(account, results.first().id).block()!!
        doc.document.contains("abc") shouldBe true
        results.first().composer shouldBe "Mauro Giuliani"
    }

    "remove one document" {
        val result = abcDocumentStore.allABCDocumentDetails(account).collectList().block()!!
        val id = result.first { it.title.contains("Grand") }.id
        abcDocumentStore.removeABCDocument(account, id)
        abcDocumentStore.allABCDocumentDetails(account).count().block()!! shouldBe 2L
    }

    "add document for another user and make sure accounts are being stored correctly" {
        abcDocumentStore.createABCDocument(account2, "This is a test")

        abcDocumentStore.allABCDocumentDetails(account).count().block() shouldBe 2L
        abcDocumentStore.allABCDocumentDetails(account2).count().block() shouldBe 1L
    }

    "make sure that documents without C and T fields are stored correctly" {
        val docDetails = abcDocumentStore.allABCDocumentDetails(account2).collectList().block()!!.first()
        docDetails.composer shouldBe "unknown"
        docDetails.title shouldBe "untitled"
    }
})
