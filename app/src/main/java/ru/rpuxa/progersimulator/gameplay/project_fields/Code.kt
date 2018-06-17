package ru.rpuxa.progersimulator.gameplay.project_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.files.nextDouble
import java.util.*

class Code(var spentWP: Long = 0, var bugs: Long = 0) : SuperSerializable {

    fun addWP(points: Long) {
        spentWP += points
        bugs += (points * nextDouble(Random(), .5, .4)).toLong()
    }

    fun addBugs(points: Long) {
        bugs += points
        if (bugs < 0)
            bugs = 0
    }
}