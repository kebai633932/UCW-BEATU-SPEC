## Getting Started（上手指南骨架）

> 当前仅搭建文档结构，后续补充具体命令与环境说明。

### 1. 开发环境要求

- Android Studio / IntelliJ 版本与 Gradle 要求（待补充）。
- JDK 版本（待补充）。
- Android SDK 与 NDK 配置（待补充）。

### 2. 仓库结构

- `BeatU/`：Android 客户端。
- `BeatUAIService/`、`BeatUContentService/`、`BeatUGateway/`、`BeatUObservability/`：后端与观测性相关工程。
- `docs/`：统一文档。

### 3. Android 客户端（BeatU）运行步骤

1. 打开 `BeatU` 目录作为 Android Studio 工程（待补充细节）。
2. 同步 Gradle 并执行基础构建：`./gradlew clean assembleDebug`（Windows 需使用对应命令）。
3. 选择模拟器或真机，运行 `app`。

### 4. 文档导航

- 架构说明：`docs/architecture.md`
- API 说明：`docs/api_reference.md`
- 开发计划：`docs/development_plan.md`
- Git 使用规范：`docs/git_usage.md`


