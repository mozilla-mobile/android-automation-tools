/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.apt.shell

import org.mozilla.apt.ext.execWaitForStdOut

/** Raw Git commands. */
internal class Git(
        private val runtime: Runtime
) {

    fun status(): String = runtime.execWaitForStdOut("git status --porcelain")

    fun nameRev(vararg args: String): String =
            runtime.execWaitForStdOut("git name-rev ${args.joinToString(" ")}")
}
