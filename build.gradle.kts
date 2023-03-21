import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.diffplug.spotless") version "6.15.0"
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.avast.gradle.docker-compose") version "0.16.11"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    jacoco
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("$buildDir/jacoco"))
}

spotless {
    kotlin {
        ktlint("0.46.1") // Same version that CI uses
    }
}

group = "game.manager.nomic"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

sourceSets {
    create("integrations") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationsImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["integrationsRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation("org.ktorm:ktorm-support-mysql:3.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.1.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    integrationsImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    sourceSets(sourceSets.getByName("integrations"))
    dependsOn(tasks.test)
    dependsOn(integrationTests)
    reports {
        xml.required.set(true)
    }
    executionData(fileTree(buildDir).include("/jacoco/*.exec"))
}

// This disables the extraneous jar of just this application's classes with none of the dependencies
// necessary to run. The bootJar task is still enabled, and that task produces the jar with all the
// dependencies bundled, creating a one file executable essentially.
tasks.jar {
    enabled = false
}

// This task dynamically adds the dependency to enable hot reload to the bootRun task.
// It will automatically reboot the server if the built jar changes. If used alongside
// build --continuous, this implements a full hot reload feature that could be useful
// for many scenarios. The dependency is not always added to ensure that when we want
// the server to run normally, it does not automatically reboot for whatever reason.
task("addHotReload") {
    doLast {
        project.dependencies.add(
            "implementation",
            "org.springframework.boot:spring-boot-devtools"
        )
    }
}

val integrationTests: Test = task<Test>("integrationTests") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrations"].output.classesDirs
    classpath = sourceSets["integrations"].runtimeClasspath
    shouldRunAfter("test")
    finalizedBy(tasks.jacocoTestReport)

    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

integrationTests.doFirst {
    dockerCompose.exposeAsEnvironment(integrationTests)
}

dockerCompose {
    this.useComposeFiles.add("docker-compose.yml")
    this.isRequiredBy(project.tasks.findByName("integrationTests"))
}
