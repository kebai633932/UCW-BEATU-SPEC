## BeatU 整体架构概览

> 说明：本文件当前为**结构骨架**，后续迭代中补充详细内容与架构图。

### 1. 仓库层级

- `BeatU/`：Android 客户端（多 Module + MVVM + Clean Architecture）。
- `BeatUAIService/`：AI 能力服务。
- `BeatUContentService/`：内容与媒体服务。
- `BeatUGateway/`：API 网关与聚合层。
- `BeatUObservability/`：日志、埋点、监控与可观测性。

### 2. Android 客户端模块规划（BeatU）

- `app/`：应用壳与导航、依赖注入入口。
- `core/`：跨 Feature 复用能力。
  - `core/common/`：通用工具类、扩展、基础类型定义。
  - `core/network/`：网络层（Retrofit/OkHttp 配置、拦截器等）。
  - `core/database/`：Room 数据库与 DAO 抽象。
  - `core/player/`：播放器抽象与 ExoPlayer 集成。
  - `core/designsystem/`：主题、组件、样式系统。
- `feature/`：按业务拆分的 Feature Module。
  - `feature/feed/`：短视频流（上下滑动播放主场景）。
  - `feature/profile/`：个人主页。
  - `feature/search/`：搜索与发现。
  - `feature/settings/`：设置与实验开关（如 AI 策略开关）。

后续将补充：

- 模块依赖关系图
- 数据流（Presentation → Domain → Data）
- 播放器生命周期与资源管理策略
- AI 相关数据流与推理路径


