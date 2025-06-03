plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(libs.protobufKotlin)
}

kotlin {
    jvmToolchain(18)
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                register("kotlin")
            }
        }
    }
}
