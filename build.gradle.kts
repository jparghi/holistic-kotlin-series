import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
  }
}

apply(plugin = "org.jetbrains.kotlin.jvm")

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
  testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.20")
}

configure<KotlinJvmProjectExtension> {
  jvmToolchain(17)
}

val mainSources = fileTree("advanced") {
  include("**/*.kt")
  exclude("**/*Test.kt")
}

val testSources = fileTree("advanced") {
  include("**/*Test.kt")
}

tasks.named<KotlinCompile>("compileKotlin") {
  setSource(mainSources)
}

tasks.named<KotlinCompile>("compileTestKotlin") {
  setSource(testSources)
}

tasks.test {
  useJUnitPlatform()
}
