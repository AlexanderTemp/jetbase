plugins { alias(libs.plugins.jetbase.android.library) }

android { namespace = "com.jetbase.core.datastore" }

dependencies {
    api(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.mmkv)
    implementation(libs.koin.android)

    testImplementation(projects.core.testing)
    testImplementation(libs.turbine)
}
