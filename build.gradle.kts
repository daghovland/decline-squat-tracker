plugins {
    id("com.android.application") version "9.1.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
    // KSP (Kotlin Symbol Processing) is the annotation processor used by Room
    // to generate boilerplate at compile time (DAOs, query validation, etc.)
    id("com.google.devtools.ksp") version "2.3.2" apply false
}
