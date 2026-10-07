plugins { alias(libs.plugins.jetbase.android.feature.impl) }

android { namespace = "com.jetbase.feature.settings.impl" }

dependencies {
    // AppCompatDelegate.setApplicationLocales: per-app language (Android 13+ and backwards
    // compatible).
    implementation(libs.androidx.appcompat)
}
