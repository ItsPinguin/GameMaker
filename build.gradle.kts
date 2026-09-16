// GAME MAKER

plugins {
  kotlin("jvm") version "2.4.0"
  id("com.gradleup.shadow") version "8.3.0"
  `maven-publish`
}

group = "fr.itspinguin.gamemaker"
version = "1.1.0"

repositories {
  maven {
    name = "papermc"
    url = uri("https://repo.papermc.io/repository/maven-public/")
  }

  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(kotlin("stdlib"))
  compileOnly("io.papermc.paper:paper-api:26.2.build.+")
  implementation("fr.itspinguin.resourcemanager:resource-manager:1.0.0")

  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.build {
  dependsOn(tasks.shadowJar)
}

tasks.test {
  useJUnitPlatform()
}

tasks.processResources {
  description = ""
  val props = mapOf(
    "version" to version,
    "description" to (project.description ?: "")
  )
  inputs.properties(props)
  filteringCharset = "UTF-8"
  filesMatching("plugin.yml") {
    expand(props)
  }
}

tasks.shadowJar {
  archiveClassifier.set("")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}