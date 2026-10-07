import com.android.build.api.dsl.LibraryExtension
import com.jetbase.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/** `jetbase.android.library.compose`: Android library with Compose (designsystem, ui). */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jetbase.android.library")
            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
    }
}
