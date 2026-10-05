plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("skript-test") version "1.0.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.skriptlang.org/releases")

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.5")
    compileOnly("com.github.SkriptLang:Skript:2.16.2") {
        isTransitive = false
    }

    testCompileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testCompileOnly("net.luckperms:api:5.5")
    testCompileOnly("com.github.SkriptLang:Skript:2.16.2") {
        isTransitive = false
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.mockito:mockito-core:5.14.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

tasks.named<org.skriptlang.gradle.test.plugin.SkriptTestTask>("skriptTest") {
    group = "execution"
    testScriptDirectory = file("src/test/skript/tests")
    extraPluginsDirectory = file("src/test/skript/plugins")
    dependsOn("jar")
    doFirst {
        delete(fileTree("src/test/skript/plugins").matching { include("Skript-LuckPerms*.jar") })
        copy {
            from(tasks.named("jar").get().outputs.files)
            into("src/test/skript/plugins")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
