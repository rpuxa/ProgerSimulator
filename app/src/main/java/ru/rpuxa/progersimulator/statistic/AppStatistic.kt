package ru.rpuxa.progersimulator.statistic

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.cache.STATISTIC
import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.gameplay.Player

class AppStatistic : SuperSerializable {
    var daysPlayed = 0
    var level = 0.0
    var money = 0L
    var moneyPerDay = 0
    var creditsCount = 0
    var creditsToPay = 0L
    var projectsCount = 0
    var wpAll = 0L

    constructor(player: Player) {
        update(player)
    }

    fun update(player: Player) {
        daysPlayed = player.day
        level = player.level
        money = player.money.value
        moneyPerDay = player.moneyPerDay()
        creditsCount = player.credits.size
        creditsToPay = player.getCreditsToPay()
        projectsCount = player.projects.size
        wpAll = player.getWorkPoints()
    }

    fun save() {
        ru.rpuxa.progersimulator.cache.save(serializable(), MainActivity.listener!!.getInstance().filesDir, STATISTIC)
    }

    fun send() {
        val bundle = Bundle()
        bundle.putInt("daysPlayed", daysPlayed)
        bundle.putDouble("level", level)
        bundle.putLong("money", money)
        bundle.putInt("moneyPerDay", moneyPerDay)
        bundle.putInt("creditsCount", creditsCount)
        bundle.putLong("creditsToPay", creditsToPay)
        bundle.putInt("projectsCount", projectsCount)
        bundle.putLong("wpAll", wpAll)
        val activity = MainActivity.listener!!.getInstance()
        activity.runOnUiThread {
            if (activity.checkNetworkConnection())
                FirebaseAnalytics.getInstance(activity).logEvent("app_statistic", bundle)
        }
    }

    companion object {
         lateinit var statistic: AppStatistic
    }
}