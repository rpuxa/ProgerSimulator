package ru.rpuxa.progersimulator.activities.contents

import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.menu_bank.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.gameplay.Player

class BankMenuContent : Content() {

    override fun onCreate() {
        parent.content.take_new_credit_menu_bank.setOnClickListener {
            if (Player.player.moneyPerDay() - Player.player.getCreditsToPay() > 20)
                parent.startContent(TakeCreditContent())
            else
                parent.showToast("В кредите отказано. Слишком маленькие доходы", true)
        }
    }

    override fun layout() = R.layout.menu_bank

    override fun update() {
        val player = Player.player
        var all = 0L
        var toPay = 0L
        var days = 0

        for (credit in player.credits) {
            all += credit.toPay * (credit.days - credit.passed)
            toPay += credit.toPay
            val i = (credit.days - credit.passed) * 10
            if (days < i)
                days = i
        }

        parent.content.all_summa_menu_bank.text = all.toString()
        parent.content.to_pay_menu_bank.text = toPay.toString()
        parent.content.days_menu_bank.text = days.toString()
        parent.content.count_credits_menu_bank.text = player.credits.size.toString()
    }
}