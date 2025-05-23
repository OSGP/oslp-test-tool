plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(libs.protobufKotlin)
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
}
