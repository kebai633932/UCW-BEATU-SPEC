package com.ucw.beatu.business.landscape.presentation.model

import kotlin.random.Random

/**
 * 本地横屏视频 Mock 数据提供器。
 * 统一在此维护假数据，方便 ViewModel/测试 Activity 复用，也便于未来替换成真实 Repository。
 */
object LandscapeMockVideoProvider {

    private const val DEFAULT_PAGE_SIZE = 5
    private val baseVideos: List<VideoItem> = listOf(

        VideoItem(
            id = "video_001",
            videoUrl = "http://vjs.zencdn.net/v/oceans.mp4",
            title = "《切腹》1/2上集:浪人为何要用竹刀这般折磨自己?# 影视解说#动作冒险",
            authorName = "云哥讲电影",
            likeCount = 535,
            commentCount = 43,
            favoriteCount = 159,
            shareCount = 59
        ),
        VideoItem(
            id = "video_002",
            videoUrl = "http://www.w3school.com.cn/example/html5/mov_bbb.mp4",
            title = "Big Buck Bunny - 经典动画短片 #动画#搞笑",
            authorName = "动画世界",
            likeCount = 1234,
            commentCount = 89,
            favoriteCount = 567,
            shareCount = 234
        ),
        VideoItem(
            id = "video_003",
            videoUrl = "https://media.w3.org/2010/05/sintel/trailer.mp4",
            title = "Sintel 高清预告片 - 奇幻冒险 #奇幻#冒险",
            authorName = "电影推荐官",
            likeCount = 890,
            commentCount = 67,
            favoriteCount = 345,
            shareCount = 123
        ),
        VideoItem(
            id = "video_004",
            videoUrl = "http://vfx.mtime.cn/Video/2021/07/10/mp4/210710171112971120.mp4",
            title = "影视片段 - 精彩剪辑 #影视#剪辑",
            authorName = "剪辑大师",
            likeCount = 2345,
            commentCount = 156,
            favoriteCount = 789,
            shareCount = 456
        ),
        VideoItem(
            id = "video_005",
            videoUrl = "http://vjs.zencdn.net/v/oceans.mp4",
            title = "海洋世界 - 自然风光 #自然#海洋",
            authorName = "自然探索",
            likeCount = 678,
            commentCount = 45,
            favoriteCount = 234,
            shareCount = 89
        )

    )

    /**
     * 获取指定页的横屏 Mock 视频列表。
     *
     * @param page 第几页，从 1 开始。
     * @param pageSize 每页数量，默认为 5。
     */
    fun getPage(page: Int, pageSize: Int = DEFAULT_PAGE_SIZE): List<VideoItem> {
        if (baseVideos.isEmpty() || pageSize <= 0) return emptyList()

        val seed = page.coerceAtLeast(1)
        val random = Random(seed)
        val shuffledTemplates = baseVideos.shuffled(random)

        return (0 until pageSize).map { index ->
            val template = shuffledTemplates[index % shuffledTemplates.size]
            val suffix = "_p${page}_$index"
            template.copy(
                id = template.id + suffix,
                likeCount = template.likeCount + page * (10 + index),
                commentCount = template.commentCount + page * 5,
                favoriteCount = template.favoriteCount + page * 3,
                shareCount = template.shareCount + page * 2,
                title = "${template.title} · 第${page}辑"
            )
        }
    }
}

