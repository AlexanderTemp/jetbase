import com.android.build.api.dsl.ApplicationExtension
import com.jetbase.buildlogic.configureKotlinAndroid
import com.jetbase.buildlogic.configureKtfmt
import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** `jetbase.android.application`: the :app module. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // AGP 9 ships Kotlin built in ("built-in Kotlin"): there is NO need
            // to apply org.jetbrains.kotlin.android.
            pluginManager.apply("com.android.application")
            configureKtfmt()

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.version("targetSdk").toInt()
                testOptions.animationsDisabled = true
            }
        }
    }
}
