# Mozilla Android gradle-plugin
The [`org.mozilla.android` gradle plugin, hosted on plugins.gradle.org][plugins], is used to share build code across Mozilla's Android projects like Firefox for Fire TV.

## Using the plugin
To use the plugin, you must first apply it and then configure specific tasks
you want to run. To apply the plugin, use the legacy plugin application method.

In `<project-root>/build.gradle`:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.mozilla.android:gradle-plugin:0.2"
  }
}
```

In the module to use the plugin, e.g. `<project-root>/app/build.gradle`:
```groovy
apply plugin: "org.mozilla.android"
```

Applying the plugin via the modern plugins DSL syntax is currently broken
([#37](https://github.com/mozilla-mobile/android-automation-tools/issues/37)).

### Tasks
The following tasks can be imported from `org.mozilla.android.tasks` (click the links for more details):
- [`ValidateAndroidAppReleaseConfiguration`][validate]: runs validation on `assembleRelease` such as ensuring there are no uncommitted changes and there is a checked out git tag

### Repository injection
Application of this plugin to a project will inject shared Mozilla repositories. Currently injected repositories are:
- `appservices`: `https://dl.bintray.com/ncalexander/application-services`

#### Adding a task to your project
After applying the plugin, you can apply a task, like `ValidateAndroidAppReleaseConfiguration`, by:
```groovy
import org.mozilla.android.tasks.*

task taskName(type: ValidateAndroidAppReleaseConfiguration) {
    // Configure the task...
}
```

## Developing the gradle-plugin
To work on the gradle-plugin, start by importing the `android-automation-tools`
repository into Android Studio.

### Developing the plugin against local repositories
Like [the android components suggest][components local], it's preferable to avoid depending on apps outside of the repository. Instead:
- Write unit tests
- Work against the sample app (TODO: add sample app :)

If you wish to develop against local repositories anyway, see below.

#### Set a version number
Append `-SNAPSHOT` to the end of the version number in this project's build.gradle.kts. If you don't, your imports may conflict with those from remote repositories and you can end up polluting your local maven cache.

#### Publish to your local maven repository
To publish the gradle plugin:
```sh
./gradlew gradle-plugin:publishToMavenLocal
```

#### Import your local plugin changes into the local repository
In the top level gradle file, you have to add the dependencies (using your own version number):
```groovy
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath "org.mozilla.android:gradle-plugin:0.1-SNAPSHOT"
    }
}

apply plugin: org.mozilla.android.MozillaPlugin
```

And again in `buildSrc/build.gradle`:
```groovy
repositories {
    mavenLocal()
}

dependencies {
    implementation("org.mozilla.android:gradle-plugin:0.1-SNAPSHOT")
}
```

If your project applies the plugin from a remote maven repository, be sure to remove that application.

### Publishing to plugins.gradle.org
To publish the plugin to the gradle plugin portal, first [create credentials][]. After making your changes and bumping the version, run the following from the root directory:
```gradlew
./gradlew gradle-plugin:publishPlugins
```

[plugins]: https://plugins.gradle.org/plugin/org.mozilla.android
[components local]: https://mozilla-mobile.github.io/android-components/contributing/testing-components-inside-app
[create credentials]: https://guides.gradle.org/publishing-plugins-to-gradle-plugin-portal/#create_an_account_on_the_gradle_plugin_portal
[validate]: https://github.com/mozilla-mobile/android-automation-tools/blob/master/gradle-plugin/src/main/kotlin/org/mozilla/android/tasks/ValidateAndroidAppReleaseConfiguration.kt
