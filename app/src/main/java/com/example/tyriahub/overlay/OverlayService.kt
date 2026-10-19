package com.example.tyriahub.overlay

/**
 * Typealias and reference providing OverlayService mapping to TyriaOverlayService.
 *
 * Runs as a persistent floating overlay window (TYPE_APPLICATION_OVERLAY) targeting
 * the AYN Thor bottom screen, using a WindowContext created via:
 * createWindowContext(display, TYPE_APPLICATION_OVERLAY, null).
 */
typealias OverlayService = TyriaOverlayService
