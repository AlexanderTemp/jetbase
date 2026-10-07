plugins {
    alias(libs.plugins.jetbase.android.application)
    alias(libs.plugins.jetbase.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jetbase"

    defaultConfig {
        applicationId = "com.jetbase"
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Debug and release can be installed side by side on the same phone.
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures { buildConfig = true }

    // Generates res/xml/locales_config.xml from the values-XX folders (per-app language).
    androidResources { generateLocaleConfig = true }

    lint {
        checkDependencies = true
        abortOnError = true
        disable += "MissingTranslation"
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.navigation)
    implementation(projects.feature.notes.api)
    implementation(projects.feature.notes.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    testImplementation(projects.core.testing)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
}
