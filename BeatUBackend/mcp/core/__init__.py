"""核心组件模块"""

# 延迟导入以避免循环导入
__all__ = ["AgentOrchestrator", "MCPFilesystem", "MCPExecutionEngine"]

def __getattr__(name):
    if name == "AgentOrchestrator":
        from mcp.core.orchestrator import AgentOrchestrator
        return AgentOrchestrator
    elif name == "MCPFilesystem":
        from mcp.core.mcp_filesystem import MCPFilesystem
        return MCPFilesystem
    elif name == "MCPExecutionEngine":
        from mcp.core.execution_engine import MCPExecutionEngine
        return MCPExecutionEngine
    raise AttributeError(f"module {__name__!r} has no attribute {name!r}")

