## 后端建设材料总览

> 目标：支撑 BeatU 客户端“可刷视频”所需的最小可行后端（Gateway + Content + AI + Observability），统一文档入口位于 `docs/backend/`。

### 目录

| 文档 | 说明 |
| --- | --- |
| `api_contract.md` | 对 `docs/api_reference.md` 的后端视角整理，包含字段约束与示例 |
| `service_responsibilities.md` | 各服务（Gateway/Content/AI/Observability）的职责、技术栈建议、依赖关系 |
| `data_models.md` | 视频/评论/用户/互动等核心模型定义（请求 & 响应） |
| `operational_checklist.md` | 环境准备、配置清单、联调/监控/验收步骤 |

### 快速对齐

1. **需求源**：`BeatUClient/docs/需求.md` + `docs/api_reference.md`。
2. **客户端依赖**：
   - `GET /api/videos` 支持 `page`、`limit`、`orientation`。
   - 互动接口（Like/Favorite）需快速返回，最多 300 ms。
   - 评论接口需支持 AI 回复（可先返回 Mock）。
3. **里程碑建议**：
   - M1：打通 Gateway → Content → 数据库，返回真实视频列表。
   - M2：补齐互动、评论、AI 推荐/问答。
   - M3：接入 Observability，输出播放/接口指标。

详细内容请按需查阅子文档，并在 `docs/development_plan.md` 中同步后端建设进度。

