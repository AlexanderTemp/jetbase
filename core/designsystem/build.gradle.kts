plugins { alias(libs.plugins.jetbase.android.library.compose) }

android { namespace = "com.jetbase.core.designsystem" }

dependencies {
    // api: whoever uses the design system also needs the Material 3 types.
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material.iconsCore)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    // Generates Material 3 schemes from a seed color (replaces Seal's :color module).
    api(libs.materialKolor)

    implementation(libs.androidx.core.ktx)
}
