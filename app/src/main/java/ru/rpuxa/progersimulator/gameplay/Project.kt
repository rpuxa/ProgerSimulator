package ru.rpuxa.progersimulator.gameplay

import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.gameplay.project_fields.Ad
import ru.rpuxa.progersimulator.gameplay.project_fields.Code
import ru.rpuxa.progersimulator.gameplay.project_fields.Design
import ru.rpuxa.progersimulator.gameplay.project_fields.Statistic

class Project : SuperSerializable {
    val name: String
    val stage: Int
    var moneyPerDay: Int
    val statistic: Statistic
    val code: Code
    val design: Design
    val ad: Ad

    constructor() {
        name = "Name"
        stage = 0
        moneyPerDay = 0
        code = Code()
        design = Design()
        statistic = Statistic()
        ad = Ad()
    }

    constructor(name: String) {
        this.name = name
        stage = 0
        moneyPerDay = 0
        code = Code()
        design = Design()
        statistic = Statistic()
        ad = Ad()
    }

    companion object {
        private const val serialVersionUID = -800127121275466770L
    }
}