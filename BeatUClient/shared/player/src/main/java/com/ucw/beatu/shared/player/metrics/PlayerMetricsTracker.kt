package com.ucw.beatu.shared.player.metrics

import com.ucw.beatu.shared.common.metrics.MetricsTracker
import com.ucw.beatu.shared.common.metrics.PlaybackMetrics
import com.ucw.beatu.shared.common.time.Stopwatch

class PlayerMetricsTracker(private val tracker: MetricsTracker) {
    private val stopwatch = Stopwatch()

    fun markStart() {
        stopwatch.start()
    }

    fun report(videoId: String, channel: String, fps: Float, rebufferCount: Int, memoryPeakMb: Int) {
        val metrics = PlaybackMetrics(
            videoId = videoId,
            channel = channel,
            startUpTimeMs = stopwatch.elapsedMillis(),
            fps = fps,
            rebufferCount = rebufferCount,
            memoryPeakMb = memoryPeakMb,
            renderedDurationMs = 0L
        )
        tracker.track(metrics)
    }
}

