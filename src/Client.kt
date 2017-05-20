import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.Socket

class App : Application() {
    var socket: Socket = Socket("localhost", 3333)
    var outputStream: PrintStream = PrintStream(socket.getOutputStream())
    var inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

    override fun start(stage: Stage) {
        val messageBox = TextField()
        val messageArea = TextArea()

        val vbox = VBox(messageBox,messageArea).apply {
            spacing = 15.0
            padding = Insets(15.0)
        }

        messageBox.promptText = "Mesajınız..."
        messageBox.setOnAction { e ->
            sendMessage(messageBox.text)
        }

        messageArea.isEditable = false

        Thread(Runnable {
            while (true) {
                val line = inputStream.readLine()
                messageArea.appendText("$line \n")
                messageBox.clear()
            }
        }).start()

        stage.scene = Scene(vbox)
        stage.show()
    }

    fun sendMessage(message: String) {
        outputStream.println(message)
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}