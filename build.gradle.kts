// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.android.kotlin.plugin)
        classpath(libs.hilt.plugin)
    }
}

//task("assignKeys") {
//    doLast {
//
//        ant.propertyfile(file: "apikey.properties") {
//        entry(key: "PUBLIC_KEY", value: publicKey)
//        entry(key: "SECRET_KEY", value: secretKey)
//        }
//    }
//}
//
//task clearKeys () {
//    doLast {
//        ant.propertyfile(file: "apikey.properties") {
//        entry(key: "PUBLIC_KEY", value: "\"\"")
//        entry(key: "SECRET_KEY", value: "\"\"")
//    }
//    }
//}