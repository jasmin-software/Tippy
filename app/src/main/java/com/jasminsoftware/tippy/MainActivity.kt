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
    private lateinit var tvTipPercentage: TextView
    private lateinit var tvTipLabel: TextView

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
        tvTipPercentage = findViewById(R.id.tvTipPergentage)
        tvTipLabel = findViewById(R.id.tvTipLabel)

        tvTipPercentage.text = "$INITIAL_PERCENTAGE%"
        seekBarTip.progress = INITIAL_PERCENTAGE
        updateTipLabel(INITIAL_PERCENTAGE)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                tvTipPercentage.text = "$progress%"
                updateTipLabel(progress)
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
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.contains(".")) {
                    val parts = text.split(".")
                    if (parts.size == 2 && parts[1].length > 2) {
                        // Limit to 2 decimal places
                        etBase.setText(parts[0] + "." + parts[1].take(2))
                        etBase.setSelection(etBase.text.length)
                    }
                }
            }
        })
    }

    private fun calculateTipAndTotal() {
        if (etBase.text.isEmpty()) {
            tvTipCalculated.text = ""
            tvTotalCalculated.text = ""
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        tvTipCalculated.text = "%.2f".format(tipAmount)
        tvTotalCalculated.text = "%.2f".format(totalAmount)
    }

    private fun updateTipLabel(int: Int) {
        val tipLabel = when (int) {
            in 0..9 -> "Poor"
            in 10.. 14 -> "Ok"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipLabel.text = tipLabel
        // TODO: update text color according to label
    }
}