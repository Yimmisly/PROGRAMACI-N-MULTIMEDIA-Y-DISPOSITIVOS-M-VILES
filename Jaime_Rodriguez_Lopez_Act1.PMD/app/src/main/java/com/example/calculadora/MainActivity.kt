
package com.example.calculadora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityMainBinding

// Control principal de la calculadora
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calc = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurar datos despues de rotacion
        if (savedInstanceState != null) {
            binding.tvDisplay.text = savedInstanceState.getString("display")
            calc.current = savedInstanceState.getString("current", "")
            calc.previous = savedInstanceState.getString("previous", "")
            calc.operator = savedInstanceState.getString("operator")
        }

        // Configurar botones numericos
        val digits = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )
        for ((i, btn) in digits.withIndex()) {
            btn.setOnClickListener { onDigitPressed(i.toString()[0]) }
        }

        binding.btnDot.setOnClickListener { onDotPressed() }

        // Operadores basicos
        binding.btnAdd.setOnClickListener { onOperatorPressed("+") }
        binding.btnSub.setOnClickListener { onOperatorPressed("-") }
        binding.btnMul.setOnClickListener { onOperatorPressed("x") }
        binding.btnDiv.setOnClickListener { onOperatorPressed("/") }
        binding.btnEq.setOnClickListener { onEqualsPressed() }

        // Utilidades
        binding.btnC.setOnClickListener { onClearPressed() }
        binding.btnBack.setOnClickListener { onBackPressedCustom() }
        binding.btnPercent.setOnClickListener { onPercentPressed() }

        // Botones avanzados (solo en landscape)
        try {
            binding.btnSin.setOnClickListener { onFunctionPressed("sin") }
            binding.btnCos.setOnClickListener { onFunctionPressed("cos") }
            binding.btnTan.setOnClickListener { onFunctionPressed("tan") }
            binding.btnSqrt.setOnClickListener { onFunctionPressed("sqrt") }
            binding.btnLog.setOnClickListener { onFunctionPressed("log") }
            binding.btnLn.setOnClickListener { onFunctionPressed("ln") }
            binding.btnPow.setOnClickListener {
                calc.setOperator("pow")
                updateDisplay()
            }
            binding.btnFact.setOnClickListener { updateDisplay(calc.applyFunction("fact")) }
            binding.btnPi.setOnClickListener { updateDisplay(calc.applyFunction("pi")) }
            binding.btnE.setOnClickListener { updateDisplay(calc.applyFunction("e")) }
        } catch (_: Exception) {}
    }

    private fun onDigitPressed(d: Char) {
        calc.inputDigit(d)
        updateDisplay()
    }

    private fun onDotPressed() {
        calc.inputDigit('.')
        updateDisplay()
    }

    private fun onOperatorPressed(op: String) {
        calc.setOperator(op)
        updateDisplay()
    }

    private fun onEqualsPressed() {
        val res = calc.calculate()
        updateDisplay(res)
    }

    private fun onClearPressed() {
        calc.clear()
        updateDisplay()
    }

    private fun onBackPressedCustom() {
        calc.backspace()
        updateDisplay()
    }

    private fun onPercentPressed() {
        val v = calc.current.toDoubleOrNull() ?: return
        calc.current = (v / 100.0).toString()
        updateDisplay()
    }

    private fun onFunctionPressed(func: String) {
        val out = calc.applyFunction(func)
        updateDisplay(out)
    }

    // Actualiza pantalla
    private fun updateDisplay(value: String? = null) {
        if (value != null) binding.tvDisplay.text = value
        else binding.tvDisplay.text =
            if (calc.current.isNotEmpty()) calc.current else (calc.previous.ifEmpty { "0" })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("display", binding.tvDisplay.text.toString())
        outState.putString("current", calc.current)
        outState.putString("previous", calc.previous)
        outState.putString("operator", calc.operator)
    }
}
