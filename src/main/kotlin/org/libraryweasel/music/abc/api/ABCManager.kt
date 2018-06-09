/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc.api

import io.undertow.security.idm.Account

interface ABCManager {
    fun allABCDocumentDetails(account: Account): ABCDocumentDetails
    fun fetchABCDocument(account: Account, id: Long): ABCDocument
    fun createABCDocument(account: Account, document: String): ABCDocument
    fun updateABCDocument(account: Account, id: Long, document: String): ABCDocument
    fun removeABCDocument(account: Account, id: Long): Boolean
}
