// Everything common (Compose, Koin, Navigation 3, core:data, own :api, tests...) comes from the
// convention plugin. Only what is specific to this feature goes here.
plugins { alias(libs.plugins.jetbase.android.feature.impl) }

android { namespace = "com.jetbase.feature.notes.impl" }

dependencies {
    // List-detail layout on tablets (Material 3 adaptive + Navigation 3).
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
}
