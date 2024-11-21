package com.jasminsoftware.tippy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

const val TAG = "MainActivity"
const val INITIAL_PERCENTAGE = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBase: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipCalculated: TextView
    private lateinit var tvTotalCalculated: TextView
    private lateinit var tvTipPergentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etBase = findViewById(R.id.etBase)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipCalculated = findViewById(R.id.tvTipCalculated)
        tvTotalCalculated = findViewById(R.id.tvTotalCalculated)
        tvTipPergentage = findViewById(R.id.tvTipPergentage)

        tvTipPergentage.text = "$INITIAL_PERCENTAGE%"
        seekBarTip.progress = INITIAL_PERCENTAGE

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                tvTipPergentage.text = "$progress%"
                calculateTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                Log.i(TAG, "amount entered: ${s.toString()}")
                calculateTipAndTotal()
                // TODO: only allow 2 decimal digits
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun calculateTipAndTotal() {
        if (etBase.text.isNotEmpty()) {
            val baseAmount = etBase.text.toString().toDouble()
            val tipPercent = seekBarTip.progress

            val tipAmount = baseAmount * tipPercent / 100
            val totalAmount = baseAmount + tipAmount

            // TODO: round to 2 decimal place
            tvTipCalculated.text = tipAmount.toString()
            tvTotalCalculated.text = totalAmount.toString()
        }

    }
}