/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.apt.ext

import java.lang.IllegalStateException

/**
 * Executes the given command, returning the standard output. Standard output is not printed to the
 * terminal; standard error is entirely ignored.
 *
 * @throws IllegalStateException when the command exits with a non-zero value
 */
internal fun Runtime.execWaitForStdOut(cmd: String): String {
    fun Process.assertNoError() {
        // We could check standard error too, but this is probably good enough.
        if (exitValue() != 0) throw IllegalStateException("Runtime exited with non-zero value: ${exitValue()}")
    }

    return exec(cmd).let { process ->
        process.waitFor()
        process.assertNoError()
        process.inputStream.bufferedReader().use { it.readText() }
    }
}
