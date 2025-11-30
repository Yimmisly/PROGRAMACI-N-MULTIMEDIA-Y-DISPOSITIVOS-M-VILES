// Logica de la calculadora
package com.example.calculadora

import kotlin.math.*

class Calculator {
    var current: String = ""
    var previous: String = ""
    var operator: String? = null
    var lastResult: Double? = null

    // Agrega digitos y valida punto decimal
    fun inputDigit(d: Char) {
        if (d == '.' && current.contains('.')) return
        current += d
    }

    // Limpia todo
    fun clear() {
        current = ""
        previous = ""
        operator = null
        lastResult = null
    }

    // Borra ultimo caracter
    fun backspace() {
        if (current.isNotEmpty()) current = current.dropLast(1)
    }

    // Guarda operador y el valor previo
    fun setOperator(op: String) {
        if (current.isEmpty() && lastResult != null) {
            previous = lastResult.toString()
        } else {
            previous = current
        }
        current = ""
        operator = op
    }

    // Funciones avanzadas
    fun applyFunction(func: String) : String {
        val value = current.ifEmpty { previous }.toDoubleOrNull() ?: return "Error"
        val res = try {
            when(func) {
                "sin" -> sin(Math.toRadians(value))
                "cos" -> cos(Math.toRadians(value))
                "tan" -> tan(Math.toRadians(value))
                "sqrt" -> if (value >= 0) sqrt(value) else return "Error"
                "ln" -> if (value > 0) ln(value) else return "Error"
                "log" -> if (value > 0) log10(value) else return "Error"
                "fact" -> factorial(value.toInt())
                "pi" -> PI
                "e" -> E
                else -> return "Error"
            }
        } catch(e: Exception) { return "Error" }
        lastResult = res
        current = res.toString()
        previous = ""
        operator = null
        return formatResult(res)
    }

    private fun factorial(n: Int): Double {
        if (n < 0) throw IllegalArgumentException()
        var r = 1L
        for (i in 1..n) r *= i
        return r.toDouble()
    }

    // Ejecuta operaciones basicas
    fun calculate(): String {
        val a = previous.toDoubleOrNull() ?: return "Error"
        val b = current.toDoubleOrNull() ?: return "Error"
        val res = try {
            when(operator) {
                "+" -> a + b
                "-" -> a - b
                "x" -> a * b
                "/" -> if (b != 0.0) a / b else return "Error"
                "pow" -> a.pow(b)
                else -> return "Error"
            }
        } catch(e: Exception) { return "Error" }
        lastResult = res
        current = res.toString()
        previous = ""
        operator = null
        return formatResult(res)
    }

    private fun formatResult(value: Double): String {
        val longVal = value.toLong()
        return if (value == longVal.toDouble()) longVal.toString() else value.toString()
    }
}
