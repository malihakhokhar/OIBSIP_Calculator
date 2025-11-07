package com.example.calculator

import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.databinding.ActivityMainBinding
import androidx.core.content.edit


class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding
    var number : String? = null

    var firstNumber : Double = 0.0
    var lastNumber : Double = 0.0

    var status : String? = null
    var operator : Boolean = false

    val myFormatter = DecimalFormat("######.######")

    var history = ""
    var currentResult = ""

    var dotControl : Boolean = true
    var buttonEqualsControl : Boolean = false

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        // make the button clickable
        mainBinding.btnZero.setOnClickListener {
            onNumberClick("0")
        }
        mainBinding.btnOne.setOnClickListener {
            onNumberClick("1")
        }
        mainBinding.btnTwo.setOnClickListener {
            onNumberClick("2")
        }
        mainBinding.btnThree.setOnClickListener {
            onNumberClick("3")
        }
        mainBinding.btnFour.setOnClickListener {
            onNumberClick("4")
        }
        mainBinding.btnFive.setOnClickListener {
            onNumberClick("5")
        }
        mainBinding.btnSix.setOnClickListener {
            onNumberClick("6")
        }
        mainBinding.btnSeven.setOnClickListener {
            onNumberClick("7")
        }
        mainBinding.btnEight.setOnClickListener {
            onNumberClick("8")
        }
        mainBinding.btnNine.setOnClickListener {
            onNumberClick("9")
        }

        mainBinding.btnAC.setOnClickListener {
            onButtonAC()
        }

        mainBinding.btnEqual.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            if (operator) {
                when(status) {
                    "Multiplication" -> multiply()
                    "Divide" -> divide()
                    "Subtraction" -> minus()
                    "Addition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
                mainBinding.textViewHistory.text = history.plus(currentResult).plus("=").plus(mainBinding.textViewResult.text.toString())

            }
            operator = false
            dotControl = true
            buttonEqualsControl = true

        }

        mainBinding.btnDel.setOnClickListener {
            number?.let {
                if (it.length == 1){
                    onButtonAC()
                }
                else{
                    number = it.substring(0, it.length-1)
                    mainBinding.textViewResult.text = number
                    dotControl = !number!!.contains(".")

                }
            }
        }

        mainBinding.btnDot.setOnClickListener {
            if (dotControl) {
                number = if (number == null) {
                    "0."
                } else if(buttonEqualsControl) {

                    if (mainBinding.textViewResult.text.toString().contains(".")) {
                        mainBinding.textViewResult.text.toString()
                    }
                    else {
                        mainBinding.textViewResult.text.toString().plus(".")
                    }
                }
                else {
                    "$number."
                }
                mainBinding.textViewResult.text = number
                dotControl = false
            }
        }

        // perform calculations
        mainBinding.btnPlus.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("+")

            if (operator) {
                when(status) {
                    "Multiplication" -> multiply()
                    "Divide" -> divide()
                    "Subtraction" -> minus()
                    "Addition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "Addition"
            operator = false
            number = null
            dotControl = false


        }
        mainBinding.btnMinus.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("-")

            if (operator) {
                when(status) {
                    "Multiplication" -> multiply()
                    "Divide" -> divide()
                    "Subtraction" -> minus()
                    "Addition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "Subtraction"
            operator = false
            number = null
            dotControl = false


        }
        mainBinding.btnMultiply.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("*")

            if (operator) {
                when(status) {
                    "Multiplication" -> multiply()
                    "Divide" -> divide()
                    "Subtraction" -> minus()
                    "Addition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "Multiplication"
            operator = false
            number = null
            dotControl = false


        }
        mainBinding.btnDivide.setOnClickListener {
            history = mainBinding.textViewHistory.text.toString()
            currentResult = mainBinding.textViewResult.text.toString()
            mainBinding.textViewHistory.text = history.plus(currentResult).plus("/")

            if (operator) {
                when(status) {
                    "Multiplication" -> multiply()
                    "Divide" -> divide()
                    "Subtraction" -> minus()
                    "Addition" -> plus()
                    else -> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
                }
            }
            status = "Divide"
            operator = false
            number = null
            dotControl = false

        }

        mainBinding.toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.setting_icon -> {
                    val intent = Intent(this@MainActivity, ThemeActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting_icon) {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("switch", false)
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences = this.getSharedPreferences("Calculations", MODE_PRIVATE)
        sharedPreferences.edit {

            val resultToSave = mainBinding.textViewResult.text.toString()
            val historyToSave = mainBinding.textViewHistory.text.toString()
            val numberToSave = number
            val statusToSave = status
            val operatorToSave = operator
            val dotToSave = dotControl
            val equalToSave = buttonEqualsControl
            val firstNumberToSave = firstNumber.toString()
            val lastNumberToSave = lastNumber.toString()

            putString("result", resultToSave)
            putString("history", historyToSave)
            putString("number", numberToSave)
            putString("status", statusToSave)
            putBoolean("operator", operatorToSave)
            putBoolean("dot", dotToSave)
            putBoolean("equal", equalToSave)
            putString("firstNumber", firstNumberToSave)
            putString("lastNumber", lastNumberToSave)

        }


    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = this.getSharedPreferences("Calculations", MODE_PRIVATE)
        mainBinding.textViewResult.text = sharedPreferences.getString("result", "0")
        mainBinding.textViewHistory.text = sharedPreferences.getString("history", "0")

        number = sharedPreferences.getString("number", null)
        status = sharedPreferences.getString("status", null)
        operator = sharedPreferences.getBoolean("operator", false)
        dotControl = sharedPreferences.getBoolean("dot", true)
        buttonEqualsControl = sharedPreferences.getBoolean("equal", false)
        firstNumber = sharedPreferences.getString("first", "0.0")!!.toDouble()
        lastNumber = sharedPreferences.getString("last", "0.0")!!.toDouble()

    }

    // function for button AC

    fun onButtonAC() {
        status = null
        number = null
        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""
        firstNumber = 0.0
        lastNumber = 0.0
        dotControl = false
        buttonEqualsControl = false


    }

        // function for making button clickable
    fun onNumberClick(clickNumber : String) {
        if(number == null) {
            number = clickNumber
        }
        else if(buttonEqualsControl) {
            number = if (dotControl) {
                clickNumber
            }
            else {
                mainBinding.textViewResult.text.toString().plus(clickNumber)
            }
            firstNumber = number!!.toDouble()
            lastNumber = 0.0
            status = null
            mainBinding.textViewHistory.text = ""
        }
        else {
            number += clickNumber
        }
            // print value
        mainBinding.textViewResult.text = number
            operator = true
            buttonEqualsControl = false
    }

    // function for creating calculations
    fun plus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber += lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }
    fun minus() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber -= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber)
    }
    fun multiply() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        firstNumber *= lastNumber
        mainBinding.textViewResult.text = myFormatter.format(firstNumber) // change the format of number according to data type
    }
    fun divide() {
        lastNumber = mainBinding.textViewResult.text.toString().toDouble()
        if (lastNumber == 0.0) {
            Toast.makeText(applicationContext, "The divisor cannot zero", Toast.LENGTH_LONG).show()
        }
        else {
            firstNumber /= lastNumber
            mainBinding.textViewResult.text = myFormatter.format(firstNumber)
        }

    }
}
