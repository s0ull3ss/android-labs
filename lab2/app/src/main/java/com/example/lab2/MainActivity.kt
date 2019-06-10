package com.example.lab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val op = listOf<Button>(addition, subtraction, multiplication, division)

        for (el in op) {
            el.setOnClickListener { v ->
                if (num1.text.isEmpty())
                    Snackbar.make(v, "Заполни поле num1", Snackbar.LENGTH_LONG).show()
                else if (num2.text.isEmpty())
                    Snackbar.make(v, "Заполни поле num2", Snackbar.LENGTH_LONG).show()
                else {
                    try {
                        val n1 = num1.text.toString().toDouble()
                        val n2 = num2.text.toString().toDouble()

                        when (el) {
                            addition -> result.text = (n1 + n2).toString()
                            subtraction -> result.text = (n1 - n2).toString()
                            multiplication -> result.text = (n1 * n2).toString()
                            division -> if (n2 != 0.0) {
                                result.text = (n1 / n2).toString()
                            } else {
                                result.text = "inf"
                            }
                        }
                    }
                    catch (e: Exception){
                        Snackbar.make(v, "Введены слишком большие числа", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
