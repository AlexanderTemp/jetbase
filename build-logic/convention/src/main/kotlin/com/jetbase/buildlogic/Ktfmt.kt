package com.jetbase.buildlogic

import com.ncorti.ktfmt.gradle.KtfmtExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Same formatter Seal uses: ktfmt with the official Kotlin style. */
internal fun Project.configureKtfmt() {
    pluginManager.apply(libs.pluginId("ktfmt"))
    extensions.configure<KtfmtExtension> { kotlinLangStyle() }
}
