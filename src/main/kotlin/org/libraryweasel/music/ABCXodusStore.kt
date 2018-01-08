/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music

import io.vertx.ext.auth.User
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.StoreTransaction
import org.libraryweasel.music.api.ABCDocument
import org.libraryweasel.music.api.ABCManager
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

    override fun allABCDocuments(user: User): Set<ABCDocument> {
        return entityStore.instance.computeInReadonlyTransaction { txn ->
            val results = txn.find(documentClass, "username", username(user))
            val queries = mutableSetOf<ABCDocument>()
            results.forEach { it ->
                queries.add(ABCDocument(it.id.localId, it.getProperty("name") as String, it.getProperty("document") as String))
            }
            queries
        }
    }

    override fun persistABCDocument(user: User, id: Long?, name: String, document: String): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val entity = if (id == null) null else fetchQueryEntity(txn, id)
            if (entity != null) {
                entity.setProperty("name", name)
                entity.setProperty("document", document)
                txn.saveEntity(entity)
                true
            } else {
                val abcDocument = txn.newEntity(documentClass)
                abcDocument.setProperty("name", name)
                abcDocument.setProperty("document", document)
                txn.saveEntity(abcDocument)
                true
            }
        }
    }

    override fun removeABCDocument(user: User, id: Long): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val result = fetchQueryEntity(txn, id)
            if (result != null) {
                result.delete()
                true
            } else {
                true
            }
        }
    }

    private fun fetchQueryEntity(txn: StoreTransaction, id: Long): Entity? {
        val results = txn.findIds(documentClass, id, id)
        return results.first
    }
}
