/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.android

import org.mozilla.android.shell.Git
import org.mozilla.android.shell.GitAggregates

/**
 * A container for dependencies following the service locator pattern.
 *
 * We do not own the lifecycle of tasks that are constructed directly in projects that apply this
 * plugin so we're forced to use a dependency injection pattern if we want to test. Using a DI
 * framework in gradle sounds unnecessarily complicated (if not impossible) so we use a service
 * locator.
 */
internal open class ServiceLocator {

    open val git by lazy { Git(Runtime.getRuntime()) }
    open val gitAggregates by lazy { GitAggregates(git) }

    /* @VisibleForTesting(otherwise = NONE) */ protected fun overrideInstance(instance: ServiceLocator) {
        INSTANCE = instance
    }

    companion object {
        var INSTANCE = ServiceLocator()
            private set
    }
}
