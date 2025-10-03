package com.example.basicscodelab

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.*
import java.util.*

object CalculatorLogic {

    private val functions = setOf("sin", "cos", "tan", "asin", "acos", "atan", "log", "ln", "sqrt")

    fun evaluateExpression(expression: String, isRadian: Boolean): String {
        if (expression.isEmpty()) return ""

        try {
            val preprocessedExpression = preprocess(expression)
            val tokens = tokenize(preprocessedExpression)
            val rpn = shuntingYard(tokens)
            val result = evaluateRpn(rpn, isRadian)
            return formatResult(result)
        } catch (e: Exception) {
            return "Error"
        }
    }

    private fun preprocess(expression: String): String {
        return expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", Math.PI.toString())
            .replace("inv(sin(", "asin(")
            .replace("inv(cos(", "acos(")
            .replace("inv(tan(", "atan(")
            .replace("DEL", "")
            .replace("AC", "")
    }

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        val regex = "(\\d*\\.?\\d+)|([a-zA-Z]+)|([\\^*/+\\-()!%])".toRegex()
        regex.findAll(expression).forEach {
            tokens.add(it.value)
        }
        return tokens
    }

    private fun precedence(op: String): Int {
        return when (op) {
            "!" -> 5
            "^" -> 4
            "%" -> 3
            "*", "/" -> 2
            "+", "-" -> 1
            else -> 0
        }
    }

    private fun isOperator(token: String) = token in listOf("+", "-", "*", "/", "^", "%", "!")

    private fun shuntingYard(tokens: List<String>): Queue<String> {
        val output: Queue<String> = LinkedList()
        val operators = Stack<String>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token in functions -> operators.push(token)
                token == "(" -> operators.push(token)
                token == ")" -> {
                    while (operators.isNotEmpty() && operators.peek() != "(") {
                        output.add(operators.pop())
                    }
                    if (operators.isEmpty()) throw IllegalArgumentException("Kurung tidak seimbang")
                    operators.pop()
                    if (operators.isNotEmpty() && operators.peek() in functions) {
                        output.add(operators.pop())
                    }
                }
                isOperator(token) -> {
                    while (operators.isNotEmpty() && isOperator(operators.peek()) &&
                        precedence(operators.peek()) >= precedence(token)) {
                        output.add(operators.pop())
                    }
                    operators.push(token)
                }
            }
        }

        while (operators.isNotEmpty()) {
            if (operators.peek() == "(") throw IllegalArgumentException("Kurung tidak seimbang")
            output.add(operators.pop())
        }
        return output
    }

    private fun evaluateRpn(rpn: Queue<String>, isRadian: Boolean): Double {
        val values = Stack<Double>()

        for (token in rpn) {
            when {
                token.toDoubleOrNull() != null -> values.push(token.toDouble())
                isOperator(token) -> {
                    if (token == "!") {
                        if (values.isEmpty()) throw IllegalArgumentException("Ekspresi tidak valid untuk faktorial")
                        val value = values.pop()
                        if (value < 0 || value != floor(value)) throw IllegalArgumentException("Faktorial hanya untuk bilangan bulat non-negatif")
                        var result = 1.0
                        for (i in 2..value.toInt()) { result *= i }
                        values.push(result)
                    } else if (token == "%") {
                        if (values.isEmpty()) throw IllegalArgumentException("Ekspresi persen tidak valid")
                        values.push(values.pop() / 100.0)
                    }
                    else {
                        if (values.size < 2) throw IllegalArgumentException("Ekspresi tidak valid")
                        val right = values.pop()
                        val left = values.pop()
                        val result = when (token) {
                            "+" -> left + right
                            "-" -> left - right
                            "*" -> left * right
                            "/" -> left / right
                            "^" -> left.pow(right)
                            else -> throw IllegalArgumentException("Operator tidak dikenal")
                        }
                        values.push(result)
                    }
                }
                token in functions -> {
                    if (values.isEmpty()) throw IllegalArgumentException("Ekspresi tidak valid untuk fungsi")
                    val value = values.pop()
                    val result = when (token) {
                        "sin" -> if (isRadian) sin(value) else sin(Math.toRadians(value))
                        "cos" -> if (isRadian) cos(value) else cos(Math.toRadians(value))
                        "tan" -> if (isRadian) tan(value) else tan(Math.toRadians(value))
                        "asin" -> if (isRadian) asin(value) else Math.toDegrees(asin(value))
                        "acos" -> if (isRadian) acos(value) else Math.toDegrees(acos(value))
                        "atan" -> if (isRadian) atan(value) else Math.toDegrees(atan(value))
                        "log" -> log10(value)
                        "ln" -> ln(value)
                        "sqrt" -> sqrt(value)
                        else -> throw IllegalArgumentException("Fungsi tidak dikenal")
                    }
                    values.push(result)
                }
            }
        }
        if (values.size != 1) throw IllegalArgumentException("Ekspresi akhir tidak valid")
        return values.pop()
    }

    private fun formatResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return if (abs(value % 1.0) < 1e-10) {
            value.toLong().toString()
        } else {
            String.format("%.8f", value).trimEnd('0').trimEnd('.')
        }
    }
}

