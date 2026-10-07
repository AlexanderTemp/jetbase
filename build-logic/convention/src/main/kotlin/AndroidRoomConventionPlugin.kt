import androidx.room.gradle.RoomExtension
import com.jetbase.buildlogic.libs
import com.jetbase.buildlogic.library
import com.jetbase.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/** `jetbase.android.room`: Room + KSP + schema export (needed for AutoMigrations). */
class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.pluginId("ksp"))
            pluginManager.apply(libs.pluginId("room"))

            // Schema JSON files are committed to git: they are the "history" of the database.
            extensions.configure<RoomExtension> { schemaDirectory("$projectDir/schemas") }

            dependencies {
                add("implementation", libs.library("room-runtime"))
                add("implementation", libs.library("room-ktx"))
                add("ksp", libs.library("room-compiler"))
            }
        }
    }
}
