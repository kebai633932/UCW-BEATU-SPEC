package com.ucw.beatu.shared.player.impl

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.ucw.beatu.shared.common.logger.AppLogger
import com.ucw.beatu.shared.player.VideoPlayer
import com.ucw.beatu.shared.player.model.VideoPlayerConfig
import com.ucw.beatu.shared.player.model.VideoSource

class ExoVideoPlayer(
    context: Context,
    private val config: VideoPlayerConfig = VideoPlayerConfig()
) : VideoPlayer {

    private val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    override val player: Player = ExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .build()

    private var currentVideoId: String? = null
    private val listeners = mutableSetOf<VideoPlayer.Listener>()

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val stateName = when (playbackState) {
                    Player.STATE_IDLE -> "IDLE"
                    Player.STATE_BUFFERING -> "BUFFERING"
                    Player.STATE_READY -> "READY"
                    Player.STATE_ENDED -> "ENDED"
                    else -> "UNKNOWN($playbackState)"
                }
                AppLogger.d(TAG, "onPlaybackStateChanged: $stateName for videoId=$currentVideoId")
                if (playbackState == Player.STATE_ENDED) {
                    currentVideoId?.let { videoId ->
                        listeners.forEach { it.onPlaybackEnded(videoId) }
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                AppLogger.e(TAG, "onPlayerError: videoId=$currentVideoId, error=${error.message}", error)
                currentVideoId?.let { videoId ->
                    listeners.forEach { it.onError(videoId, error) }
                }
            }

            override fun onRenderedFirstFrame() {
                AppLogger.d(TAG, "onRenderedFirstFrame: videoId=$currentVideoId")
                currentVideoId?.let { videoId ->
                    listeners.forEach { it.onReady(videoId) }
                }
            }
        })
    }

    override fun attach(playerView: PlayerView) {
        if (playerView.player === player) return
        playerView.player = player
    }

    override fun prepare(source: VideoSource) {
        AppLogger.d(TAG, "prepare: videoId=${source.videoId}, url=${source.url}")
        currentVideoId = source.videoId
        val mediaItem = MediaItem.Builder()
            .setUri(source.url)
            .setTag(source.videoId)
            .build()
        AppLogger.d(TAG, "prepare: MediaItem created, setting to player")
        player.setMediaItem(mediaItem)
        player.prepare()
        AppLogger.d(TAG, "prepare: player.prepare() called")
    }

    override fun play() {
        player.playWhenReady = true
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun setSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    override fun release() {
        AppLogger.d(TAG, "release player $currentVideoId")
        player.release()
        listeners.clear()
    }

    override fun addListener(listener: VideoPlayer.Listener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: VideoPlayer.Listener) {
        listeners.remove(listener)
    }

    companion object {
        private const val TAG = "ExoVideoPlayer"
    }
}

