package com.example.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme

// Import fungsi logika dari CalculatorLogic.kt
import com.example.basicscodelab.CalculatorLogic.evaluateExpression


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                CalculatorApp()
            }
        }
    }
}

private val ButtonSize = 78.dp
private val ButtonTextSize = 32.sp
private val OperationTextSize = 30.sp
private val ControlTextSize = 24.sp
private val PaddingAroundButtons = 12.dp

private val NumberButtonColor = Color(0xFF333333) // Abu-abu Gelap (Angka)
private val OperatorButtonColor = Color(0xFFFE9F0A) // Oranye Cerah
private val ControlButtonColor = Color(0xFFA5A5A5) // Abu-abu Kontrol Terang
private val TextOnDark = Color.White
private val TextOnLight = Color.Black

@Composable
fun CalculatorApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        CalculatorLayout()
    }
}

@Composable
fun CalculatorLayout() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var showScientific by remember { mutableStateOf(false) }
    var isRadianMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display Area
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            ExpressionText(expression)
            ResultText(result)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(PaddingAroundButtons)
        ) {
            if (showScientific) {
                ScientificKeypad(
                    onInput = { expression += it },
                    onDelete = { if (expression.isNotEmpty()) expression = expression.dropLast(1) },
                    onClear = { expression = ""; result = "" },
                    onCalculate = {
                        // PASTIKAN BARIS INI MENGGUNAKAN 'isRadianMode', BUKAN 'false'
                        result = evaluateExpression(expression, isRadianMode)
                        expression = result
                    },
                    onToggleMode = { isRadianMode = !isRadianMode },
                    isRadianMode = isRadianMode,
                    onToggleScientific = { showScientific = false }
                )
            } else {
                BasicKeypad(
                    onInput = { expression += it },
                    onClear = { expression = ""; result = "" },
                    onDelete = { if (expression.isNotEmpty()) expression = expression.dropLast(1) },
                    onCalculate = {
                        result = evaluateExpression(expression, false)
                        expression = result
                    },
                    onToggleScientific = { showScientific = true }
                )
            }
        }
    }
}

@Composable
fun BasicKeypad(
    onInput: (String) -> Unit,
    onClear: () -> Unit,
    onDelete: () -> Unit,
    onCalculate: () -> Unit,
    onToggleScientific: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingAroundButtons)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)
        ) {
            CalculatorButton(text = "AC", backgroundColor = ControlButtonColor, textColor = TextOnLight, textSize = ControlTextSize, modifier = Modifier.weight(1f)) { onClear() }
            CalculatorButton(text = "%", backgroundColor = ControlButtonColor, textColor = TextOnLight, textSize = ControlTextSize, modifier = Modifier.weight(1f)) { onInput("%") }
            CalculatorButton(icon = Icons.Default.Backspace, iconColor = TextOnDark, backgroundColor = ControlButtonColor, modifier = Modifier.weight(1f)) { onDelete() }
            CalculatorButton(text = "÷", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("÷") }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton(text = "7", modifier = Modifier.weight(1f)) { onInput("7") }
            CalculatorButton(text = "8", modifier = Modifier.weight(1f)) { onInput("8") }
            CalculatorButton(text = "9", modifier = Modifier.weight(1f)) { onInput("9") }
            CalculatorButton(text = "×", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("×") }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton(text = "4", modifier = Modifier.weight(1f)) { onInput("4") }
            CalculatorButton(text = "5", modifier = Modifier.weight(1f)) { onInput("5") }
            CalculatorButton(text = "6", modifier = Modifier.weight(1f)) { onInput("6") }
            CalculatorButton(text = "-", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("-") }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton(text = "1", modifier = Modifier.weight(1f)) { onInput("1") }
            CalculatorButton(text = "2", modifier = Modifier.weight(1f)) { onInput("2") }
            CalculatorButton(text = "3", modifier = Modifier.weight(1f)) { onInput("3") }
            CalculatorButton(text = "+", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("+") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)
        ) {
            CalculatorButton(text = "SC", backgroundColor = ControlButtonColor, textColor = TextOnLight, textSize = ControlTextSize, modifier = Modifier.weight(1f)) { onToggleScientific() }
            CalculatorButton(text = "0", modifier = Modifier.weight(1f)) { onInput("0") }
            CalculatorButton(text = ".", modifier = Modifier.weight(1f)) { onInput(".") }
            CalculatorButton(text = "=", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onCalculate() }
        }
    }
}

@Composable
fun ScientificKeypad(
    onInput: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    onCalculate: () -> Unit,
    onToggleMode: () -> Unit,
    isRadianMode: Boolean,
    onToggleScientific: () -> Unit
) {
    val ScientificBg = Color(0xFF505050)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingAroundButtons)
    ) {
        val scientificTextSize = 16.sp

        // Baris 1: sin, cos, tan, rad/deg, inv
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("sin", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("sin(") }
            CalculatorButton("cos", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("cos(") }
            CalculatorButton("tan", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("tan(") }
            CalculatorButton(
                text = if (isRadianMode) "deg" else "rad",
                backgroundColor = if (isRadianMode) OperatorButtonColor else ScientificBg,
                textColor = TextOnDark,
                textSize = scientificTextSize, modifier = Modifier.weight(1f)
            ) { onToggleMode() }
            CalculatorButton("inv", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("inv(") }
        }

        // Baris 2: log, ln, (, ), !
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("log", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("log(") }
            CalculatorButton("ln", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("ln(") }
            CalculatorButton("(", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("(") }
            CalculatorButton(")", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput(")") }
            CalculatorButton("!", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("!") }
        }

        // Baris 3: π, AC, %, Backspace, ÷
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("π", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("PI") }
            CalculatorButton("AC", backgroundColor = ControlButtonColor, textColor = TextOnLight, textSize = ControlTextSize, modifier = Modifier.weight(1f)) { onClear() }
            CalculatorButton("%", backgroundColor = ControlButtonColor, textColor = TextOnLight, textSize = ControlTextSize, modifier = Modifier.weight(1f)) { onInput("%") }
            CalculatorButton(icon = Icons.Default.Backspace, iconColor = TextOnDark, backgroundColor = ControlButtonColor, modifier = Modifier.weight(1f)) { onDelete() }
            CalculatorButton("÷", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("÷") }
        }

        // Baris 4: ^, 7, 8, 9, ×
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("^", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("^") }
            CalculatorButton("7", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("7") }
            CalculatorButton("8", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("8") }
            CalculatorButton("9", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("9") }
            CalculatorButton("×", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("×") }
        }

        // Baris 5: √, 4, 5, 6, -
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("√", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("sqrt(") }
            CalculatorButton("4", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("4") }
            CalculatorButton("5", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("5") }
            CalculatorButton("6", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("6") }
            CalculatorButton("-", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("-") }
        }

        // Baris 6: 1/x, 1, 2, 3, +
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("1/x", backgroundColor = ScientificBg, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onInput("1/(") }
            CalculatorButton("1", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("1") }
            CalculatorButton("2", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("2") }
            CalculatorButton("3", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput("3") }
            CalculatorButton("+", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onInput("+") }
        }

        // Baris 7: B (Basic), 0, ., =
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(PaddingAroundButtons)) {
            CalculatorButton("B", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = scientificTextSize, modifier = Modifier.weight(1f)) { onToggleScientific() }
            // Tombol 0 dibuat lebih lebar (2 slot)
            CalculatorButton("0", backgroundColor = NumberButtonColor, modifier = Modifier.weight(2f)) { onInput("0") }
            CalculatorButton(".", backgroundColor = NumberButtonColor, modifier = Modifier.weight(1f)) { onInput(".") }
            CalculatorButton("=", backgroundColor = OperatorButtonColor, textColor = TextOnDark, textSize = OperationTextSize, modifier = Modifier.weight(1f)) { onCalculate() }
        }
    }
}

@Composable
fun RowScope.CalculatorButton(
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    backgroundColor: Color = NumberButtonColor,
    textColor: Color = TextOnDark,
    iconColor: Color = TextOnDark,
    textSize: TextUnit = ButtonTextSize,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .height(ButtonSize),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = textSize,
                color = textColor,
                softWrap = false,
                overflow = TextOverflow.Visible,
                fontWeight = FontWeight.Normal
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Control Button",
                tint = iconColor,
                modifier = Modifier.size(textSize.value.dp * 1.2f)
            )
        }
    }
}

@Composable
fun ExpressionText(expression: String) {
    Text(
        text = expression,
        fontSize = 40.sp,
        color = Color.White.copy(alpha = 0.7f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        textAlign = TextAlign.Right,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ResultText(result: String) {
    Text(
        text = if (result.isEmpty()) "0" else result,
        fontSize = 68.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        textAlign = TextAlign.Right,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Light
    )
}


@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    MaterialTheme {
        CalculatorApp()
    }
}