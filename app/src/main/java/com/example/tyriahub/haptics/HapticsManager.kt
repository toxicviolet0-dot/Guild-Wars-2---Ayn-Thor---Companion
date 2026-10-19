package com.example.tyriahub.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.util.concurrent.Executors

class HapticsManager(private val context: Context) {

    private val hapticsExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "TyriaHapticsWorker").apply {
            isDaemon = true
            priority = Thread.NORM_PRIORITY
        }
    }

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    var isEnabled: Boolean = true
    var intensity: String = "MEDIUM" // "LIGHT", "MEDIUM", "HEAVY"

    private fun getDurationMultiplier(): Float {
        return when (intensity.uppercase()) {
            "LIGHT" -> 0.6f
            "HEAVY" -> 1.5f
            else -> 1.0f
        }
    }

    private fun getAmplitude(base: Int): Int {
        val mult = when (intensity.uppercase()) {
            "LIGHT" -> 0.6f
            "HEAVY" -> 1.3f
            else -> 1.0f
        }
        return (base * mult).toInt().coerceIn(1, 255)
    }

    fun playSkillTap() {
        if (!isEnabled || vibrator == null || !vibrator.hasVibrator()) return
        hapticsExecutor.execute {
            try {
                val mult = getDurationMultiplier()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && intensity == "MEDIUM") {
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val duration = (18 * mult).toLong().coerceAtLeast(10L)
                    val amp = getAmplitude(180)
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, amp))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate((18 * mult).toLong().coerceAtLeast(10L))
                }
            } catch (_: Exception) {
                // Safe fallback
            }
        }
    }

    fun playHeavyTap() {
        if (!isEnabled || vibrator == null || !vibrator.hasVibrator()) return
        hapticsExecutor.execute {
            try {
                val mult = getDurationMultiplier()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && intensity == "MEDIUM") {
                    vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val duration = (35 * mult).toLong().coerceAtLeast(15L)
                    val amp = getAmplitude(240)
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, amp))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate((35 * mult).toLong().coerceAtLeast(15L))
                }
            } catch (_: Exception) {
                // Safe fallback
            }
        }
    }

    fun playDodgePulse() {
        if (!isEnabled || vibrator == null || !vibrator.hasVibrator()) return
        hapticsExecutor.execute {
            try {
                val mult = getDurationMultiplier()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val t1 = (15 * mult).toLong().coerceAtLeast(8L)
                    val t2 = (25 * mult).toLong().coerceAtLeast(12L)
                    val timings = longArrayOf(0, t1, 30, t2)
                    val amplitudes = intArrayOf(0, getAmplitude(180), 0, getAmplitude(255))
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate((40 * mult).toLong().coerceAtLeast(20L))
                }
            } catch (_: Exception) {
                // Safe fallback
            }
        }
    }
}
