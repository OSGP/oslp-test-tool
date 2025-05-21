plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
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