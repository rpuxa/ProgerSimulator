package ru.rpuxa.progersimulator.activities.action

import ru.rpuxa.progersimulator.gameplay.Player

class Action(name: String, wp: Long, money: Long, wpAll: Long, userData: Any?) : AbstractAction(name, wp, money, wpAll, userData) {

    override fun action(player: Player) {
    }
}