plugins {
    `kotlin-dsl`
}


//group = "com.avito.android.buildlogic"

dependencies {
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
}

repositories {
    google()
    mavenCentral()
}
