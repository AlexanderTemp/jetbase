package com.jetbase.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Enables Compose in an Android module and adds the BOM + tooling. */
internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    pluginManager.apply(libs.pluginId("compose-compiler"))

    commonExtension.buildFeatures.compose = true

    dependencies {
        val bom = libs.library("androidx-compose-bom")
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))
        add("implementation", libs.library("androidx-compose-ui-tooling-preview"))
        add("debugImplementation", libs.library("androidx-compose-ui-tooling"))

        // Instrumented Compose UI tests (run on a device/emulator: ./gradlew connectedDebugAndroidTest).
        add("androidTestImplementation", libs.library("androidx-compose-ui-test-junit4"))
        // ui-test-junit4 pulls an older Espresso that crashes on recent Android (removed
        // InputManager.getInstance); pin the latest one explicitly.
        add("androidTestImplementation", libs.library("androidx-test-espresso-core"))
        add("androidTestImplementation", libs.library("androidx-test-ext-junit"))
        add("androidTestImplementation", libs.library("androidx-test-runner"))
        add("debugImplementation", libs.library("androidx-compose-ui-test-manifest"))
    }
}
