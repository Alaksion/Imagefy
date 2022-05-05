// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.get()}")
    }

}//
/**
 * This is a double workaround (bruh moment) because the IDE is bugged and catalogs are not
 * accessible in the plugins scope and to fix this we used the suppress annotation below dsl_scope_violation:
 *
 * BUT apparently the IDE also bugs when the annotation is applied to the whole scope so i
 * have to apply it for every declaration
 * */
plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    val ktVersion = libs.versions.kotlin.get()

    @Suppress("DSL_SCOPE_VIOLATION")
    val agp = libs.versions.agp.get()

    id("com.android.application") version (agp) apply false
    id("com.android.library") version (agp) apply false
    id("org.jetbrains.kotlin.android") version (ktVersion) apply false
    id("org.jetbrains.kotlin.jvm") version (ktVersion) apply (false)
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