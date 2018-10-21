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

group = "org.mozilla.android"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())

    // Ideally, this version would align with the application versions.
    implementation("com.android.tools.build:gradle:3.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testImplementation("org.mockito:mockito-core:2.23.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

pluginBundle {
    website = "https://github.com/mozilla-mobile/android-automation-tools/blob/master/gradle-plugin/README.md"
    vcsUrl = "https://github.com/mozilla-mobile/android-automation-tools"
    tags = listOf("mozilla", "android")
}

gradlePlugin {
    plugins {
        create("mozillaPlugin") {
            // this identifier is unused on remote
            id = "org.mozilla.android"
            displayName = "Mozilla Android Plugin"
            description = "A plugin for Android development at Mozilla"
            implementationClass = "org.mozilla.android.MozillaPlugin"
        }
    }
}

tasks.getByName("test", Test::class).apply {
    useJUnitPlatform()
}
