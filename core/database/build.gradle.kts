plugins {
    alias(libs.plugins.jetbase.android.library)
    alias(libs.plugins.jetbase.android.room)
}

android { namespace = "com.jetbase.core.database" }

dependencies {
    api(projects.core.model)

    implementation(libs.koin.android)
    implementation(platform(libs.koin.bom))
}
