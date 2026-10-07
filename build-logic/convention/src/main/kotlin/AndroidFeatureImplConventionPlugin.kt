import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.library
import com.jetbase.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * `jetbase.android.feature.impl`: everything a feature implementation needs, in one line.
 *
 * An :impl module may see core:ui, core:designsystem, core:data (repository interfaces),
 * core:navigation and its OWN :api module (added automatically). It NEVER sees core:database,
 * core:datastore, or another feature's :impl.
 */
class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jetbase.android.library.compose")
            pluginManager.apply(libs.pluginId("kotlin-serialization"))

            dependencies {
                // feature/notes/impl automatically depends on feature/notes/api.
                add("implementation", project(path.removeSuffix(":impl") + ":api"))

                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:navigation"))

                add("implementation", libs.library("androidx-lifecycle-runtime-compose"))
                add("implementation", libs.library("androidx-lifecycle-viewmodel-compose"))
                add("implementation", libs.library("androidx-navigation3-runtime"))
                add("implementation", libs.library("kotlinx-serialization-json"))

                add("implementation", platform(libs.library("koin-bom")))
                add("implementation", libs.library("koin-androidx-compose"))

                add("testImplementation", project(":core:testing"))
                add("testImplementation", libs.library("turbine"))
            }
        }
    }
}
