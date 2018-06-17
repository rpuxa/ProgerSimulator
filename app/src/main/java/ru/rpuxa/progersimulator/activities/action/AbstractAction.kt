package ru.rpuxa.progersimulator.activities.action

import android.view.View
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.files.MONEY_NEEDED
import ru.rpuxa.progersimulator.files.NONE_NEEDED
import ru.rpuxa.progersimulator.files.WP_NEEDED
import ru.rpuxa.progersimulator.gameplay.Player

abstract class AbstractAction(var name: String, var wp: Long, var money: Long, var wpAll: Long, var userData: Any?) : View.OnClickListener {

    override fun onClick(p0: View?) {
        run(Player.player)
    }

    protected open fun run(player: Player): Int {
        val activity = MainActivity.listener!!.getInstance()
        if (!player.wp.add(wp)) {
            activity.showToast("Не хватает очков работы", true)
            return WP_NEEDED
        }
        if (!player.money.add(money)) {
            activity.showToast("Не хватает средств", true)
            return MONEY_NEEDED
        }
        player.wp.addAll(wpAll)
        action(player)
        return NONE_NEEDED
    }

    abstract fun action(player: Player)
}

