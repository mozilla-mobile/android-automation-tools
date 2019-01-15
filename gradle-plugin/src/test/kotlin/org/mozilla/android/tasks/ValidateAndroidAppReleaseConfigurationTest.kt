/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.android.tasks

import com.android.build.gradle.AppPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mozilla.android.TestServiceLocator
import org.mozilla.android.shell.GitAggregates

// Missing tests:
// - Task is added to dependency graphs
internal class ValidateAndroidAppReleaseConfigurationTest {

    private lateinit var testTask: ValidateAndroidAppReleaseConfiguration

    @Mock private lateinit var mockGitAggregates: GitAggregates
    private lateinit var project: Project

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        TestServiceLocator.getMockAndOverride().also {
            `when`(it.gitAggregates).thenReturn(mockGitAggregates)
        }

        project = ProjectBuilder.builder().build()
        testTask = project.tasks.create("validate", ValidateAndroidAppReleaseConfiguration::class.java)
    }

    @Test
    fun `GIVEN the Android plugin is not applied THEN task fails`() {
        assertNull(project.plugins.findPlugin(AppPlugin::class.java)) // pre-condition: assert not applied
        assertThrows()
    }

    @Test
    fun `WHEN there are no uncommitted changes and buildVersion == gitVersion THEN tasks succeeds`() {
        initScenario() // default args are success.
        executeTaskActions()
    }

    @Test
    fun `WHEN there are uncommitted git changes THEN task fails`() {
        initScenario(hasUncommittedChanges = true)
        assertThrows()
    }

    @Test
    fun `WHEN there is no checked out git tag THEN task fails`() {
        initScenario(checkedOutGitTag = null)
        assertThrows()
    }

    @Test
    fun `WHEN there is checked out git tag is not a version THEN task fails`() {
        initScenario(checkedOutGitTag = "wip")
        assertThrows()
    }

    @Test
    fun `WHEN there is checked out git tag != buildVersion THEN task fails`() {
        initScenario(checkedOutGitTag = "v2.0", buildVersion = "1.1")
        assertThrows()
    }

    @Test
    fun `GIVEN property isPullRequest WHEN there is no checked out git tag and there are no uncommitted changes THEN task succeeds`() {
        initScenario(hasUncommittedChanges = false, checkedOutGitTag = null)
        project.extensions.add("isPullRequest", true)
        executeTaskActions()
    }

    @Test
    fun `GIVEN property isPullRequest WHEN there is no checked out git tag but there are uncommitted changes THEN task fails`() {
        initScenario(hasUncommittedChanges = true, checkedOutGitTag = null)
        project.extensions.add("isPullRequest", true)
        assertThrows()
    }

    @Test
    fun `GIVEN property noValidate THEN task will not execute`() {
        project.extensions.add("noValidate", true)
        assertFalse(testTask.onlyIf.isSatisfiedBy(testTask))
    }

    private fun executeTaskActions() {
        testTask.actions.forEach { it.execute(testTask) }
    }

    private fun assertThrows() {
        assertThrows<IllegalStateException> { executeTaskActions() }
    }

    private fun initScenario(
            hasUncommittedChanges: Boolean = false,
            checkedOutGitTag: String? = "v1.1",
            buildVersion: String = "1.1"
    ) {
        project.plugins.apply(AppPlugin::class.java).apply {
            extension.defaultConfig.versionName = buildVersion
        }

        `when`(mockGitAggregates.hasUncommittedChanges()).thenReturn(hasUncommittedChanges)
        `when`(mockGitAggregates.getCheckedOutGitTag()).thenReturn(checkedOutGitTag)
    }
}
