import com.android.build.api.dsl.ApplicationExtension
import com.jetbase.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/** `jetbase.android.application.compose`: Compose for :app. */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            configureAndroidCompose(extensions.getByType<ApplicationExtension>())
        }
    }
}
