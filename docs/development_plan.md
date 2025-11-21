## 开发计划（Development Plan）

> 说明：所有需求、Bug、技术债务必须先登记在此文档中，方可开始实现。

### 1. 当前迭代任务

- [x] 项目目录与文档骨架搭建  
  - 2025-11-18 - done by LRZ  
  - 内容：顶层仓库结构说明、Android 客户端模块规划草案、核心文档文件创建（`README.md`、`docs/architecture.md`、`docs/getting_started.md`、`docs/api_reference.md`、`docs/development_plan.md`）。

- [x] BeatU 客户端文档与需求目录结构规划  
  - 2025-11-18 - done by LRZ  
  - 内容：`BeatU/docs` 客户端文档结构（架构/Feature/播放器/AI/性能/交互/测试/ADR）与 `BeatU/docs/requirements` 需求文档目录（图片参考 + 文字总结版）骨架搭建。

- [x] BeatUClient 空视图 Activity 初始化后文档同步  
  - 2025-11-19 - done by LRZ  
  - 内容：原 `BeatU` Android 客户端已重置为 `BeatUClient`（Empty Views Activity 模板），同步更新 README、架构、上手、API 等文档中的目录命名与初始化说明。

- [x] BeatU 最新需求导入与根目录规范更新  
  - 2025-11-19 - done by LRZ  
  - 内容：依据 `BeatUClient/docs/需求.md` 及原型图补充 `.cursorrules`、README、Getting Started、Architecture、API Reference，明确 ViewPager2+ExoPlayer 播放器复用、交互/横屏/AI 能力与性能指标，并在本计划中登记后续任务。

- [x] BeatU 客户端技术方案白皮书（架构与目录规划）  
  - 2025-11-19 - done by LRZ  
  - 2025-11-20 - 已删除（架构重构后过时）
  - 成果：原文档描述旧架构（`core/*`、`domain/`、`data/`、`feature/*`），架构重构后已不再适用，已删除。新架构说明见 `docs/architecture.md` 和 `docs/重构方案.md`。

- [x] 项目文档技术栈修正（移除 Compose，明确使用原生 View 系统）  
  - 2025-11-20 - done by LRZ  
  - 内容：修正所有项目文档（`.cursorrules`、`README.md`、`docs/architecture.md`、`docs/client_tech_whitepaper.md`、`docs/getting_started.md`），明确技术栈使用**原生 View 系统**（TextView、ImageView、RecyclerView 等）+ **Jetpack View 组件**（ViewPager2、Navigation、MotionLayout）+ 传统 XML 布局，**不使用 Jetpack Compose**。统一采用 Activity/Fragment + 原生 View + XML 布局的技术栈。

- [x] 刷视频行为代码级流程文档  
  - 2025-11-20 - done by LRZ  
  - 2025-11-20 - 已删除（架构重构后过时）
  - 内容：原文档描述旧架构（`feature/feed/` → `domain/` → `data/` → `core/`），架构重构后已不再适用，已删除。

- [x] 第一阶段：基础架构搭建  
  - 2025-11-20 - done by LRZ  
  - 内容：
    1. ✅ 更新 `libs.versions.toml` 添加所有关键依赖（ExoPlayer、ViewPager2、Paging3、Retrofit、OkHttp、Room、Hilt、Navigation、Coil、Coroutines 等）
    2. ✅ 创建完整的模块化结构：
       - `core/common`、`core/network`、`core/database`、`core/player`、`core/designsystem`
       - `domain`（model、repository、usecase）
       - `data`（repository/impl、source/remote、source/local、source/player）
       - `feature/feed`、`feature/landscape`、`feature/profile`、`feature/search`、`feature/settings`、`feature/aiassistant`
    3. ✅ 更新 `settings.gradle.kts` 包含所有新模块
    4. ✅ 为每个模块创建 `build.gradle.kts` 配置文件，配置正确的依赖关系
    5. ✅ 配置 Hilt 依赖注入（根 `build.gradle.kts` 和 `app/build.gradle.kts`）
    6. ✅ 创建 `BeatUApp` Application 类（Hilt 入口）
    7. ✅ 创建 Base 类（`BaseActivity`、`BaseFragment`、`BaseViewModel`）
    8. ✅ 更新 `AndroidManifest.xml` 注册 Application 并添加网络权限
    9. ✅ 为所有模块创建基本的 `AndroidManifest.xml` 文件
    10. ✅ 启用 ViewBinding 支持
  - 成果：项目已具备完整的模块化架构骨架，所有依赖已配置，Hilt 已集成，Base 类已创建，可以开始第二阶段开发。

- [x] BeatUClient 移除 Git 子模块模式并调整文档  
  - 2025-11-20 - done by LRZ  
  - 内容：删除 `.gitmodules`，将 BeatUClient 视为父仓库内常规目录，更新 Git 使用规范以反映新的仓库结构与协作流程。

- [x] 制定 20 天阶段化分工开发流程  
  - 2025-11-20 - done by LRZ  
  - 内容：基于独立功能负责制，梳理 20 天（4 周工作日）的文档驱动流程、阶段目标、交付物与量化指标，并输出独立 `docs/20_day_feature_plan.md` 供全员对齐。

- [x] 第二阶段：核心基础设施落地  
  - 2025-11-20 - done by LRZ  
  - 内容：  
    1. `core/common`：新增 `AppResult`、`AppLogger`、`DispatcherProvider`、`MetricsTracker`、`PlaybackMetrics`、`Stopwatch`。  
    2. `core/network`：实现 `NetworkConfig`、`HeaderInterceptor`、`NetworkLoggingInterceptor`、`OkHttpProvider`、`RetrofitProvider`、`ConnectivityObserver`。  
    3. `core/database`：搭建 Room `BeatUDatabase`、DAO（`VideoDao`/`CommentDao`/`InteractionStateDao`）、Entity 与 `Converters`。  
    4. `core/player`：实现 `VideoPlayer` 抽象、`VideoSource`/`VideoQuality`、`VideoPlayerConfig`、`ExoVideoPlayer`、`VideoPlayerPool`、`PlayerMetricsTracker`。  
    5. 将上述交付同步至 `docs/architecture.md`、`docs/client_tech_whitepaper.md`，并保持未来阶段可扩展性。

- [x] 架构重构方案制定  
  - 2025-11-20 - done by Auto  
  - 内容：从技术层划分（MVVM + Clean Architecture）重构为业务边界划分，每个业务内部采用 Clean Architecture + Feature 分层，公共模块独立搭建。  
  - 成果：新增 `docs/重构方案.md`，包含：
    1. 重构背景与目标分析
    2. 业务边界识别（VideoFeed、User、Search、AI、Landscape、Settings）
    3. 新架构设计（business/* + shared/* 结构）
    4. 公共模块设计（shared/common、player、network、database、designsystem）
    5. 依赖关系图与通信方式
    6. 分步骤实施计划（阶段 0-3，共 14 个步骤）
    7. 迁移检查清单、风险与注意事项、测试策略、回滚方案
    8. 团队协作指南、常见问题解答、时间估算
  - 下一步：按照重构方案逐步执行迁移，预计 18-26 天（单人）或 10-12 天（3-4 人并行）

- [x] 阶段 0：架构重构准备与公共模块迁移  
  - 2025-11-20 - done by Auto  
  - 内容：
    1. ✅ 创建新目录结构骨架（`business/` 和 `shared/`）
    2. ✅ 迁移 `core/*` 到 `shared/*`（common、network、database、player、designsystem）
    3. ✅ 更新包名和导入路径（`core.*` → `shared.*`）
    4. ✅ 创建业务模块骨架（videofeed、user、search、ai、landscape、settings）
    5. ✅ 更新 `settings.gradle.kts` 和所有 `build.gradle.kts` 文件
    6. ✅ 完成公共设施代码编写（确保所有 shared 模块代码完整）
    7. ✅ 更新所有文档（architecture.md、getting_started.md、development_plan.md）
  - 成果：新架构目录结构已建立，公共模块已迁移并完成代码编写，业务模块骨架已创建，文档已更新。可以开始阶段 1：独立业务迁移。



> 后续迭代中，请将具体任务拆分为更细粒度条目，并在完成后标记 `[x]`，附上日期与负责人。


