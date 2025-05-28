import java.net.ServerSocket
import kotlinx.coroutines.*

class JvmSocketService {
    var onMessage: ((String) -> Unit)? = null
    var log: ((String) -> Unit)? = null

    fun startListening() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                log?.invoke("Start socket op poort 12121...")
                val serverSocket = ServerSocket(12121)
                log?.invoke("Socket gestart, wacht op verbindingen")

                while (true) {
                    try {
                        val client = serverSocket.accept()
                        log?.invoke("Nieuwe verbinding van ${client.inetAddress.hostAddress}")

                        val line = client.getInputStream().bufferedReader().readLine()
                        log?.invoke("Ontvangen: $line")

                        onMessage?.invoke(line ?: "(leeg bericht)")
                        client.close()
                    } catch (e: Exception) {
                        log?.invoke("Fout bij clientverwerking: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                log?.invoke("Kon socket niet starten: ${e.message}")
            }
        }
    }
}
