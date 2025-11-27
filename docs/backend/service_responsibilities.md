## 后端服务职责划分

> BeatU 后端由 Gateway、ContentService、AIService、Observability 四大模块组成，可按微服务或模块化单体实现。以下内容总结职责、技术栈建议及依赖关系。

### 1. BeatUGateway（API 网关 / 聚合层）

- **职责**
  - 对外暴露 REST 接口（`/api/videos`、`/api/metrics/*` 等）。
  - 鉴权（Token / 设备指纹 / 版本灰度）、限流、重试、熔断。
  - 将请求路由到 ContentService / AIService，并聚合响应。
- **技术建议**
  - Kotlin + Ktor / Spring Cloud Gateway / NestJS 任一。
  - 接入配置中心（Apollo / Nacos）存储后端地址、流控规则。
  - 提供统一错误码与日志追踪 ID（TraceId）。
- **依赖**
  - ContentService（视频/互动/评论）
  - AIService（推荐/AI评论/清晰度）
  - Observability（日志、指标）

### 2. BeatUContentService

- **职责**
  - 视频元数据、播放地址、封面、作者信息管理。
  - Feed 列表查询（分页 + 频道 + orientation）。
  - 点赞/收藏/关注等互动写入（幂等）。
  - 评论读写，支持排序与分页。
- **数据存储**
  - MySQL（核心业务数据）。
  - Redis（Feed 缓存、互动状态短期缓存）。
  - OSS/CDN（视频/封面文件）。
- **接口实现建议**
  - `GET /videos`、`GET /videos/{id}`、`POST /videos/{id}/like` 等直接由 ContentService 提供，Gateway 仅做透传。
  - 互动写入需发布事件（Kafka / Pulsar）供 Observability 统计。

### 3. BeatUAIService

- **职责**
  - 推荐：基于用户画像/视频标签返回 `nextVideos`。
  - AI 评论问答（@元宝）。
  - 清晰度自适应策略，根据网络/设备指标给出码率建议。
- **实现思路**
  - 可先落地 Mock/Rule-based，后续接入真实模型。
  - 暴露 `POST /api/ai/*` 系列接口，使用 gRPC 或 REST 对 Gateway 提供服务。
  - 需要访问 ContentService 以获取视频标签、用户历史。

### 4. BeatUObservability

- **职责**
  - 接收客户端上报的播放指标、互动指标。
  - 采集服务端指标（接口耗时、错误率）。
  - 触发告警（首帧>800ms、互动失败率>2%、AI 超时>10%）。
- **实现建议**
  - 可使用 OpenTelemetry + Prometheus + Loki + Grafana。
  - 暴露 `POST /api/metrics/*` 给客户端，内部写入 TSDB/日志。

### 5. 依赖关系（简化）

```
Client → Gateway → (ContentService, AIService)
                       ↓
                 Observability
```

### 6. 数据流示例

1. **刷视频**：`Client → Gateway (/api/videos)` → ContentService → MySQL → 返回列表。
2. **点赞**：`Client → Gateway (/api/videos/{id}/like)` → ContentService → MySQL + Redis；异步事件 → Observability。
3. **AI 推荐**：`Client → Gateway (/api/ai/recommend)` → AIService → （需要 ContentService 提供特征）→ 返回下一批视频。
4. **指标上报**：`Client → Gateway (/api/metrics/playback)` → Observability → 告警/可视化。

### 7. 非功能性要求

- **性能**：Feed 接口 P95 < 200ms；互动写入 P95 < 300ms。
- **可用性**：核心接口 SLA ≥ 99.9%。Gateway 需支持蓝绿/灰度。
- **安全**：鉴权、限流、防重放；日志脱敏。

> 若未来扩展用户中心、内容审核等能力，可在此文档基础上新增服务职责章节。

