plugins { alias(libs.plugins.jetbase.android.library.compose) }

android { namespace = "com.jetbase.core.ui" }

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)

    api(libs.androidx.navigation3.ui)
}
