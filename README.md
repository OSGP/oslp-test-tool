# OSLP Test Tool

This is a test tool for testing devices using the Open Smart Lighting Protocol (OSLP).
It is written in Kotlin and uses the Compose Multiplatform framework for the user interface.

## Project structure
This is a Kotlin Multiplatform project targeting Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` contains the common resources for the application.
  - `desktopMain` contains the desktop-specific resources and code for the application. As the application is currently only targeting desktop, this is the only platform-specific folder.
* The `protobuf` module contains the Protobuf definition of OSLP `0.6.1` updated for Protobuf version `4.31.1`, including the logic to generate and build the code.
* The project uses a Gradle build system, which is configured in the `build.gradle.kts` file.
* For connectivity, the Ktor framework is used.

## Functional description

This tool is designed to test devices that implement the Open Smart Lighting Protocol. It features a UI that allows users to send and receive messages from a connected device.
It's built to act as the platform to which OSLP enabled devices connect to.

Settings to configure:
* **Test tool address**: The address the tool will listen on.
* **Test tool port**: The port the tool will listen on.
* **Device address**: The address of the device to connect to.
* **Device port**: The port of the device to connect to.
* **Latitude**: The latitude of the device, for localized features.
* **Longitude**: The longitude of the device, for localized features.
* **Private key**: Platform private key
* **Public key**: Device public key

For more detailed documentation about OSLP and the operations please refer to the [OSLP Documentation](https://grid-exchange-fabric.gitbook.io/gxf/protocols/oslp/oslp-v0.6.1).

## Install instructions
The application can be used as a portable application by extracting the oslp-test-tool directory from the /app directory in the zip file. The application can also be installed using the provided MSI installer.

**!Do not install this application under Program Files, because of access rights!** Choose your own preferred installation location.
