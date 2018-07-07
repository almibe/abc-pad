
package org.libraryweasel.music.abc

import org.libraryweasel.music.abcBasePath
import org.libraryweasel.web.api.StaticFiles

class ABCStaticFiles: StaticFiles {
    override val classLoader: ClassLoader = this.javaClass.classLoader
    override val rootPath: String = abcBasePath
}
