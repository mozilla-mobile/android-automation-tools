/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
    `java-gradle-plugin`
    `maven-publish` // local publishing
    id("com.gradle.plugin-publish") version "0.10.0" // remote publishing
}

group = "org.mozilla.apt"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(gradleApi())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

pluginBundle {
    website = "https://github.com/mozilla-mobile/android-automation-tools/blob/master/gradle-plugin/README.md"
    vcsUrl = "https://github.com/mozilla-mobile/android-automation-tools"
    tags = listOf("mozilla", "apt", "android")
}

gradlePlugin {
    plugins {
        create("mozillaPlugin") { // this identifier is unused on remote
            id = "org.mozilla.apt"
            displayName = "Mozilla APT Plugin"
            description = "A plugin for the Android Product Team (APT) at Mozilla"
            implementationClass = "org.mozilla.apt.MozillaPlugin"
        }
    }
}
