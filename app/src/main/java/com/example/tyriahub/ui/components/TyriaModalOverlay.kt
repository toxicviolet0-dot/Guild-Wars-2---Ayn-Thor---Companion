package com.example.tyriahub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * An in-composition modal container that completely replaces Android's Dialog / BasicAlertDialog.
 *
 * CRITICAL REASON:
 * Android's Compose `Dialog` uses `android.app.Dialog`, which attempts to create a child
 * window with `LayoutParams.TYPE_APPLICATION` (type 2). In an overlay service where the WindowContext
 * is `TYPE_APPLICATION_OVERLAY` (type 2038), Android's WindowManager throws:
 * `java.lang.IllegalArgumentException: Window type mismatch. Window Context's window type is 2038, while LayoutParams' type is set to 2.`
 *
 * `TyriaModalOverlay` stays entirely within the Compose rendering tree of the existing window,
 * avoiding sub-window creation, focus stealing, or Android OS window type mismatch crashes.
 */
@Composable
fun TyriaModalOverlay(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrimColor: Color = Color.Black.copy(alpha = 0.82f),
    contentAlignment: Alignment = Alignment.Center,
    zIndex: Float = 100f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(zIndex)
            .background(scrimColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismissRequest
            ),
        contentAlignment = contentAlignment
    ) {
        // Intercept clicks on the dialog content so it doesn't dismiss the scrim
        Box(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* consume click */ }
                )
        ) {
            content()
        }
    }
}
