package ru.rpuxa.progersimulator.gameplay.project_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable

class Design(var spentWP: Long = 0) : SuperSerializable {

    fun addWP(points: Long) {
        spentWP += points
    }
}