package ru.rpuxa.progersimulator.gameplay.project_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable

class Reviews(var evaluation: Int = 0, var adaptation: Int = 100) : SuperSerializable {

    fun adaptationDec() {
        if (adaptation > 0)
            adaptation--
    }

    fun adaptationInc() {
        adaptation += 10
        if (adaptation > 100)
            adaptation = 100
    }
}
