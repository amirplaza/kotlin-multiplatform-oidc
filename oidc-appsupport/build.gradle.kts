plugins {
    id("org.publicvalue.convention.android.library")
    id("org.publicvalue.convention.kotlin.multiplatform.mobile")
    id("maven-publish")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.oidcCore)
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.browser)
            }
        }

        val commonTest by getting {
            dependencies {
            }
        }
    }
}

android {
    namespace = "org.publicvalue.multiplatform.oidc.appsupport"
}