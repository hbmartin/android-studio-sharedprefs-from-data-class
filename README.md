# Kotlin Data Class to SharedPreferences plugin for Android Studio
Android Studio plugin to generate SharedPreferences reader / writer class based on Kotlin data class fields

# This plugin has been sunset and will receive no future updates. Please use [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) instead.

<img src="media/demo.gif" />

## Installation & Usage
1. Download from the [release page](https://github.com/hbmartin/android-studio-sharedprefs-from-data-class/releases), search for "Kotlin Data Class to SharedPreferences" in Preferences > Plugins, or visit the [plugin listing page](https://plugins.jetbrains.com/plugin/14970-kotlin-data-class-to-sharedpreferences/)

2. Place your cursor inside a data class, bring up the generator menu (command-N or control-return usually), and choose "Generate SharedPrefs"

3. 🚀

## Contributing

* [PRs](https://github.com/hbmartin/android-studio-sharedprefs-from-data-class/pulls) and [bug reports / feature requests](https://github.com/hbmartin/android-studio-sharedprefs-from-data-class/issues) are all welcome!
* This project is linted with [ktlint](https://github.com/pinterest/ktlint) via [ktlint-gradle](https://github.com/JLLeitschuh/ktlint-gradle/tags) and performs static analysis with [detekt](https://github.com/detekt/detekt)
* Treat other people with helpfulness, gratitude, and consideration! See the [Android SE CoC](https://android.stackexchange.com/conduct)

## Authors

* [Harold Martin](https://www.linkedin.com/in/harold-martin-98526971/) - harold.martin at gmail
* Significant inspiration drawn from [android-parcelable-intellij-plugin-kotlin by nekocode](https://plugins.jetbrains.com/plugin/8086-parcelable-code-generator-for-kotlin-)

## License

MIT License

Copyright (c) Harold Martin 2020

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
