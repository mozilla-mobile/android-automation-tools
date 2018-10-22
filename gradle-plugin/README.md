# Android Product Team's Gradle plugin

## Publishing
To publish the plugin to the gradle plugin portal, first [create credentials][]. After making your changes and bumping the version, run the following from the root directory:
```gradlew
./gradlew gradle-plugin:publishPlugins
```

## Developing the plugin against local repositories
Like [the android components suggest][components local], it's preferable to avoid depending on apps outside of the repository. Instead:
- Write unit tests
- Work against the sample app (TODO: add sample app :)

If you wish to develop against local repositories anyway, see below.

### Set a version number
Append `-SNAPSHOT` to the end of the version number in this project's build.gradle.kts. If you don't, your imports may conflict with those from remote repositories and you can end up polluting your local maven cache.

### Publish to your local maven repository
To publish the gradle plugin:
```sh
./gradlew gradle-plugin:publishToMavenLocal
```

### Import your local plugin changes into the local repository
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
    implementation("org.mozilla.apt:gradle-plugin:0.1-SNAPSHOT")
}
```

If your project applies the plugin from a remote maven repository, be sure to remove that application.

[components local]: https://mozilla-mobile.github.io/android-components/contributing/testing-components-inside-app
[create credentials]: https://guides.gradle.org/publishing-plugins-to-gradle-plugin-portal/#create_an_account_on_the_gradle_plugin_portal
