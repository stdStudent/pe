package std.student

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import std.student.conventions.getInstance
import std.student.headers.Element
import std.student.headers.EmbeddableElement
import std.student.headers.Header
import std.student.headers.Header.Companion.getProperties
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

    file.close()

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
private fun ElementValue(element: Element<*>) {
    var isHex by remember { mutableStateOf(true) }
    val text = if (isHex)
        element.hex
    else run {
        if (element.hex == "Absent")
            return@run "N/A" // BaseOfData for PE32+

        // Handles both numeric and string values
        val dataType = element.getDataType()
        val value = dataType.getInstance(element.hex.split(", ").joinToString(" "))
        when (value) {
            is List<*> -> value.joinToString(", ") { it.toString() }
            is ByteArray -> String(value)
            else -> value.toString()
        }
    }

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = text)
        append(text)
    }

    Text(
        text = annotatedString,
        softWrap = true,
        fontFamily = FontFamily.Monospace,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                isHex = !isHex
            }
    )
}

@Composable
fun CopyElement(element: EmbeddableElement<*>, filePath: String) {
    var showDialog by remember { mutableStateOf(false) }
    var newValue by remember { mutableStateOf("") }
    var copiedElement by remember { mutableStateOf<EmbeddableElement<*>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    // Reset values when the element changes (i.e. another file is selected)
    LaunchedEffect(element) {
        showDialog = false
        newValue = ""
        copiedElement = null
        errorMessage = null
    }

    LaunchedEffect(copiedElement) {
        success = false
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Column {
                    TextField(
                        value = newValue,
                        onValueChange = { input ->
                            val filteredInput = input.replace(Regex("\\s+"), " ") // only single space allowed
                            newValue = filteredInput
                        },
                        label = { Text("Enter New Value") },
                        placeholder = { Text("Decimal by default.") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "0xABC or ABCh for HEX.\n" +
                        "\"abc\" for ASCII.\n" +
                        "'MZ' for bytes (little endian)."
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    try {
                        val dataType = element.getDataType()
                        val newData = dataType.getInstance(newValue.trim())
                        copiedElement = element.getCopy(newData)
                        showDialog = false
                        errorMessage = null
                    } catch (e: Throwable) {
                        errorMessage = "Error: ${e.message ?: "N/A"}, cause: ${e.cause?.message ?: "N/A."}"
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    errorMessage?.let {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }

    Row {
        IconButton(onClick = { showDialog = true }) {
            if (copiedElement != null) {
                ElementValue(copiedElement!!)
            } else {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Copy Element"
                )
            }
        }

        copiedElement?.let { changedElement ->
            IconButton(onClick = {
                try {
                    val file = RandomAccessFile(filePath, "rw")
                    changedElement.embed(file)

                    success = true
                    file.close()
                } catch (e: Throwable) {
                    errorMessage = "Error: ${e.message ?: "N/A"}, cause: ${e.cause?.message ?: "N/A."}"
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Paste Element",
                    tint = if (success) Color.Green else Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun ElementDisplay(element: Element<*>, filePath: String) {
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
                ElementValue(element)
            }
            Spacer(modifier = Modifier.width(8.dp))
            CopyElement(element as EmbeddableElement<*>, filePath)
        }

        if (isExpanded)
            Text(text = element.description, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun HeadersDisplay(headers: List<Header>, filePath: String) {
    headers.forEach { header ->
        val headerName = header.headerName
        val headerElements: List<Element<*>> = header.getProperties()

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = headerName, modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                headerElements.forEach { element ->
                    ElementDisplay(element, filePath)
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

    var isOpenedSuccessfully by remember { mutableStateOf(false) }
    var openFileErrorMessage by remember { mutableStateOf<String>("") }
    fun setListOfHeaders(filePath: String) {
        listOfHeaders.apply {
            clear()

            val headers = try {
                getHeaders(filePath)
            } catch (e: Throwable) {
                isOpenedSuccessfully = false
                openFileErrorMessage = "Error: ${e.message ?: "N/A"}, cause: ${e.cause?.message ?: "N/A."}"
                return
            }

            addAll(headers)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FileSelectionButton { filePath ->
                selectedFilePath = filePath
                val fileName = Path(filePath).fileName.toString()
                setListOfHeaders(filePath)
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
                IconButton(onClick = {
                    setListOfHeaders(it)
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reread file content."
                    )
                }
            }
        }

        if (listOfHeaders.isNotEmpty()) {
            Box {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                    HeadersDisplay(listOfHeaders, selectedFilePath ?: return)
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }
        }

        if (!isOpenedSuccessfully && openFileErrorMessage.isNotEmpty()) {
            fun reset() {
                isOpenedSuccessfully = true
                openFileErrorMessage = ""
                selectedFilePath = null
            }

            AlertDialog(
                title = { Text("Error while opening the file.") },
                text = { Text(openFileErrorMessage) },
                onDismissRequest = { reset() },
                confirmButton = {
                    Button(onClick = { reset() }) {
                        Text("OK")
                    }
                }
            )
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
