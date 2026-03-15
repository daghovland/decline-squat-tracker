plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    // KSP (Kotlin Symbol Processing) is the annotation processor used by Room
    // to generate boilerplate at compile time (DAOs, query validation, etc.)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
