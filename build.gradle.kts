plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.teavm:teavm-jso:0.11.0")
    api("org.teavm:teavm-jso-apis:0.11.0")
}