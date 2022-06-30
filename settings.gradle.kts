import org.gradle.internal.os.OperatingSystem

rootProject.name = "orx"

@Suppress("INACCESSIBLE_TYPE")
// This is equivalent to `gradle.ext` https://stackoverflow.com/a/65377323/17977931
val openrndrClassifier: String by (gradle as ExtensionAware).extra(
    "natives-" + when (val os = OperatingSystem.current()) {
        OperatingSystem.WINDOWS -> "windows"
        OperatingSystem.LINUX -> "linux-x64"
        OperatingSystem.MAC_OS -> "macos"
        else -> error("Unsupported operating system: $os")
    }
)

val openrndrVersion =
    (extra.properties["OPENRNDR.version"] as String? ?: System.getenv("OPENRNDR_VERSION"))?.replace("v", "")
        ?: "0.5.1-SNAPSHOT"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlinApi", "1.6")
            version("kotlinLanguage", "1.6")
            version("kotlin", "1.6.20")
            version("jvmTarget", "1.8")
            version("kotlinxCoroutines", "1.6.0")
            version("kotlinLogging", "2.1.21")
            version("kotlinxSerialization", "1.3.2")
            version("spek", "2.0.15")
            version("boofcv", "0.39")
            version("kluent", "1.68")
            version("kotest", "5.2.1")
            version("junitJupiter", "5.8.2")
            version("slf4j", "1.7.36")
            version("openrndr", openrndrVersion)
            version("libfreenect", "0.5.7-1.5.7")
            version("librealsense", "2.50.0-1.5.7")
            version("gson", "2.9.0")
            version("antlr", "4.9.3")
            version("tensorflow", "0.4.0")

            library("openrndr-application", "org.openrndr", "openrndr-application").versionRef("openrndr")
            library("openrndr-extensions", "org.openrndr", "openrndr-extensions").versionRef("openrndr")
            library("openrndr-math", "org.openrndr", "openrndr-math").versionRef("openrndr")
            library("openrndr-shape", "org.openrndr", "openrndr-shape").versionRef("openrndr")
            library("openrndr-draw", "org.openrndr", "openrndr-draw").versionRef("openrndr")
            library("openrndr-event", "org.openrndr", "openrndr-event").versionRef("openrndr")
            library("openrndr-filter", "org.openrndr", "openrndr-filter").versionRef("openrndr")
            library("openrndr-dialogs", "org.openrndr", "openrndr-dialogs").versionRef("openrndr")
            library("openrndr-ffmpeg", "org.openrndr", "openrndr-ffmpeg").versionRef("openrndr")
            library("openrndr-ffmpeg-natives", "org.openrndr", "openrndr-ffmpeg-$openrndrClassifier").versionRef("openrndr")
            library("openrndr-svg", "org.openrndr", "openrndr-svg").versionRef("openrndr")
            library("openrndr-gl3-core", "org.openrndr", "openrndr-gl3").versionRef("openrndr")
            library("openrndr-gl3-natives", "org.openrndr", "openrndr-gl3-$openrndrClassifier").versionRef("openrndr")

            library("tensorflow", "org.tensorflow", "tensorflow-core-api").versionRef("tensorflow")
            library("boofcv", "org.boofcv", "boofcv-core").versionRef("boofcv")
            library("libfreenect", "org.bytedeco", "libfreenect").versionRef("libfreenect")
            library("librealsense", "org.bytedeco", "librealsense2").versionRef("librealsense")

            library("kotlin-logging", "io.github.microutils", "kotlin-logging").versionRef("kotlinLogging")
            library("kotlin-coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinxCoroutines")
            library("kotlin-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef("kotlinxSerialization")
            library("kotlin-serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core").versionRef("kotlinxSerialization")
            library("kotlin-stdlib", "org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef("kotlin")
            library("kotlin-reflect", "org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            library("kotlin-gradlePlugin", "org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            library("kotlin-scriptingJvm", "org.jetbrains.kotlin", "kotlin-scripting-jvm").versionRef("kotlin")
            library("kotlin-scriptingJvmHost", "org.jetbrains.kotlin", "kotlin-scripting-jvm-host").versionRef("kotlin")
            library("kotlin-scriptingJSR223", "org.jetbrains.kotlin", "kotlin-scripting-jsr223").versionRef("kotlin")

            library("spek-dsl", "org.spekframework.spek2", "spek-dsl-jvm").versionRef("spek")
            library("spek-junit5", "org.spekframework.spek2", "spek-runner-junit5").versionRef("spek")

            library("jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junitJupiter")
            library("jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junitJupiter")

            bundle("jupiter", listOf("jupiter-api", "jupiter-engine"))

            library("kotest", "io.kotest", "kotest-assertions-core").versionRef("kotest")
            library("kluent", "org.amshove.kluent", "kluent").versionRef("kluent")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("gson", "com.google.code.gson", "gson").versionRef("gson")
            library("antlr", "org.antlr", "antlr4").versionRef("antlr")
            library("antlrRuntime", "org.antlr", "antlr4-runtime").versionRef("antlr")
        }
    }
}

include(
    listOf(
        "openrndr-demos",
        "orx-jvm:orx-boofcv",
        "orx-camera",
        "orx-jvm:orx-chataigne",
        "orx-color",
        "orx-compositor",
        "orx-compute-graph",
        "orx-compute-graph-nodes",
        "orx-jvm:orx-dnk3",
        "orx-easing",
        "orx-jvm:orx-file-watcher",
        "orx-parameters",
        "orx-filter-extension",
        "orx-fx",
        "orx-jvm:orx-git-archiver",
        "orx-jvm:orx-git-archiver-gradle",
        "orx-glslify",
        "orx-gradient-descent",
        "orx-hash-grid",
        "orx-integral-image",
        "orx-interval-tree",
        "orx-jumpflood",
        "orx-jvm:orx-gui",
        "orx-image-fit",
        "orx-kdtree",
        "orx-jvm:orx-keyframer",
        "orx-mesh-generators",
        "orx-jvm:orx-minim",
        "orx-jvm:orx-kotlin-parser",
        "orx-jvm:orx-midi",
        "orx-no-clear",
        "orx-noise",
        "orx-obj-loader",
        "orx-jvm:orx-olive",
        "orx-jvm:orx-osc",
        "orx-palette",
        "orx-jvm:orx-panel",
        "orx-jvm:orx-poisson-fill",
        "orx-quadtree",
        "orx-jvm:orx-rabbit-control",
        "orx-jvm:orx-realsense2",
        "orx-jvm:orx-realsense2-natives-linux-x64",
        "orx-jvm:orx-realsense2-natives-macos",
        "orx-jvm:orx-realsense2-natives-windows",
        "orx-jvm:orx-runway",
        "orx-shader-phrases",
        "orx-shade-styles",
        "orx-shapes",
        "orx-jvm:orx-syphon",
        "orx-temporal-blur",
        "orx-jvm:orx-tensorflow",
        "orx-jvm:orx-tensorflow-gpu-natives-linux-x64",
        "orx-jvm:orx-tensorflow-gpu-natives-windows",
        "orx-jvm:orx-tensorflow-natives-linux-x64",
        "orx-jvm:orx-tensorflow-natives-macos",
        "orx-jvm:orx-tensorflow-natives-windows",
        "orx-timer",
        "orx-time-operators",
        "orx-jvm:orx-triangulation",
        "orx-jvm:orx-kinect-common",
        "orx-jvm:orx-kinect-v1",
        "orx-jvm:orx-kinect-v1-natives-linux-arm64",
        "orx-jvm:orx-kinect-v1-natives-linux-x64",
        "orx-jvm:orx-kinect-v1-natives-macos",
        "orx-jvm:orx-kinect-v1-natives-windows",
        "orx-jvm:orx-kinect-v1-demo",
        "orx-jvm:orx-video-profiles"
    )
)