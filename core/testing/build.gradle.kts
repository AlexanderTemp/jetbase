plugins { alias(libs.plugins.jetbase.android.library) }

android { namespace = "com.jetbase.core.testing" }

dependencies {
    // api: any module doing testImplementation(core:testing) gets junit,
    // coroutines-test and turbine without declaring them.
    api(libs.junit4)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)

    api(projects.core.data)
    implementation(projects.core.datastore)
}
