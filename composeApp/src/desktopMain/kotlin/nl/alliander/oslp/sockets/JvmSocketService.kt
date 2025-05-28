import java.net.ServerSocket
import kotlinx.coroutines.*

class JvmSocketService {
    var onMessage: ((String) -> Unit)? = null

    fun startListening() {
        CoroutineScope(Dispatchers.IO).launch {
            val serverSocket = ServerSocket(12121)
            println("Luistert op poort 12121")

            while (true) {
                val client = serverSocket.accept()
                val line = client.getInputStream().bufferedReader().readLine()
                onMessage?.invoke(line ?: "(leeg bericht)")
                client.close()
            }
        }
    }
}