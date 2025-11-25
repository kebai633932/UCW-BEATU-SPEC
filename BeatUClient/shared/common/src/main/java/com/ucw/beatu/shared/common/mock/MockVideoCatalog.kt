package com.ucw.beatu.shared.common.mock

import com.ucw.beatu.shared.common.mock.MockVideoCatalog.Orientation.LANDSCAPE
import com.ucw.beatu.shared.common.mock.MockVideoCatalog.Orientation.PORTRAIT

/**
 * 统一的 Demo / Mock 视频资源目录
 */

data class Video(
    val id: String,
    val url: String,
    val title: String,
    val author: String,
    val likeCount: Int,
    val commentCount: Int,
    val favoriteCount: Int,
    val shareCount: Int,
    val orientation: MockVideoCatalog.Orientation
)

object MockVideoCatalog {

    enum class Orientation {
        PORTRAIT,
        LANDSCAPE
    }

    /** 明确类型：List<Video> */
    private val portraitVideos: List<Video> = listOf(
        Video(
            id = "video_001",
            url = "http://vjs.zencdn.net/v/oceans.mp4",
            title = "《切腹》1/2上集:浪人为何要用竹刀这般折磨自己?",
            author = "云哥讲电影",
            likeCount = 535,
            commentCount = 43,
            favoriteCount = 159,
            shareCount = 59,
            orientation = PORTRAIT
        ),
        Video(
            id = "video_002",
            url = "http://www.w3school.com.cn/example/html5/mov_bbb.mp4",
            title = "Big Buck Bunny - 经典动画短片",
            author = "动画世界",
            likeCount = 1234,
            commentCount = 89,
            favoriteCount = 567,
            shareCount = 234,
            orientation = PORTRAIT
        ),
        Video(
            id = "video_003",
            url = "https://media.w3.org/2010/05/sintel/trailer.mp4",
            title = "Sintel 高清预告片 - 奇幻冒险",
            author = "电影推荐官",
            likeCount = 890,
            commentCount = 67,
            favoriteCount = 345,
            shareCount = 123,
            orientation = PORTRAIT
        ),
        Video(
            id = "video_004",
            url = "http://vfx.mtime.cn/Video/2021/07/10/mp4/210710171112971120.mp4",
            title = "影视片段 - 精彩剪辑",
            author = "剪辑大师",
            likeCount = 2345,
            commentCount = 156,
            favoriteCount = 789,
            shareCount = 456,
            orientation = PORTRAIT
        ),
        Video(
            id = "video_005",
            url = "http://vjs.zencdn.net/v/oceans.mp4",
            title = "海洋世界 - 自然风光",
            author = "自然探索",
            likeCount = 678,
            commentCount = 45,
            favoriteCount = 234,
            shareCount = 89,
            orientation = LANDSCAPE
        )
    )

    /** 显式指定类型避免递归推断失败 */
    private val landscapeVideos: List<Video> = portraitVideos.map { template ->
        template.copy(
            orientation = LANDSCAPE,
            title = "${template.title} · 横屏版"
        )
    }

    fun getPage(
        orientation: Orientation,
        page: Int,
        pageSize: Int
    ): List<Video> {

        val source: List<Video> = when (orientation) {
            PORTRAIT -> portraitVideos
            LANDSCAPE -> landscapeVideos
        }

        if (source.isEmpty() || pageSize <= 0) return emptyList()

        val seed = page.coerceAtLeast(1)
        val shuffled = source.shuffled(kotlin.random.Random(seed))

        return (0 until pageSize).map { index ->
            val template = shuffled[index % shuffled.size]
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
