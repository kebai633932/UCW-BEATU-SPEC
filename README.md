## BeatU 项目总览

- **目标**：沉浸式短视频流 App 及配套服务（AIService、ContentService、Gateway、Observability），重点展示架构设计、性能优化、交互体验和 AI 融合能力。
- **当前阶段**：项目目录与文档结构搭建阶段，仅做结构规划，不包含具体业务代码。

### 仓库结构（顶层）

- `BeatU/`：Android 客户端 App（Kotlin + Jetpack + ExoPlayer）。
- `BeatUAIService/`：AI 能力相关服务（推荐、清晰度调节、语音识别等）。
- `BeatUContentService/`：内容管理与视频资源服务。
- `BeatUGateway/`：网关/聚合层服务。
- `BeatUObservability/`：观测性与监控相关组件。
- `docs/`：跨项目文档（架构、API、开发计划、上手指南等）。

详细架构和模块划分请参考 `docs/architecture.md` 与 `docs/development_plan.md`。


