package ru.rpuxa.progersimulator.activities.contents

import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.bank.view.*
import kotlinx.android.synthetic.main.content.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.files.toPercents
import ru.rpuxa.progersimulator.gameplay.Credit
import ru.rpuxa.progersimulator.gameplay.Player
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class TakeCreditContent : Content() {

    var minSumma = 0L
    var maxSumma = 100L
    var minPercent = 2.2f
    var maxPercent = 10f
    var maxTime = 360
    var minTime = 50

    var summa = 0L
    var percent = 0.0f
    var time = 0

    override fun onCreate() {
        val moneyPerDay = Player.player.moneyPerDay() - Player.player.getCreditsToPay()
        maxSumma = 432L * moneyPerDay
        minSumma = 60L * moneyPerDay

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val p = seekBar!!.progress.toDouble() / seekBar.max
                val summaPercent = (summa - minSumma).toDouble() / (maxSumma - minSumma)
                val timePrecent = (time - minTime).toDouble() / (maxTime - minTime)
                when (seekBar.id) {
                    R.id.bar_summa_bank -> {
                        summa = ((maxSumma - minSumma) * p + minSumma).toLong()
                        if (fromUser)
                            parent.content.bar_percent_bank.progress = (100 - 100 * min(p, timePrecent)).roundToInt()
                    }
                    R.id.bar_percent_bank -> {
                        percent = ((maxPercent - minPercent) * p + minPercent).toFloat()
                        if (fromUser) {
                            if (summaPercent <= timePrecent)
                                parent.content.bar_summa_bank.progress = (100 - 100 * p).roundToInt()
                            if (timePrecent <= summaPercent)
                                parent.content.bar_time_bank.progress = (100 - 100 * p).roundToInt()
                        }
                    }
                    R.id.bar_time_bank -> {
                        time = ((maxTime - minTime) * p + minTime).toInt()
                        if (fromUser)
                            parent.content.bar_percent_bank.progress = (100 - 100 * min(p, summaPercent)).roundToInt()
                    }
                }
                parent.content.summa_bank.text = summa.toString()
                parent.content.percent_bank.text = (((percent * 100).roundToLong()) - 100).toString()
                parent.content.time_bank.text = time.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        parent.content.bar_summa_bank.setOnSeekBarChangeListener(listener)
        parent.content.bar_percent_bank.setOnSeekBarChangeListener(listener)
        parent.content.bar_time_bank.setOnSeekBarChangeListener(listener)

        parent.content.bar_summa_bank.progress = 100
        parent.content.bar_percent_bank.progress = 1
        parent.content.bar_percent_bank.progress = 0
        parent.content.bar_time_bank.progress = 100

        var accept = false

        val checkBoxListener = View.OnClickListener {
             accept = parent.content.check_box1_bank.isChecked && parent.content.check_box2_bank.isChecked
        }

        parent.content.check_box1_bank.setOnClickListener(checkBoxListener)
        parent.content.check_box2_bank.setOnClickListener(checkBoxListener)

        parent.content.accept_credit_bank.setOnClickListener {
            if (accept) {
                Player.player.credits.add(Credit(summa, percent, time))
                parent.startContent(BankMenuContent())
                parent.showMessage("Поздравляю! Кредит одобрен", false)
            } else {
                parent.showToast("Вы не согласились с условиями", true)
            }
        }

        parent.content.min_summa_bank.text = minSumma.toString()
        parent.content.mid_summa_bank.text = ((minSumma + maxSumma) / 2).toString()
        parent.content.max_summa_bank.text = maxSumma.toString()

        parent.content.min_percent_bank.text = minPercent.toPercents().toString()
        parent.content.mid_percent_bank.text = ((minPercent + maxPercent) / 2).toPercents().toString()
        parent.content.max_percent_bank.text = maxPercent.toPercents().toString()

        parent.content.min_time_bank.text = minTime.toString()
        parent.content.mid_time_bank.text = ((minTime + maxTime) / 2).toString()
        parent.content.max_time_bank.text = maxTime.toString()

    }

    override fun layout() = R.layout.bank

    override fun update() {
    }
}