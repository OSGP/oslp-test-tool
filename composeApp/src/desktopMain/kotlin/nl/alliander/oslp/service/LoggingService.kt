package nl.alliander.oslp.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class LoggingService {
    var loggingText by mutableStateOf("")

    fun appendLog(text: String) {
        loggingText += "$text\n"
    }

    fun exportLogFile() {
        val fileChooser = JFileChooser().configureForTxtExport()
        val result = fileChooser.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.writeText(loggingText)
        }
    }

    private fun JFileChooser.configureForTxtExport(): JFileChooser {
        dialogTitle = "Save Log files"
        fileFilter = FileNameExtensionFilter("Text Files", "txt")
        selectedFile = File("oslp-test-tool.txt")
        return this
    }
}
