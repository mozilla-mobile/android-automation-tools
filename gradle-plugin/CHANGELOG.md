# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed

## [0.2] - 2019-03-18
### Added
* Injection of custom maven repository named "appservices" for consumers of `application-services`.

### Changed
* Upgrade `com.android.tools.build:gradle` plugin to v3.3.2 (#41)

## [0.1] - 2019-01-15
*Initial release.*

### Added
- [ValidateAndroidAppReleaseConfiguration task](https://github.com/mozilla-mobile/android-automation-tools/blob/159757dbf2032ea374923e490806f12e0f765923/gradle-plugin/src/main/kotlin/org/mozilla/android/tasks/ValidateAndroidAppReleaseConfiguration.kt#L15-L23) to validate the repository is in an appropriate state before building a release build.

[Unreleased]: https://github.com/mozilla-mobile/android-automation-tools/compare/v0.1...HEAD
[0.1]: https://github.com/mozilla-mobile/android-automation-tools/compare/afbfde421c7f289e211e389bfb6fabc31fcad506...v0.1
