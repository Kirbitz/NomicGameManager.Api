import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.jetbrains.dokka") version "1.7.20"
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

group = "game.manager.nomic"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Testing
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	testImplementation("org.mockito:mockito-junit-jupiter:5.1.1")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

	// Authentication libraries
	implementation("com.auth0:java-jwt:4.2.2")
	implementation("org.springframework.security:spring-security-crypto:6.0.1")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.bouncycastle:bcprov-jdk18on:1.72")

	// Database libraries
	implementation("org.ktorm:ktorm-support-mysql:3.6.0")
	implementation("mysql:mysql-connector-java:8.0.25")
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
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
	}
}