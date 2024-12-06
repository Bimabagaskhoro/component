plugins {
    alias(libs.plugins.mynode.android.library)
    alias(libs.plugins.mynode.android.library.compose)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "com.bimabk.component"
}

dependencies {
    implementation(project(":common"))
}
