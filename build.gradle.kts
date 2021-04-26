import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.test"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.0.3"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.test.test.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

val asynchLog = "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"

application {
  mainClassName = launcherClassName
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web")
  implementation ("org.slf4j:slf4j-api:1.7.25")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  implementation ("com.fasterxml.jackson.core:jackson-databind:2.0.1")
  compileOnly ("org.projectlombok:lombok:1.18.20")
  annotationProcessor ("org.projectlombok:lombok:1.18.20")
  implementation ("org.apache.logging.log4j:log4j-api:2.14.1")
  implementation ("org.apache.logging.log4j:log4j-core:2.14.1")
  implementation ("io.vertx:vertx-dropwizard-metrics:4.0.3")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName,
          "--redeploy=$watchForChange",
          "--launcher-class=$launcherClassName",
          "--on-redeploy=$doOnChange",
  "-Dlog4j2.contextSelector=$asynchLog",
  "-jar your-fat-jar -Dvertx.metrics.options.enabled=true")
}
