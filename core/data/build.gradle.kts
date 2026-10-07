plugins { alias(libs.plugins.jetbase.android.library) }

android { namespace = "com.jetbase.core.data" }

dependencies {
    // api: whoever depends on core:data also "sees" the models (Note, UserSettings).
    api(projects.core.model)
    api(projects.core.common)

    // implementation: Room and MMKV stay HIDDEN. A feature cannot import NoteDao.
    implementation(projects.core.database)
    implementation(projects.core.datastore)

    implementation(libs.koin.android)

    testImplementation(projects.core.testing)
    testImplementation(libs.turbine)
}
