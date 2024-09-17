package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = input,
                    onValueChange = { newValue ->
                        input = newValue
                    },
                    textStyle = TextStyle(fontSize = 32.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val buttons = listOf(
                    listOf("1", "2", "3", "+", "*"),
                    listOf("4", "5", "6", "-", "/"),
                    listOf("7", "8", "9", "sqrt"),
                    listOf("0", ".", "C", "="),
                    listOf()
                )

                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { symbol ->
                            CalculatorButton(symbol) {
                                val newText = onButtonClick(input.text, symbol)
                                input = TextFieldValue(
                                    text = newText,
                                    selection = androidx.compose.ui.text.TextRange(newText.length)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Since parentheses are not used, the calculation order is strictly from left to right, with no operator precedence.",
                    style = TextStyle(fontSize = 15.sp),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                )
            }
        }
    )
}



@Composable
fun CalculatorButton(symbol: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = if (symbol == "sqrt" || symbol == "=") {
            Modifier
                .width(146.dp)
                .height(70.dp)
                .padding(3.dp)

        } else {
            Modifier
                .width(70.dp)
                .height(70.dp)
                .padding(3.dp)
        },
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(symbol, fontSize = 24.sp)
    }
}

fun onButtonClick(input: String, symbol: String): String {
    return when (symbol) {
        "=" -> {
            try {
                val result = evaluateExpression(input)
                result.toString()
            } catch (e: Exception) {
                "Error"
            }
        }
        "C" -> {
            ""
        }
        "sqrt" -> {
            try {
                val number = input.toDoubleOrNull() ?: return "Error"
                kotlin.math.sqrt(number).toString()
            } catch (e: Exception) {
                "Error"
            }
        }
        else -> {
            input + symbol
        }
    }
}

fun evaluateExpression(expression: String): String {
    return try {
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex())
        var result = tokens[0].toDouble()

        var index = 1
        while (index < tokens.size) {
            val operator = tokens[index]
            val nextNumber = tokens[index + 1].toDouble()
            when (operator) {
                "+" -> result += nextNumber
                "-" -> result -= nextNumber
                "*" -> result *= nextNumber
                "/" -> result /= nextNumber
            }
            index += 2
        }
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}



@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}
