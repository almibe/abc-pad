/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc.api

import io.undertow.security.idm.Account
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ABCManager {
    fun allABCDocuments(account: Account): Flux<ABCDocument>
    fun fetchABCDocument(account: Account, id: Long): Mono<ABCDocument>
    fun createABCDocument(account: Account, document: String): Mono<ABCDocument>
    fun updateABCDocument(account: Account, id: Long, document: String): Mono<ABCDocument>
    fun removeABCDocument(account: Account, id: Long): Mono<Boolean>
}
