"""AI 搜索 API 接口（用于 FastAPI 集成）

注意：此模块需要安装 FastAPI 才能使用。
如果不需要 FastAPI 集成，可以直接使用 service.py 中的 AISearchService。
"""

try:
    from typing import AsyncGenerator
    from fastapi import APIRouter, HTTPException
    from fastapi.responses import StreamingResponse
    import json
    
    from mcp.business.ai_search import AISearchService, AISearchRequest, AISearchResponse, StreamChunk
    
    router = APIRouter(prefix="/ai-search", tags=["AI Search"])
    
    # 全局服务实例（可以通过依赖注入优化）
    _service: AISearchService = None
    
    
    def get_ai_search_service() -> AISearchService:
        """获取 AI 搜索服务实例"""
        global _service
        if _service is None:
            _service = AISearchService()
        return _service
    
    
    @router.post("/search", response_model=AISearchResponse)
    async def search(request: AISearchRequest):
        """
        同步搜索接口
        
        Args:
            request: 搜索请求
        
        Returns:
            AISearchResponse: 搜索响应
        """
        try:
            service = get_ai_search_service()
            response = await service.search(request)
            return response
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"搜索失败: {str(e)}")
    
    
    @router.post("/search/stream")
    async def search_stream(request: AISearchRequest):
        """
        流式搜索接口
        
        Args:
            request: 搜索请求
        
        Returns:
            StreamingResponse: 流式响应
        """
        async def generate():
            """生成流式响应"""
            try:
                service = get_ai_search_service()
                async for chunk in service.search_stream(request):
                    # 将 StreamChunk 转换为 JSON 字符串
                    chunk_data = chunk.model_dump()
                    yield f"data: {json.dumps(chunk_data, ensure_ascii=False)}\n\n"
            except Exception as e:
                error_chunk = StreamChunk(
                    chunk_type="error",
                    content=f"处理失败: {str(e)}",
                    is_final=True
                )
                yield f"data: {json.dumps(error_chunk.model_dump(), ensure_ascii=False)}\n\n"
        
        return StreamingResponse(
            generate(),
            media_type="text/event-stream",
            headers={
                "Cache-Control": "no-cache",
                "Connection": "keep-alive",
                "X-Accel-Buffering": "no"
            }
        )
    
except ImportError:
    # FastAPI 未安装时的占位符
    router = None
    
    def get_ai_search_service():
        raise ImportError("FastAPI 未安装，无法使用 API 接口。请安装 FastAPI 或直接使用 service.py 中的 AISearchService。")

