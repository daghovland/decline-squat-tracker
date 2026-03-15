plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.daghovland.declinesquattracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.daghovland.declinesquattracker"
        minSdk = 26          // Android 8.0 — gives us java.time without a backport library
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true       // enables Jetpack Compose (declarative UI, like SwiftUI/React)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Compose BOM pins all Compose library versions so they stay compatible with each other
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.activity:activity-compose:1.9.3")

    // ViewModel survives config changes (screen rotation etc.)
    // lifecycle-runtime-compose provides collectAsStateWithLifecycle — like useEffect for flows
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Room: SQLite ORM. Three parts:
    //   runtime  — the actual DB engine wrapper
    //   ktx      — coroutine/Flow extensions (suspend functions, Flow queries)
    //   compiler — KSP annotation processor that generates the implementation at build time
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
}
