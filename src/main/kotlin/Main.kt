package std.student

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import std.student.headers.Header.Companion.getProperties
import std.student.headers.Element
import std.student.headers.Header
import std.student.headers.dos.Dos
import std.student.headers.pe.Pe
import std.student.utils.LocalPreferences.Settings
import java.io.RandomAccessFile
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.io.path.Path

private fun processFile(filePath: String): String {
    if (filePath.isEmpty())
        return "No file was properly selected!"

    val file = RandomAccessFile(filePath, "r")

    val dos = Dos(file)
    val pe = Pe(dos.header.offsetToPeSignature.data, file)

    file.close()

    return buildString {
        appendLine(dos.toString())
        appendLine()
        appendLine(pe.toString())
    }
}

private fun getHeaders(filePath: String): List<Header> {
    if (filePath.isEmpty())
        throw IllegalArgumentException("No file was properly selected!")

    val file = RandomAccessFile(filePath, "r")

    val dos = Dos(file)
    val pe = Pe(dos.header.offsetToPeSignature.data, file)

    val result = mutableListOf<Header>().apply {
        add(dos.header)
        add(dos.stub)
        add(pe.signatureHeader)
        add(pe.coffHeader)
        add(pe.optionalHeader)
        addAll(pe.sectionsHeaders)
    }

    return result
}

@Composable
fun FileSelectionButton(onFileSelected: (String) -> Unit) {
    Button(onClick = {
        val fileChooser = JFileChooser(Settings.lastOpenedDir).apply {
            fileFilter = FileNameExtensionFilter("PE Files", "exe", "dll")
        }

        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            val filePath = fileChooser.selectedFile.absolutePath
            onFileSelected(filePath)
            Settings.lastOpenedDir = fileChooser.currentDirectory.absolutePath
        }
    }) {
        Text("Select File")
    }
}

@Composable
fun ScrollableTextOutput(output: String) {
    val scrollState = rememberScrollState()

    Box {
        SelectionContainer {
            Text(
                text = output,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.verticalScroll(scrollState).fillMaxWidth().padding(end = 12.dp)
            )
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
fun TextScreen(onFileChosen: (String) -> Unit) {
    var filePath by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var isButtonVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isButtonVisible)
            FileSelectionButton(onFileSelected = { selectedFilePath ->
                filePath = selectedFilePath
                output = processFile(filePath)
                isButtonVisible = false // hide the button

                val fileName = Path(filePath).fileName.toString()
                onFileChosen(fileName)
            })

        if (output.isNotEmpty())
            ScrollableTextOutput(output)
    }
}

@Composable
fun ElementDisplay(element: Element<*>) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            Text(text = element.realName, modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(Color.LightGray)
                    .padding(8.dp)
                    .widthIn(max = 200.dp) // Set a maximum width
                    .wrapContentWidth(Alignment.Start)
                    .wrapContentHeight(Alignment.Top)
            ) {
                Text(text = element.hex, softWrap = true) // Allow text to wrap
            }
        }

        if (isExpanded)
            Text(text = element.description, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun HeadersDisplay(headers: List<Header>) {
    headers.forEach { header ->
        val headerName = header.headerName
        val headerElements: List<Element<*>> = header.getProperties()

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = headerName, modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                headerElements.forEach { element ->
                    ElementDisplay(element)
                }
            }
        }
    }
}

@Composable
fun GUIScreen(onFileChosen: (String) -> Unit) {
    var selectedFilePath by remember { mutableStateOf<String?>(null) }
    val listOfHeaders by remember { mutableStateOf(mutableStateListOf<Header>()) }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FileSelectionButton { filePath ->
                selectedFilePath = filePath
                listOfHeaders.apply {
                    clear()
                    addAll(getHeaders(filePath))
                }

                val fileName = Path(filePath).fileName.toString()
                onFileChosen(fileName)
            }
            selectedFilePath?.let {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Text(text = "Selected File: $it")
                }
            }
        }

        listOfHeaders.isNotEmpty().let {
            Box {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                    HeadersDisplay(listOfHeaders)
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }
        }
    }
}

fun main() = application {
    val appTitle = "PE Tool"
    var windowTitle by remember { mutableStateOf(appTitle) }
    var selectedMode by remember { mutableStateOf("GUI") }

    Window(
        title = windowTitle,
        onCloseRequest = ::exitApplication
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mode: ")
                Spacer(modifier = Modifier.width(4.dp))
                RadioButton(
                    selected = selectedMode == "Text",
                    onClick = { selectedMode = "Text" }
                )
                Text("Text Mode")
                Spacer(modifier = Modifier.width(4.dp))
                RadioButton(
                    selected = selectedMode == "GUI",
                    onClick = { selectedMode = "GUI" }
                )
                Text("GUI Mode")
            }

            when (selectedMode) {
                "Text" -> TextScreen(onFileChosen = { fileName ->
                    windowTitle = "$appTitle: $fileName"
                })

                "GUI" -> GUIScreen(onFileChosen = { fileName ->
                    windowTitle = "$appTitle: $fileName"
                })
            }
        }
    }
}
