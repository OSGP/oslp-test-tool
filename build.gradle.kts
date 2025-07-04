import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.gradleWrapperUpgrade)
}

wrapperUpgrade {
    gradle {
        register("oslp-test-tool") {
            repo.set("OSGP/oslp-test-tool")
            baseBranch.set("main")
        }
    }
}

tasks.register<Copy>("updateGitHooks") {
    description = "Copies the pre-commit Git Hook to the .git/hooks folder."
    group = "verification"
    from(file("spotless/pre-commit"))
    into(file(".git/hooks"))
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.named("updateGitHooks"))
    compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") }
}
