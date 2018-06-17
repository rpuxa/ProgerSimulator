package ru.rpuxa.progersimulator.gameplay.player_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.gameplay.setMoneyBar

class WorkPoints(var value: Long = 0, var all: Long = 100) : SuperSerializable {

    fun add(m: Long): Boolean {
        if (value + m < 0)
            return false
        value += m
        setMoneyBar()
        return true
    }

    fun addAll(m: Long) {
        all += m
        setMoneyBar()
    }

}