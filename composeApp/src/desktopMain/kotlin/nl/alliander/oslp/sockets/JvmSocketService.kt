import java.net.ServerSocket
import kotlinx.coroutines.*

class JvmSocketService : SocketService {
    override fun startListening() {
        // Blocking socket code in IO scope
        CoroutineScope(Dispatchers.IO).launch {
            val serverSocket = ServerSocket(12121)
            println("Luistert op poort 12121")

            while (true) {
                val client = serverSocket.accept()
                println("Nieuwe verbinding: ${client.inetAddress.hostAddress}")

                // Lees eventueel data
                client.getInputStream().bufferedReader().use {
                    val line = it.readLine()
                    println("Ontvangen: $line")
                }

                client.close()
            }
        }
    }
}