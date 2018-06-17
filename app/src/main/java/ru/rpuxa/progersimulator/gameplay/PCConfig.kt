package ru.rpuxa.progersimulator.gameplay

import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.files.MOTHERBOARD
import ru.rpuxa.progersimulator.files.PROCESSOR
import ru.rpuxa.progersimulator.files.RAM
import ru.rpuxa.progersimulator.files.VIDEO_CARD
import ru.rpuxa.progersimulator.gameplay.Components.components
import java.lang.Math.pow

class PCConfig(var motherBoard: Int, var videoCard: Int, var ram: Int, var processor: Int) : Workable, SuperSerializable {

    constructor() : this(1, 1, 1, 1)

    override fun getWorkPoints(): Long {
        return components[MOTHERBOARD]!![motherBoard]!!.getWorkPoints() + components[VIDEO_CARD]!![videoCard]!!.getWorkPoints() +
                components[RAM]!![ram]!!.getWorkPoints() + components[PROCESSOR]!![processor]!!.getWorkPoints()
    }

}

class Component(val type: Int, val id: Int, val wp: Long, val money: Long, val name: String) : Workable {
    override fun getWorkPoints() = wp
}

object Components {
    val components = Array<Array<Component?>?>(4, { null })

    init {
        val motherboards = arrayOf(
                "Отсутствует",
                "MSI H81M-E33",
                "ASUS M5A78L-M",
                "ASUS H110M-R",
                "ASUS H81I-PLUS",
                "ASUS B85M-E",
                "MSI Z270",
                "ASUS MAXIMUS VIII HERO",
                "ASUS SABERTOOTH 990FX",
                "GIGABYTE GA-990X"
        )
        val videoCards = arrayOf(
                "Отсутствует",
                "ASUS GeForce 210",
                "ASUS GeForce GT 610",
                "ASUS Radeon R5 230",
                "Radeon RX 550",
                "GeForce GTX 1050",
                "Radeon RX 560 OC",
                "GeForce GTX 1060",
                "MSI GeForce GTX 1070",
                "AORUS GeForce GTX 1080 TI"
        )
        val rams = arrayOf(
                "Отсутствует",
                "Kingston KVR16",
                "Hynix DDR2 1Gb",
                "Patriot Memory PSD3 1Gb",
                "HyperX HX318",
                "Hynix DDR3 4Gb",
                "HyperX HX410",
                "Corsair CMSX8G 8Gb",
                "Crucial CT5 8Gb",
                "HyperX HX424C15F*2/8"
                )
        val processors = arrayOf(
                "Отсутствует",
                "Intel Celeron Kaby Lake",
                "Intel Pentium Skylake",
                "AMD Athlon X4 Kaveri",
                "AMD A6 Bristol Ridge",
                "Intel Pentium G4560",
                "Intel Core i3-8100",
                "AMD Ryzen 3 Raven Ridge",
                "Intel Core i3 Kaby Lake",
                "AMD Ryzen 5"
                )
        val percent = arrayOf(.4, .3, .1, .2)

        val array = arrayOf(motherboards, videoCards, rams, processors)

        for (type in MOTHERBOARD..PROCESSOR) {
            if (components[type] == null)
                components[type] = Array(array[type].size, { null })
            for ((level, name) in array[type].withIndex()) {
                val wp = 50 * level * (level + 1)
                val money = pow(10.0, Math.sqrt(level.toDouble())) * 10 * level
                components[type]!![level] = Component(type, level, (wp * percent[type]).toLong(), (money * percent[type]).toLong(), name)
            }
        }
    }
}