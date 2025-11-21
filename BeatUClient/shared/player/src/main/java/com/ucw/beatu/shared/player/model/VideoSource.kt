package com.ucw.beatu.shared.player.model

data class VideoSource(
    val videoId: String,
    val url: String,
    val qualities: List<VideoQuality> = emptyList(),
    val headers: Map<String, String> = emptyMap()
)

