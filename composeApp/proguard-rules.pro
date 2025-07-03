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
-keep class com.google.gson.** { *; }
-keep class com.google.protobuf.** { *; }
-keep class org.opensmartgridplatform.oslp.** { *; }

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
