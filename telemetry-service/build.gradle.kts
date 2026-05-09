plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("com.google.protobuf") version "0.9.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "telemetry"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("io.grpc:grpc-protobuf:1.54.0")
    implementation("io.grpc:grpc-stub:1.54.0")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
    implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
    implementation("io.grpc:grpc-protobuf:1.62.2")
    implementation("io.grpc:grpc-stub:1.62.2")

    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}



protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.0"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.54.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/grpc"
            )
        }
    }
}

tasks.test {
    useJUnitPlatform()
}