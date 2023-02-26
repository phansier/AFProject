plugins {
    id("convention.detekt")
}

buildscript {
    val kotlin_version = "1.8.0"
    val navigationVersion = "2.5.3"

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
    }
}



allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
