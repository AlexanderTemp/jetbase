import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * `jetbase.android.feature.api`: the PUBLIC face of a feature. It only contains navigation keys
 * (`@Serializable ... : NavKey`) and `Navigator.navigateToX()` helpers. Other features may depend on
 * an :api module to navigate into this feature without ever seeing its screens or ViewModels.
 */
class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jetbase.android.library")
            pluginManager.apply(libs.pluginId("kotlin-serialization"))

            dependencies {
                add("api", project(":core:navigation"))
                add("api", project(":core:model"))
            }
        }
    }
}
