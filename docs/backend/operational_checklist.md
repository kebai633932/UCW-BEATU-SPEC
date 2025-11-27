## 后端交付 Checklist

> 面向后端工程团队，列出从环境准备、服务部署到客户端联调所需的关键步骤与验收项。

### 1. 环境准备

1. **基础设施**
   - Kubernetes / Docker Compose / 裸机三选一，需提供公网访问或隧道。
   - MySQL 8.x、Redis 7.x、对象存储（OSS/S3）可用。
2. **CI/CD**
   - 配置 GitHub Actions / Jenkins，至少包含 `build → test → deploy`。
   - 约定环境：`dev`、`staging`、`prod`。
3. **配置管理**
   - 建议使用 `.env` 或配置中心（Apollo/Nacos）管理以下变量：
     - `DATABASE_URL`
     - `REDIS_URL`
     - `OSS_BUCKET`
     - `AI_SERVICE_ENDPOINT`
     - `JWT_SECRET` / `STATIC_TOKEN`

### 2. 服务部署顺序

1. **ContentService**
   - 建表：`videos`、`video_stats`、`comments`、`interactions`。
   - 初始化种子数据（至少 10 条 `portrait` / 5 条 `landscape`）。
   - 提供 `GET /videos`、`POST /videos/{id}/like` 等接口。
2. **AIService**
   - 首版可返回硬编码推荐/AI 评论，保证字段完整。
3. **Gateway**
   - 配置路由：`/api/videos` → ContentService。
   - 实现鉴权（短期可用静态 Token）。
   - 接入统一日志、TraceId。
4. **Observability**
   - 暴露 `POST /api/metrics/*`，将数据写入日志或 TSDB。
   - 配置基础 Dashboard（QPS、错误率、P95）。

### 3. 联调 Checklist

| 项目 | 验收方式 |
| --- | --- |
| Feed 接口 | 客户端/POSTMAN 请求 `/api/videos?page=1&limit=5` 返回 200，含 `items` 列表 |
| orientation | 请求 `orientation=landscape` 返回仅横屏数据 |
| 互动 | 调用 `POST /api/videos/{id}/like`，数据库计数 + 日志事件 |
| 评论 | 能够分页拉取 / 发布评论 |
| AI 推荐 | `POST /api/ai/recommend` 返回至少 2 条视频 |
| Metrics | 客户端上报 `POST /api/metrics/playback`，Observability 能查询 |

### 4. 上线前自检

1. **性能**：`/api/videos` 压测 100 QPS，P95 < 200 ms。
2. **稳定性**：模拟后端错误（降级 AI、Redis 宕机）时 Gateway 能返回可控错误码。
3. **安全**：接口必须校验 Token；日志需脱敏（不记录明文 Token）。
4. **监控**：设置告警阈值（错误率 >3%、首帧 >800ms、AI 超时 >10%）。

### 5. 客户端需要你做什么？

1. 提供可访问的 `BASE_URL`（HTTPS 优先）。
2. 确认上述接口已返回真实数据（或稳定 Mock），字段与 `docs/backend/api_contract.md` 一致。
3. 下发环境配置（Token、渠道 ID 等），供客户端在 `NetworkModule` 中更新。

> 完成以上 checklist 后，即可安排客户端进行真机联调与性能测试。

