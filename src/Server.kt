import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
    val clientThreads: MutableList<ClientThread> = mutableListOf()
    val port = 3333
    val serverSocket = ServerSocket(port)

    var countTemp = 0

    while (true) {
        val clientSocket = serverSocket.accept()
        val thread = ClientThread(clientSocket, clientThreads, "Kullanıcı $countTemp")
        countTemp++

        clientThreads.add(thread)
        thread.start()
    }
}

class ClientThread(clientSocket: Socket, clientThreads: MutableList<ClientThread>, clientName: String) : Thread() {
    var inputStream: BufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    var outputStream = PrintStream(clientSocket.getOutputStream())
    var clients: MutableList<ClientThread> = clientThreads
    var fullname: String = clientName

    override fun run() {
        while (true) {
            val line = "$fullname : ${inputStream.readLine()}"
            clients.forEach { client -> client.outputStream.println(line) }
            println(line)
        }
    }
}