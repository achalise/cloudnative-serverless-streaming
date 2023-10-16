//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
//import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	//id("com.github.johnrengelman.shadow") version "8.1.1"
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.arun"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2022.0.4"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.cloud:spring-cloud-function-web")
	implementation ("org.springframework.kafka:spring-kafka")
	//implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
	//implementation("org.springframework.boot.experimental:spring-boot-thin-gradle-plugin:1.0.31.RELEASE")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation ("org.springframework.kafka:spring-kafka-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
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

//tasks.assemble{
//	dependsOn("shadowJar")
//}
//
//tasks.withType<ShadowJar> {
//    archiveClassifier.set("aws")
//	dependencies {
//		exclude("org.springframework.cloud:spring-cloud-function-web")
//	}
//	// Required for Spring
//	mergeServiceFiles()
//	append("META-INF/spring.handlers")
//	append("META-INF/spring.schemas")
//	append("META-INF/spring.tooling")
//	transform(PropertiesFileTransformer::class.java) {
//		paths.add("META-INF/spring.factories")
//		mergeStrategy = "append"
//	}
//}
//
//tasks.withType<Jar> {
//	manifest {
//		attributes["Start-Class"] = "com.arun.claimservice.ClaimserviceApplication"
//	}
//}
