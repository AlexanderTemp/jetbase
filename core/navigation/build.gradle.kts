plugins { alias(libs.plugins.jetbase.android.library.compose) }

android { namespace = "com.jetbase.core.navigation" }

dependencies {
    // api: every feature :api module exposes NavKey and Navigator in its public signatures.
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}
