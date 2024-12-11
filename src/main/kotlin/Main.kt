package std.student

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import std.student.headers.dos.header.DosHeader
import std.student.headers.dos.stub.DosStub
import std.student.headers.pe.Pe
import java.io.RandomAccessFile
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private fun processFile(filePath: String): String {
    if (filePath.isEmpty())
        return "No file was properly selected!"

    val file = RandomAccessFile(filePath, "r")

    val dosHeader = DosHeader.get(file)
    val dosStub = DosStub.get(file)
    val pe = Pe(dosHeader.offsetToPeSignature.data, file)

    file.close()

    return buildString {
        appendLine(dosHeader.toString())
        appendLine()
        appendLine(dosStub.toString())
        appendLine()
        appendLine(pe.toString())
    }
}

@Composable
@Preview
fun FileSelectionButton(onFileSelected: (String) -> Unit) {
    Button(onClick = {
        val fileChooser = JFileChooser().apply {
            fileFilter = FileNameExtensionFilter("PE Files", "exe", "dll")
        }

        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            val filePath = fileChooser.selectedFile.absolutePath
            onFileSelected(filePath)
        }
    }) {
        Text("Select File")
    }
}

@Composable
@Preview
fun ScrollableTextOutput(output: String) {
    val scrollState = rememberScrollState()

    Box {
        Text(
            text = output,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth().padding(end = 12.dp)
        )
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
fun App() {
    var filePath by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var isButtonVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isButtonVisible) {
            FileSelectionButton(onFileSelected = { selectedFilePath ->
                filePath = selectedFilePath
                output = processFile(filePath)
                isButtonVisible = false // hide the button
            })
        }

        if (output.isNotEmpty()) {
            ScrollableTextOutput(output)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
