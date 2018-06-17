package ru.rpuxa.progersimulator.activities.action

import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.files.*
import ru.rpuxa.progersimulator.gameplay.Components.components
import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.views.ActionView

abstract class BuyAction(val type: Int, var position: Int, val view: ActionView, private val activity: MainActivity) :
        AbstractAction(
                if (position + 1 < components[type]!!.size) "${components[type]!![position + 1]!!.name} (${position + 1} поколение)" else "Недоступно",
                0,
                if (position + 1 < components[type]!!.size) -components[type]!![position + 1]!!.money else 0,
                if (position + 1 < components[type]!!.size) components[type]!![position + 1]!!.wp else 0,
                null
        ) {

    override fun run(player: Player): Int {
        val activity = MainActivity.listener!!.getInstance()
        if (position + 1 >= components[type]!!.size)
            return NONE_NEEDED
        if (!player.wp.add(wp)) {
            activity.showToast("Не хватает очков работы", true)
            return WP_NEEDED
        }
        if (!player.money.add(money)) {
            activity.showToast("Не хватает средств", true)
            return MONEY_NEEDED
        }
        val wp = wpAll - components[type]!![position]!!.wp
        player.wp.addAll(wp)
        player.wp.add(wp)
        action(player)
        return NONE_NEEDED
    }

    override fun action(player: Player) {
        if (position + 1 < components[type]!!.size) {
            position++
            when (type) {
                MOTHERBOARD -> player.config.motherBoard = position
                VIDEO_CARD -> player.config.videoCard = position
                RAM -> player.config.ram = position
                PROCESSOR -> player.config.processor = position
            }
            if (position + 1 < components[type]!!.size) {
                name = "${components[type]!![position + 1]!!.name}(${position + 1} поколение)"
                money = -components[type]!![position + 1]!!.money
                wpAll = components[type]!![position + 1]!!.wp
            } else {
                name = "Недоступно"
                money = 0
                wp = 0
                wpAll = 0
            }
            view.setAction(this, activity)
            updateViews()
        }
    }

    abstract fun updateViews()
}