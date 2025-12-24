# === Stage 1: Builder ===
# 使用 JDK 镜像作为基础，用于提取层
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /application

# 传入构建好的 Jar 包
ARG JAR_FILE=target/verulia-admin.jar
COPY ${JAR_FILE} application.jar

# 关键一步：使用 layertools 提取分层文件
# 提取后，会自动生成 dependencies/, spring-boot-loader/ 等文件夹
RUN java -Djarmode=layertools -jar application.jar extract

# === Stage 2: Runner ===
# 真正的运行镜像
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /application

# 利用 Docker 层级缓存机制，我们将变动最少的层放在最前面 COPY
# 1. 依赖层 (几百MB，极少变动 -> 完美缓存)
COPY --from=builder /application/dependencies/ ./
# 2. 快照依赖 (偶尔变动)
COPY --from=builder /application/snapshot-dependencies/ ./
# 3. Spring Boot 加载器 (几乎不变)
COPY --from=builder /application/spring-boot-loader/ ./
# 4. 业务代码 (频繁变动 -> 只有这一层会频繁失效重构)
COPY --from=builder /application/application/ ./

# 使用 JarLauncher 启动，而不是直接 java -jar
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]