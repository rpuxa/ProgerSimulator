package ru.rpuxa.progersimulator.files

import java.util.*

fun addSign(long: Long) = if (long < 0) "-$long" else "+$long"

fun addDivides(long: Long): String {
    var newString = ""
    var i = 3 - long.toString().length % 3
    var firstTime = true
    for (c in long.toString().toCharArray()) {
        if (i != 0 && i % 3 == 0 && !firstTime)
            newString += ' '
        newString += c
        i++
        firstTime = false
    }
    return newString
}

fun addPoint(int: Int): String {
    var s = int.toString()
    if (s.length == 1)
        s += "0"
    return "${s[0]}.${s[1]}"
}

fun sqr(x: Double) = x * x

fun nextDouble(random: Random, from: Double, to: Double) = random.nextDouble() * (to - from) + from

fun Float.toPercents() = ((this * 100) - 100).toInt()