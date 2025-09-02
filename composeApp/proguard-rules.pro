# --- Preserve all Kotlin metadata annotations (required for reflection, serialization, etc.) ---
-keep @kotlin.Metadata class ** { *; }

# --- SLF4J (logger reflection use) ---
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**
-keepclassmembers class org.slf4j.** { *; }

# --- Ktor: Network sockets and UnixSocketAddress ---
-keep class io.ktor.network.sockets.** { *; }
-dontwarn io.ktor.network.sockets.**

# --- Keep Ktor GMTDate and serializer ---
-keep class io.ktor.util.date.GMTDate { *; }
-keep class io.ktor.util.date.GMTDate$$serializer { *; }

# --- Keep proto classes ---
-keep class com.google.** { *; }
-keep class org.opensmartgridplatform.oslp.** { *; }
-keep class com.gxf.utilities.oslp.message.signing.** { *; }

# --- General kotlinx.serialization support ---
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class ** {
    static kotlinx.serialization.KSerializer serializer(...);
}
-dontwarn kotlinx.serialization.**

# --- Keep all annotations ---
-keepattributes *Annotation*

# --- Required if you use reflection (like calling getPath() dynamically) ---
-keepclassmembers class * {
    *** get*();
    *** set*();
}

# --- Optional: suppress "unresolved references" if needed ---
-dontwarn kotlin.Metadata
-dontwarn io.ktor.**
-dontnote io.ktor.**

# Suppress warnings for logback and SLF4J
-dontwarn ch.qos.logback.**
-dontwarn org.slf4j.**
-dontwarn kotlinx.coroutines.slf4j.**

# Suppress warnings for Android and Robolectric (if not targeting Android)
-dontwarn android.os.**
-dontwarn org.robolectric.**

# Suppress warnings for Google App Engine
-dontwarn com.google.appengine.**
-dontwarn com.google.apphosting.**

# Suppress warnings for libcore (Android internals)
-dontwarn libcore.io.**

# Suppress warnings for missing LogbackLoggerWrapper methods
-dontwarn io.github.oshai.kotlinlogging.logback.internal.LogbackLoggerWrapper

# Suppress warnings for missing LogbackLogEvent methods
-dontwarn io.github.oshai.kotlinlogging.logback.internal.LogbackLogEven


-dontwarn com.oracle.svm.core.annotate.Substitute
-dontwarn com.oracle.svm.core.annotate.TargetElement
-dontwarn com.oracle.svm.core.annotate.TargetClass
