package ru.rpuxa.progersimulator.gameplay.player_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.gameplay.setMoneyBar

class Money(var value: Long = 500) : SuperSerializable {

    fun add(m: Long): Boolean {
        if (value + m < 0)
            return false
        value += m
        setMoneyBar()
        return true
    }

}
