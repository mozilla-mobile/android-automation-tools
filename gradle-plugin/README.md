# Android Product Team's Gradle plugin

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
You'll have to add the dependencies (using your own version number):
```groovy
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath "org.mozilla.apt:gradle-plugin:0.1-SNAPSHOT"
    }
}

apply plugin: org.mozilla.apt.MozillaPlugin
```

If your project already applies the plugin, be sure to remove the other import.

[components local]: https://mozilla-mobile.github.io/android-components/contributing/testing-components-inside-app
