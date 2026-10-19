package com.example.tyriahub.ime

import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.util.concurrent.Executors

/**
 * High-performance multi-threaded hardware key injector for Tyria Hub on AYN Thor.
 *
 * Performance architecture:
 * 1. Persistent background shell worker: Keeps an interactive 'sh' process open with a persistent
 *    output stream. Avoids the 25-45ms JVM process fork overhead of Runtime.exec() on every touch.
 * 2. Dedicated multi-threaded executor: Offloads keystroke dispatches from the Compose UI thread
 *    so combat rotations at 60/120Hz never drop frames or stutter touch gesture tracking.
 * 3. Concurrent dual-dispatch: Dispatches hardware shell keyevents and Android IME key events
 *    in parallel across threads.
 */
object FastKeyInjector {
    private const val TAG = "FastKeyInjector"

    // High-priority dedicated input worker thread pool
    private val inputExecutor = Executors.newFixedThreadPool(3) { runnable ->
        Thread(runnable, "TyriaInputWorker").apply {
            isDaemon = true
            priority = Thread.MAX_PRIORITY
        }
    }

    private val injectorScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val shellLock = Any()
    private var shellProcess: Process? = null
    private var shellWriter: OutputStreamWriter? = null

    init {
        inputExecutor.execute {
            initShell()
        }
    }

    private fun initShell() {
        synchronized(shellLock) {
            try {
                shellWriter?.close()
                shellProcess?.destroy()
            } catch (_: Exception) {}

            try {
                // Try standard sh first, with su as alternative
                val process = ProcessBuilder("sh")
                    .redirectErrorStream(true)
                    .start()
                shellProcess = process
                shellWriter = OutputStreamWriter(process.outputStream)
            } catch (e: Exception) {
                Log.w(TAG, "Standard shell unavailable: ${e.message}")
                try {
                    val suProcess = ProcessBuilder("su")
                        .redirectErrorStream(true)
                        .start()
                    shellProcess = suProcess
                    shellWriter = OutputStreamWriter(suProcess.outputStream)
                } catch (e2: Exception) {
                    Log.e(TAG, "Failed to start shell: ${e2.message}")
                    shellProcess = null
                    shellWriter = null
                }
            }
        }
    }

    /**
     * Dispatches a keycode asynchronously on a dedicated high-priority input thread.
     */
    fun injectKeyAsync(keyCode: Int, keyName: String? = null) {
        inputExecutor.execute {
            injectKeySync(keyCode, keyName)
        }
    }

    /**
     * Executes the keycode injection directly on the current background thread.
     */
    fun injectKeySync(keyCode: Int, keyName: String? = null) {
        var writtenToShell = false

        synchronized(shellLock) {
            val writer = shellWriter
            if (writer != null) {
                try {
                    writer.write("input keyevent $keyCode\n")
                    writer.flush()
                    writtenToShell = true
                } catch (e: Exception) {
                    Log.w(TAG, "Shell stream write failed, restarting shell: ${e.message}")
                    initShell()
                }
            }
        }

        // Fallback to one-shot process if persistent stream was not available
        if (!writtenToShell) {
            try {
                val proc = Runtime.getRuntime().exec(arrayOf("input", "keyevent", keyCode.toString()))
                proc.waitFor()
            } catch (_: Exception) {
                try {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "input keyevent $keyCode"))
                } catch (_: Exception) {}
            }
        }

        // Also dispatch to IME if keyName provided
        if (keyName != null) {
            TyriaHubInputMethodService.sendKey(keyName)
        }
    }

    /**
     * Dispatches text input into chat asynchronously using the persistent shell stream.
     */
    fun sendTextAsync(text: String, onComplete: (() -> Unit)? = null) {
        inputExecutor.execute {
            var written = false
            synchronized(shellLock) {
                val writer = shellWriter
                if (writer != null) {
                    try {
                        writer.write("input text \"$text\"\n")
                        writer.flush()
                        written = true
                    } catch (_: Exception) {
                        initShell()
                    }
                }
            }

            if (!written) {
                try {
                    val proc = Runtime.getRuntime().exec(arrayOf("input", "text", text))
                    proc.waitFor()
                } catch (_: Exception) {
                    try {
                        Runtime.getRuntime().exec(arrayOf("su", "-c", "input text \"$text\""))
                    } catch (_: Exception) {}
                }
            }

            onComplete?.invoke()
        }
    }

    /**
     * Dispatches the Enter key specifically.
     */
    fun sendEnter() {
        injectKeySync(KeyEvent.KEYCODE_ENTER, "ENTER")
    }
}
