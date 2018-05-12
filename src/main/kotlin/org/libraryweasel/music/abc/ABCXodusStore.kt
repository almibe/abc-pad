/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc

import io.undertow.security.idm.Account
import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.StoreTransaction
import org.libraryweasel.music.abc.api.ABCDocument
import org.libraryweasel.music.abc.api.ABCManager
import org.libraryweasel.servo.Component
import org.libraryweasel.servo.Service
import org.libraryweasel.xodus.api.EntityStoreInstanceManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component(ABCManager::class)
class ABCXodusStore : ABCManager {
    @Service @Volatile
    private lateinit var entityStore: EntityStoreInstanceManager

    private val documentClass = "music.abc.Document"

    private fun start() {
    }

    private fun username(account: Account) : String = account.principal.name

    override fun allABCDocuments(account: Account): Flux<ABCDocument> {
        return try {
            entityStore.instance.computeInReadonlyTransaction { txn ->
                val results = txn.find(documentClass, "username", username(account))
                val documents = results.map { it ->
                    ABCDocument(it.id.localId, it.getProperty("document") as String)
                }
                Flux.fromIterable(documents)
            }
        } catch (ex: Exception) {
            Flux.error(ex)
        }
    }

    override fun fetchABCDocument(account: Account, id: Long): Mono<ABCDocument> {
        return try {
            entityStore.instance.computeInReadonlyTransaction { txn ->
                val entity = fetchABCDocumentEntity(txn, account, id)
                if (entity != null) {
                    Mono.just(ABCDocument(id, entity.getProperty("document") as String))
                } else {
                    Mono.error(RuntimeException("document not owned by user"))
                }
            }
        } catch (ex: Exception) {
            Mono.error(ex)
        }

    }

    override fun createABCDocument(account: Account, document: String): Mono<ABCDocument> {
        return try {
            entityStore.instance.computeInTransaction { txn ->
                val abcDocument = txn.newEntity(documentClass)
                abcDocument.setProperty("name", documentToName(document))
                abcDocument.setProperty("document", document)
                abcDocument.setProperty("username", username(account))
                txn.saveEntity(abcDocument)
                Mono.just(ABCDocument(abcDocument.id.localId, document))
            }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    override fun updateABCDocument(account: Account, id: Long, document: String): Mono<ABCDocument> {
        return try {
            entityStore.instance.computeInTransaction { txn ->
                val entity = fetchABCDocumentEntity(txn, account, id)
                if (entity != null) {
                    //TODO set other fields
                    entity.setProperty("name", documentToName(document))
                    entity.setProperty("document", document)
                    txn.saveEntity(entity)
                    Mono.just(ABCDocument(id, document))
                } else {
                    Mono.empty()
                }
            }
        } catch (ex: Exception) {
            Mono.error(ex)
        }
    }

    override fun removeABCDocument(account: Account, id: Long): Mono<Boolean> {
        return try {
            entityStore.instance.computeInTransaction { txn ->
                val result = fetchABCDocumentEntity(txn, account, id)
                if (result != null) {
                    result.delete()
                    Mono.just(true)
                } else {
                    Mono.just(true)
                }
            }
        } catch (ex: Exception) {
            Mono.error(ex)
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

    private fun documentToName(document: String): String {
        val title: String = document.lines().find { it.trim().startsWith("T:") }?.trim() ?: "untitled"
        val composer: String = document.lines().find { it.trim().startsWith("C:") }?.trim() ?: "unknown"
        return "$title - $composer"
    }
}
