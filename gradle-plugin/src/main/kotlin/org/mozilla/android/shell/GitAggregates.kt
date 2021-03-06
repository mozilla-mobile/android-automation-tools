/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.android.shell

/** Git state retrieved from raw Git commands. */
internal class GitAggregates(
        private val git: Git
) {

    /**
     * Returns whether or not the git repository has uncommitted changes.
     *
     * If the underlying parsed command's output is unexpected, we return true, the option more
     * likely to block: it's too much work to identify valid command output.
     */
    fun hasUncommittedChanges(): Boolean = !git.status().isBlank()

    /** @return the checked out git tag, or null if there is none */
    fun getCheckedOutGitTag(): String? {
        val commandOutput = git.nameRev("--tags", "HEAD").trim() // ends in whitespace.

        // "HEAD undefined" if there is no git tag on HEAD.
        if (commandOutput.endsWith("undefined")) {
            return null
        }

        // "HEAD tags/v1.1" if there is a git tag on HEAD.
        return commandOutput.split("/", limit = 2)[1]
    }
}
