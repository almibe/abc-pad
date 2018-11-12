/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.chamber.abc.api

import io.vertx.ext.auth.User

interface ABCManager {
    fun allABCDocumentDetails(user: User): ABCDocumentDetails
    fun fetchABCDocument(user: User, id: Long): ABCDocument
    fun createABCDocument(user: User, document: String): ABCDocument
    fun updateABCDocument(user: User, id: Long, document: String): ABCDocument
    fun removeABCDocument(user: User, id: Long): Boolean
}
