/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.libraryweasel.music.abc.api

data class ABCDocument(val id: Long, val document: String)

data class ABCDocumentDetails(val id: Long, val title: String = "", val composer: String = "")
