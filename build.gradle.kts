import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.11"
}

group = "me.aunique.ctf"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

paperweight {
    reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    processResources {
        filteringCharset = "UTF-8"
    }
    build {
        finalizedBy("copyJars")
    }
}

tasks.register("copyJars", Copy::class) {
    val folderPath = providers.environmentVariable("PluginFolderPath")

    if (folderPath.isPresent) {
        from(tasks.jar) {
            rename { "${rootProject.name}.jar" }
        }
        into(file(folderPath.get()))
    }
}