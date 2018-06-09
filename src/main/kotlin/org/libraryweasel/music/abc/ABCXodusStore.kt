/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.undertow.security.idm.Account
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.StoreTransaction
import org.libraryweasel.music.abc.api.ABCDocument
import org.libraryweasel.music.abc.api.ABCDocumentDetail
import org.libraryweasel.music.abc.api.ABCDocumentDetails
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.xodus.api.EntityStoreInstanceManager

@Component(ABCManager::class)
class ABCXodusStore(storeArg: EntityStoreInstanceManager? = null) : ABCManager {
    @Service @Volatile
    private var entityStore: EntityStoreInstanceManager = storeArg!!

    private val documentClass = "music.abc.Document"

    private fun start() {
    }

    private fun username(account: Account) : String = account.principal.name

    override fun allABCDocumentDetails(account: Account): ABCDocumentDetails {
        return entityStore.instance.computeInReadonlyTransaction { txn ->
            val results = txn.find(documentClass, "username", username(account))
            val documentDetails = results.map { it ->
                ABCDocumentDetail(it.id.localId, it.getProperty("title") as String, it.getProperty("composer") as String)
            }
            ABCDocumentDetails(documentDetails)
        }
    }

    override fun fetchABCDocument(account: Account, id: Long): ABCDocument {
        return entityStore.instance.computeInReadonlyTransaction { txn ->
            val entity = fetchABCDocumentEntity(txn, account, id)
            if (entity != null) {
                ABCDocument(id, entity.getProperty("document") as String)
            } else {
                throw RuntimeException("document not owned by user")
            }
        }
    }

    override fun createABCDocument(account: Account, document: String): ABCDocument {
        return entityStore.instance.computeInTransaction { txn ->
            val abcDocument = txn.newEntity(documentClass)
            abcDocument.setProperty("title", documentToTitle(document))
            abcDocument.setProperty("composer", documentToComposer(document))
            abcDocument.setProperty("document", document)
            abcDocument.setProperty("username", username(account))
            txn.saveEntity(abcDocument)
            ABCDocument(abcDocument.id.localId, document)
        }
    }

    override fun updateABCDocument(account: Account, id: Long, document: String): ABCDocument {
        return entityStore.instance.computeInTransaction { txn ->
            val entity = fetchABCDocumentEntity(txn, account, id)
            if (entity != null) {
                entity.setProperty("title", documentToTitle(document))
                entity.setProperty("composer", documentToComposer(document))
                entity.setProperty("document", document)
                txn.saveEntity(entity)
                ABCDocument(id, document)
            } else {
                throw RuntimeException("Document $id not found.")
            }
        }
    }

    override fun removeABCDocument(account: Account, id: Long): Boolean {
        return entityStore.instance.computeInTransaction { txn ->
            val result = fetchABCDocumentEntity(txn, account, id)
            if (result != null) {
                result.delete()
                true
            } else {
                throw RuntimeException("Document $id not found.")
            }
        }
    }

    private fun fetchABCDocumentEntity(txn: StoreTransaction, account: Account, id: Long): Entity? {
        val results = txn.findIds(documentClass, id, id)
        return if (!results.isEmpty && results.first?.getProperty("username") as String == username(account)) {
            results.first
        } else {
            null
        }
    }

    private fun documentToTitle(document: String): String =
            document.lines().find { it.trim().startsWith("T:") }?.substring(2)?.trim() ?: "untitled"

    private fun documentToComposer(document: String): String =
            document.lines().find { it.trim().startsWith("C:") }?.substring(2)?.trim() ?: "unknown"
}
