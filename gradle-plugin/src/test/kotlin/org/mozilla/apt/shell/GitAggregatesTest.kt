/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.apt.shell

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mozilla.apt.anyNonNull

internal class GitAggregatesTest {

    private lateinit var testAggregates: GitAggregates
    @Mock private lateinit var mockGit: Git

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testAggregates = GitAggregates(mockGit)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "  ", "\n", " \n ", "\n\n"])
    fun `WHEN git status is empty or whitespace THEN there are no uncommitted changes`(statusValue: String) {
        `when`(mockGit.status()).thenReturn(statusValue)
        assertFalse(testAggregates.hasUncommittedChanges())
    }

    @Test
    fun `WHEN git status contains a status message THEN there are uncommitted changes`() {
        val gitStatus = """
            M gradle-plugin/build.gradle.kts
            AM gradle-plugin/src/test/kotlin/org/mozilla/apt/shell/GitAggregatesTest.kt
            AM gradle-plugin/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker
            ?? gradle-plugin/out/
        """.trimIndent()
        `when`(mockGit.status()).thenReturn(gitStatus)
        assertTrue(testAggregates.hasUncommittedChanges())
    }

    @Test
    fun `WHEN git status contains gibberish THEN there are uncommitted changes`() {
        `when`(mockGit.status()).thenReturn("this isn't a real git status")
        assertTrue(testAggregates.hasUncommittedChanges())
    }

    @Test
    fun `WHEN git name-rev returns an undefined tag THEN checked out git tag is null`() {
        `when`(mockGit.nameRev(anyNonNull())).thenReturn("HEAD undefined")
        assertNull(testAggregates.getCheckedOutGitTag())
    }

    @ParameterizedTest(name = "tag={1} WHEN name-rev={0}")
    @CsvSource(
            "HEAD tags/v1.1, v1.1",
            "HEAD tags/releases/v2.2, releases/v2.2",
            "HEAD tags/lolol, lolol\n "
    )
    fun `WHEN git name-rev returns a version tag THEN checked out git tag is the version number`(nameRevOutput: String, expectedTag: String) {
        `when`(mockGit.nameRev(anyNonNull())).thenReturn(nameRevOutput)
        assertEquals(expectedTag, testAggregates.getCheckedOutGitTag())
    }
}
