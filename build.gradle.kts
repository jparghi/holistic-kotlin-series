plugins {
  kotlin("jvm") version "2.0.0"
}

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain(17)

  sourceSets {
    val main by getting {
      kotlin.srcDir("advanced")
      kotlin.exclude("**/*Test.kt")
    }
    val test by getting {
      kotlin.srcDir("advanced")
      kotlin.include("**/*Test.kt")
    }
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testImplementation(kotlin("test-junit5"))
}

tasks.test {
  useJUnitPlatform()
}
