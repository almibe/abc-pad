/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.chamber.abc

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import jetbrains.exodus.entitystore.PersistentEntityStores
import org.libraryweasel.music.abc.api.ABCDocumentDetail
import org.libraryweasel.xodus.api.EntityStoreInstanceManager

class ABCXodusStoreTest : StringSpec({
    val account = object : User {
        override fun clearCache(): User { TODO("not implemented") }
        override fun setAuthProvider(authProvider: AuthProvider?) { TODO("not implemented") }
        override fun isAuthorized(authority: String?, resultHandler: Handler<AsyncResult<Boolean>>?): User { TODO("not implemented") }
        override fun principal(): JsonObject {
            val jsonObject = JsonObject()
            jsonObject.put("username", "account")
            return jsonObject
        }
    }

    val account2 = object : User {
        override fun clearCache(): User { TODO("not implemented") }
        override fun setAuthProvider(authProvider: AuthProvider?) { TODO("not implemented") }
        override fun isAuthorized(authority: String?, resultHandler: Handler<AsyncResult<Boolean>>?): User { TODO("not implemented") }
        override fun principal(): JsonObject {
            val jsonObject = JsonObject()
            jsonObject.put("username", "account2")
            return jsonObject
        }
    }

    val persistentEntityStore = PersistentEntityStores.newInstance(createTempDir())
    val entityStoreInstanceManager = EntityStoreInstanceManager { persistentEntityStore }
    val abcDocumentStore = ABCXodusStore()
    abcDocumentStore.entityStore = entityStoreInstanceManager

    "start with zero documents" {
        val result = abcDocumentStore.allABCDocumentDetails(account)
        result.documents.size shouldBe 0
    }

    "test adding one documents" {
        val doc = abcDocumentStore.createABCDocument(account, "T: Grand Overture - Op.61\nC: Mauro Giuliani\nK: C")
        doc.document shouldBe "T: Grand Overture - Op.61\nC: Mauro Giuliani\nK: C"
        abcDocumentStore.allABCDocumentDetails(account).documents.size shouldBe 1
    }

    "test adding two more documents" {
        abcDocumentStore.createABCDocument(account, "T: In C\nC: Terry Riley\nK: C")
        abcDocumentStore.createABCDocument(account, "T: A Rainbow in Curved Air\nC: Terry Riley")
        //abcDocumentStore.allABCDocumentDetails(account).count().block() shouldBe 3L
    }

    "test if documents were added to store" {
        val result: Long = entityStoreInstanceManager.instance.computeInReadonlyTransaction {
            it.getAll("music.abc.Document").size()
        }
        result shouldBe 3L
    }

    "test details for two documents and fetch" {
        val result: List<ABCDocumentDetail> = abcDocumentStore.allABCDocumentDetails(account).documents
        result.size shouldBe 3
        result.filter { it.composer == "Terry Riley" }.size shouldBe 2
        result.filter { it.title == "In C" }.size shouldBe 1
        result.filter { it.title == "A Rainbow in Curved Air" }.size shouldBe 1
    }

    "edit documents" {
        val result = abcDocumentStore.allABCDocumentDetails(account).documents
        val id = result.first { it.title.contains("Grand") }.id
        val content = abcDocumentStore.fetchABCDocument(account, id).document
        abcDocumentStore.updateABCDocument(account, id, "$content\nabc")

        val testResult: List<ABCDocumentDetail> = abcDocumentStore.allABCDocumentDetails(account).documents
        testResult.size shouldBe 3
    }

    "test new details and fetch" {
        val results = abcDocumentStore.allABCDocumentDetails(account).documents.filter { it.title.contains("Grand") }
        results.size shouldBe 1
        val doc = abcDocumentStore.fetchABCDocument(account, results.first().id)
        doc.document.contains("abc") shouldBe true
        results.first().composer shouldBe "Mauro Giuliani"
    }

    "remove one document" {
        val result = abcDocumentStore.allABCDocumentDetails(account).documents
        val id = result.first { it.title.contains("Grand") }.id
        abcDocumentStore.removeABCDocument(account, id)
        abcDocumentStore.allABCDocumentDetails(account).documents.size shouldBe 2
    }

    "add document for another user and make sure accounts are being stored correctly" {
        abcDocumentStore.createABCDocument(account2, "This is a test")

        abcDocumentStore.allABCDocumentDetails(account).documents.size shouldBe 2
        abcDocumentStore.allABCDocumentDetails(account2).documents.size shouldBe 1
    }

    "make sure that documents without C and T fields are stored correctly" {
        val docDetails = abcDocumentStore.allABCDocumentDetails(account2).documents.first()
        docDetails.composer shouldBe "unknown"
        docDetails.title shouldBe "untitled"
    }

    "test extracting title and composer" {
        val title = abcDocumentStore.documentToTitle("T: A Rainbow in Curved Air\nC: Terry Riley")
        val composer = abcDocumentStore.documentToComposer("T: A Rainbow in Curved Air\nC: Terry Riley")

        title shouldBe "A Rainbow in Curved Air"
        composer shouldBe "Terry Riley"
    }
})
