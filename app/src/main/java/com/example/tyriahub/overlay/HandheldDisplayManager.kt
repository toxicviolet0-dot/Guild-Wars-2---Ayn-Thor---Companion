package com.example.tyriahub.overlay

import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display

data class DisplayItemInfo(
    val displayId: Int,
    val name: String,
    val width: Int,
    val height: Int,
    val isDefault: Boolean,
    val isBottomScreen: Boolean,
    val label: String
)

object HandheldDisplayManager {

    /**
     * Retrieves all connected displays, querying both the standard display list
     * and presentation category displays.
     */
    fun getAllDisplays(context: Context): List<Display> {
        val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val standard = dm.displays.toList()
        val presentation = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION).toList()
        return (standard + presentation).distinctBy { it.displayId }
    }

    /**
     * Returns true if the display is likely the bottom screen on a dual-screen device
     * like the AYN Thor (3.92" 1080x1240 screen vs 6.0" 1920x1080 top screen).
     */
    fun isLikelyBottomScreen(display: Display, allDisplays: List<Display>): Boolean {
        if (allDisplays.size <= 1) return false

        val currentArea = getDisplayPixelArea(display)
        val minArea = allDisplays.minOf { getDisplayPixelArea(it) }

        // The AYN Thor bottom screen has 1080x1240 (1.34 MP), which is significantly smaller
        // than the top screen (1920x1080 = 2.07 MP).
        if (currentArea == minArea && allDisplays.any { getDisplayPixelArea(it) > minArea }) {
            return true
        }

        // Secondary display (non-default) if areas are similar
        return display.displayId != Display.DEFAULT_DISPLAY
    }

    fun getDisplayPixelArea(display: Display): Long {
        val (w, h) = getDisplayDimensions(display)
        return w.toLong() * h.toLong()
    }

    fun getDisplayDimensions(display: Display): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            display.getRealMetrics(metrics)
            Pair(metrics.widthPixels, metrics.heightPixels)
        } else {
            val point = Point()
            @Suppress("DEPRECATION")
            display.getRealSize(point)
            Pair(point.x, point.y)
        }
    }

    /**
     * Automatically picks the bottom screen. Falls back to secondary screen,
     * or default display if only one screen exists.
     */
    fun findBottomDisplay(context: Context): Display {
        val all = getAllDisplays(context)
        if (all.size <= 1) {
            val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            return all.firstOrNull() ?: dm.getDisplay(Display.DEFAULT_DISPLAY)
        }

        // 1. Minimum pixel area (AYN Thor bottom 3.92" 1080x1240 < top 6.0" 1920x1080)
        val byArea = all.minByOrNull { getDisplayPixelArea(it) }
        if (byArea != null && byArea.displayId != Display.DEFAULT_DISPLAY) {
            return byArea
        }

        // 2. Any display with displayId != DEFAULT_DISPLAY
        val secondary = all.find { it.displayId != Display.DEFAULT_DISPLAY }
        if (secondary != null) return secondary

        // 3. Fallback
        return byArea ?: all.first()
    }

    /**
     * Finds the top screen (larger display or default display).
     */
    fun findTopDisplay(context: Context): Display {
        val all = getAllDisplays(context)
        if (all.size <= 1) {
            val dm = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            return all.firstOrNull() ?: dm.getDisplay(Display.DEFAULT_DISPLAY)
        }

        val byArea = all.maxByOrNull { getDisplayPixelArea(it) }
        return byArea ?: all.find { it.displayId == Display.DEFAULT_DISPLAY } ?: all.first()
    }

    /**
     * Formats detailed display info for UI selection.
     */
    fun getDisplayListInfo(context: Context): List<DisplayItemInfo> {
        val all = getAllDisplays(context)
        return all.map { d ->
            val (w, h) = getDisplayDimensions(d)
            val isBottom = isLikelyBottomScreen(d, all)
            val isDef = d.displayId == Display.DEFAULT_DISPLAY
            val tag = if (isBottom) "📱 Bottom Screen" else if (isDef) "🖥️ Top Screen" else "📺 Display ${d.displayId}"
            DisplayItemInfo(
                displayId = d.displayId,
                name = d.name ?: "Display ${d.displayId}",
                width = w,
                height = h,
                isDefault = isDef,
                isBottomScreen = isBottom,
                label = "$tag (${w}x${h} - ID ${d.displayId})"
            )
        }
    }
}
