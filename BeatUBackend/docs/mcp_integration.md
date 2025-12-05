# MCP 功能集成说明

## 概述

BeatUBackend 已完全集成 MCP（Model Context Protocol）功能，所有 MCP 相关代码已迁移到 `BeatUBackend/mcp/` 目录下，**不再依赖外部 AgentMCP 项目**。可以通过 REST API 调用 MCP 服务。

## 功能特性

- ✅ 任务分解：自动将用户请求分解为多个子任务
- ✅ 工具发现：智能发现合适的 MCP 工具
- ✅ 执行计划：自动生成执行计划
- ✅ 结果整合：智能整合执行结果

## 配置

### 1. 环境变量配置

在 `.env` 文件中添加以下配置：

```env
# MCP LLM 配置（AgentMCP 使用）
MCP_API_KEY=your_api_key_here
MCP_BASE_URL=https://dashscope.aliyuncs.com/compatible-mode/v1
MCP_MODEL=qwen-flash

# MCP 注册表路径（可选，默认为 BeatUBackend/mcp_registry）
MCP_REGISTRY_PATH=
```

### 2. MCP 注册表

MCP 服务配置位于 `BeatUBackend/mcp_registry/` 目录下，目录结构：

```
mcp_registry/
├── food/
│   └── howtocook/
│       ├── config.json
│       └── mcp/
│           └── howtocook_mcp.json
└── ...
```

## API 接口

### 1. 处理 MCP 请求（同步）

**接口**: `POST /api/mcp/process`

**请求体**:
```json
{
  "userInput": "帮我规划一下今天的一日三餐吃啥，一个人吃。"
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "response": "处理结果..."
  }
}
```

### 2. 处理 MCP 请求（流式）

**接口**: `POST /api/mcp/process/stream`

**请求体**:
```json
{
  "userInput": "帮我规划一下今天的一日三餐吃啥，一个人吃。"
}
```

**响应**: Server-Sent Events (SSE) 格式的流式响应

## 使用示例

### Python 示例

```python
import requests

# 同步请求
response = requests.post(
    "http://localhost:9306/api/mcp/process",
    json={"userInput": "帮我查一下北京明天的天气"}
)
print(response.json())

# 流式请求
response = requests.post(
    "http://localhost:9306/api/mcp/process/stream",
    json={"userInput": "帮我查一下北京明天的天气"},
    stream=True
)
for line in response.iter_lines():
    if line:
        print(line.decode('utf-8'))
```

### cURL 示例

```bash
# 同步请求
curl -X POST "http://localhost:9306/api/mcp/process" \
  -H "Content-Type: application/json" \
  -d '{"userInput": "帮我规划一下今天的一日三餐吃啥，一个人吃。"}'

# 流式请求
curl -X POST "http://localhost:9306/api/mcp/process/stream" \
  -H "Content-Type: application/json" \
  -d '{"userInput": "帮我规划一下今天的一日三餐吃啥，一个人吃。"}'
```

## 架构说明

### 集成方式

BeatUBackend 已完全集成 MCP 功能，所有代码都在 BeatUBackend 内部：

1. **MCP 核心代码**: `mcp/` 目录包含所有 MCP 相关代码（从 AgentMCP 迁移而来）
2. **服务层封装**: `services/mcp_orchestrator_service.py` 封装了 MCP 的 `AgentOrchestrator`
3. **路由层**: `routes/mcp.py` 提供 REST API 接口
4. **配置管理**: 通过 `core/config.py` 管理 MCP 相关配置

### 代码结构

```
BeatUBackend/
├── mcp/                             # MCP 核心代码（已完全迁移）
│   ├── agents/                      # 智能体模块
│   ├── core/                        # 核心组件
│   ├── models/                      # 数据模型
│   ├── tools/                       # 工具定义
│   └── utils/                       # 工具函数
├── services/
│   └── mcp_orchestrator_service.py  # MCP 服务封装
├── routes/
│   └── mcp.py                       # MCP API 路由
├── mcp_registry/                    # MCP 服务注册表
│   └── ...
└── core/
    └── config.py                    # 配置管理（包含 MCP 配置）
```

### 依赖关系

- ✅ **完全独立**：不再依赖外部 AgentMCP 项目
- ✅ **自包含**：所有 MCP 代码都在 `BeatUBackend/mcp/` 目录下
- ✅ **可直接运行**：只需启动 BeatUBackend 即可使用 MCP 功能

## 注意事项

1. **环境变量**: MCP 使用环境变量 `API_KEY`、`BASE_URL`、`MODEL`，这些会从 BeatUBackend 的配置自动设置
2. **完全独立**: 不再需要外部 AgentMCP 项目，所有代码都在 BeatUBackend 内部
3. **依赖安装**: 需要安装 MCP 的所有依赖（已在 `requirements.txt` 和 `environment.yml` 中添加）

## 故障排查

### 1. 导入错误

如果遇到 `ImportError: 无法导入 mcp 模块`：

- 确认 `BeatUBackend/mcp/` 目录存在且包含所有必要的 Python 文件
- 确认已安装所有依赖：`pip install -r requirements.txt`
- 检查 Python 路径是否正确

### 2. API Key 错误

如果遇到 LLM API 调用失败：

- 检查 `.env` 文件中的 `MCP_API_KEY` 配置
- 确认 API Key 有效且有足够的配额

### 3. MCP 服务未找到

如果提示找不到 MCP 服务：

- 检查 `mcp_registry/` 目录是否存在
- 确认 MCP 配置文件格式正确

## 参考文档

- MCP 核心代码位于 `BeatUBackend/mcp/` 目录
- 所有 MCP 相关功能已完全集成到 BeatUBackend，无需外部依赖

