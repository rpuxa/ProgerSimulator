package ru.rpuxa.progersimulator.gameplay

import ru.rpuxa.progersimulator.cache.SuperSerializable
import java.lang.Math.pow
import kotlin.math.ceil

class Credit(summa: Long, percent: Float, time: Int) : SuperSerializable {
    val toPay: Long
    val days: Int = ceil(time.toFloat() / 10).toInt()
    var passed: Int
    val startDay: Int

    fun pay(): Boolean {
        val m = Player.player.money.value
        if (toPay > m)
            return false
        Player.player.money.add(-toPay)
        passed++
        return passed >= days
    }

    init {
        toPay = ceil(summa * pow(percent.toDouble(), time.toDouble() / 360) / days).toLong()
        Player.player.money.add(summa)
        passed = 0
        startDay = Player.player.day
    }

    companion object {
        private const val serialVersionUID = -80012092363443710L
    }
}