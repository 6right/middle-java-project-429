plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("com.diffplug.spotless") version "8.2.1"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

spotless {
    java {
        googleJavaFormat()
    }
}

dependencies {
    implementation("io.javalin:javalin:7.2.4")
    runtimeOnly("org.postgresql:postgresql:42.7.14")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.shadowJar {
    archiveFileName.set("app.jar")
    manifest {
        attributes["Main-Class"] = "hexlet.code.App"
    }
}

tasks.processResources {
    from("node_modules/@hexlet/java-flight-booking-frontend/dist") {
        into("static")
    }
}

tasks.test {
    useJUnitPlatform()
}