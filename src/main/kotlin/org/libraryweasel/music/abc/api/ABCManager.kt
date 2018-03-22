/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc.api

import io.vertx.ext.auth.User

interface ABCManager {
    fun allABCDocuments(user: User): Map<Long, String>
    fun fetchABCDocument(user: User, id: Long): ABCDocument
    fun createABCDocument(user: User, document: String): Boolean
    fun updateABCDocument(user: User, id: Long, document: String): Boolean
    fun removeABCDocument(user: User, id: Long): Boolean
}
