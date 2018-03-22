/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.vertx.ext.auth.User
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.StoreTransaction
import org.libraryweasel.music.abc.api.ABCDocument
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.xodus.api.EntityStoreInstanceManager

@Component(ABCManager::class)
class ABCXodusStore : ABCManager {
    @Service @Volatile
    private lateinit var entityStore: EntityStoreInstanceManager

    private val documentClass = "music.abc.Document"

    private fun start() {
    }

    private fun username(user: User) : String = user.principal().getString("username")

    override fun allABCDocuments(user: User): Map<Long, String> {
        return entityStore.instance.computeInReadonlyTransaction { txn ->
            val results = txn.find(documentClass, "username", username(user))
            val documents = mutableMapOf<Long, String>()
            results.forEach { it ->
                documents.put(it.id.localId, it.getProperty("name") as String)
            }
            documents
        }
    }

    override fun fetchABCDocument(user: User, id: Long): ABCDocument {
        return  entityStore.instance.computeInReadonlyTransaction { txn ->
            val entity = fetchABCDocumentEntity(txn, user, id)
            if (entity != null) {
                ABCDocument(id, entity.getProperty("document") as String)
            } else {
                throw RuntimeException("document not owned by user")
            }
        }
    }

    override fun createABCDocument(user: User, document: String): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val abcDocument = txn.newEntity(documentClass)
            abcDocument.setProperty("name", documentToName(document))
            abcDocument.setProperty("document", document)
            abcDocument.setProperty("username", username(user))
            txn.saveEntity(abcDocument)
            true
        }
    }

    override fun updateABCDocument(user: User, id: Long, document: String): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val entity = fetchABCDocumentEntity(txn, user, id)
            if (entity != null) {
                entity.setProperty("name", documentToName(document))
                entity.setProperty("document", document)
                txn.saveEntity(entity)
                true
            } else {
                false
            }
        }
    }

    override fun removeABCDocument(user: User, id: Long): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val result = fetchABCDocumentEntity(txn, user, id)
            if (result != null) {
                result.delete()
                true
            } else {
                true
            }
        }
    }

    private fun fetchABCDocumentEntity(txn: StoreTransaction, user: User, id: Long): Entity? {
        val results = txn.findIds(documentClass, id, id)
        return if (!results.isEmpty && results.first?.getProperty("username") as String == username(user)) {
            results.first
        } else {
            null
        }
    }

    private fun documentToName(document: String): String {
        val title: String = document.lines().find { it.trim().startsWith("T:") }?.trim() ?: "untitled"
        val composer: String = document.lines().find { it.trim().startsWith("C:") }?.trim() ?: "unknown"
        return "$title - $composer"
    }
}
