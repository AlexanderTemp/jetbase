plugins { alias(libs.plugins.jetbase.jvm.library) }

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(platform(libs.koin.bom))
    api(libs.koin.core)

    testImplementation(libs.turbine)
}
