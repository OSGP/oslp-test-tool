/*
 * Copyright 2025 Alliander N.V.
 */
package nl.alliander.oslp.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import nl.alliander.oslp.domain.Envelope
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object Logger {
    var loggingText by mutableStateOf("")

    fun log(message: String) {
        appendLog(message)
    }

    fun logReceive(message: String) {
        val messageToLog = message.trim().prependIndent("< ")
        log(messageToLog)
    }

    fun logReceive(envelope: Envelope) {
        with(envelope) {
            logReceive("Received:")
            logReceive("Seq: $sequenceNumber - Len: $lengthIndicator")
            logReceive(message.toString())
        }
    }

    fun logSend(message: String) {
        val messageToLog = message.trim().prependIndent("> ")
        log(messageToLog)
    }

    fun logSend(envelope: Envelope) {
        with(envelope) {
            logSend("Sent:")
            logSend("Seq: $sequenceNumber - Len: $lengthIndicator")
            logSend(message.toString())
        }
    }

    fun exportLogFile() {
        val fileChooser = JFileChooser().configureForTxtExport()
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.writeText(loggingText)
        }
    }

    private fun appendLog(text: String) {
        loggingText += "$text\n"
    }

    private fun JFileChooser.configureForTxtExport(): JFileChooser {
        dialogTitle = "Save Log files"
        fileFilter = FileNameExtensionFilter("Text Files", "txt")
        selectedFile = File("oslp-test-tool.txt")
        return this
    }
}
