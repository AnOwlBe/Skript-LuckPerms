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
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.SkriptLang:Skript:2.15.0") {
        isTransitive = false
    }

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
    testScriptDirectory = file("src/test/skript")
    extraPluginsDirectory = file("src/test/plugins")
    dependsOn("jar")
    doFirst {
        delete(fileTree("src/test/plugins").matching { include("Skript-LuckPerms*.jar") })
        copy {
            from(tasks.named("jar").get().outputs.files)
            into("src/test/plugins")
        }
    }
}
