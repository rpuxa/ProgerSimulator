package ru.rpuxa.progersimulator.gameplay

import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.project.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.activities.contents.BankMenuContent
import ru.rpuxa.progersimulator.cache.SuperSerializable
import ru.rpuxa.progersimulator.files.nextDouble
import ru.rpuxa.progersimulator.files.sqr
import ru.rpuxa.progersimulator.gameplay.player_fields.Money
import ru.rpuxa.progersimulator.gameplay.player_fields.Settings
import ru.rpuxa.progersimulator.gameplay.player_fields.WorkPoints
import ru.rpuxa.progersimulator.statistic.AppStatistic
import ru.rpuxa.progersimulator.views.ReviewDialog
import java.lang.Math.pow
import java.lang.Math.round
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class Player : Workable, SuperSerializable {

    companion object {
        lateinit var player: Player
        val random = Random()
    }

    var day = 0
    var level = 0.0
    var money = Money()
    var wp = WorkPoints()
    var settings = Settings()
    var config = PCConfig()
    var projects = ArrayList<Project>()
    var credits = ArrayList<Credit>()
    @Transient
    var daysWithoutAd = 0

    fun onCreate() {
        openNewMenu()
    }

    fun set(money: Money, wp: WorkPoints, config: PCConfig) {
        this.money = money
        this.wp = wp
        this.config = config
    }

    fun nextDay() {
        day++
        wp.value = wp.all
        setMoneyBar()
        var p = 100.0
        for (project in projects) {
            p += project.code.spentWP.toDouble() + if (project.design.spentWP > project.code.spentWP) project.code.spentWP else project.design.spentWP
            calculate(project)
        }

        level = 1.0 / 30 * (Math.cbrt(3 * sqrt(3.0) * sqrt(27 * sqr(p) - 2000 * p) + 27 * p - 1000) +
                100 / Math.cbrt(3 * sqrt(3.0) * sqrt(27 * sqr(p) - 2000 * p) + 27 * p - 1000) - 10)

        openNewMenu()

        for (c in credits.toTypedArray()) {
            val d = day - c.startDay
            if (d != 0 && d % 10 == 0) {
                if (c.toPay <= money.value)
                    MainActivity.listener.getInstance().showToast("Часть кредита выплачена", true)
                if (c.pay())
                    credits.remove(c)
            }
        }
        if (settings.showReview <= 0) {
            ReviewDialog(MainActivity.listener.getInstance()).createDialog()
        } else {
            settings.showReview--
        }
        AppStatistic.statistic.send()
        val content = MainActivity.listener.getInstance().contentQueue.peekFirst()
        (content as? BankMenuContent)?.update()
        if (daysWithoutAd <= 0 && MainActivity.listener.getInstance().showBanner())
            daysWithoutAd = 10 + random.nextInt(10)
        else
            daysWithoutAd--
    }

    fun getCreditsToPay(): Long {
        var a = 0L
        for (credit in credits)
            a += credit.toPay
        return a
    }

    private fun calculate(project: Project) {
        // val random = SecureRandom(project.name.toByteArray())
        val p = project.code.spentWP.toDouble() + if (project.design.spentWP > project.code.spentWP) project.code.spentWP else project.design.spentWP
        val level = 1.0 / 30 * (Math.cbrt(3 * sqrt(3.0) * sqrt(27 * sqr(p) - 2000 * p) + 27 * p - 1000) +
                100 / Math.cbrt(3 * sqrt(3.0) * sqrt(27 * sqr(p) - 2000 * p) + 27 * p - 1000) - 10)
        if (level == level && level >= 0) {
            var design = 100 * project.design.spentWP / project.code.spentWP
            if (design > 50)
                design = 50
            project.statistic.reviewsCode.evaluation = (round(sqrt(project.statistic.reviewsCode.adaptation.toDouble()) * 5) +
                    50 - 100 * project.code.bugs / project.code.spentWP).toInt() / 2
            project.statistic.reviewsDesign.evaluation = (round(sqrt(project.statistic.reviewsDesign.adaptation.toDouble()) * 5) +
                    design).toInt() / 2
            if (project.statistic.reviewsCode.evaluation < 1)
                project.statistic.reviewsCode.evaluation = 1
            if (project.statistic.reviewsDesign.evaluation < 1)
                project.statistic.reviewsDesign.evaluation = 1
            project.statistic.reviews = (0.6f * project.statistic.reviewsCode.evaluation + .4f * project.statistic.reviewsDesign.evaluation).toInt()

            val adCoefficient = project.ad.agressive.toDouble() / 10 * 1.5 + .5

            val newPeople = (pow(10.0, sqrt(level)) * getReviewCoefficient(project.statistic.reviews) - .95 * project.statistic.downloads + project.statistic.delete) * nextDouble(random, .95, 1.05) / adCoefficient
            val downloads = (if (newPeople > 0) newPeople / .8 * nextDouble(random, .8, 1.5) else (level * random.nextInt(3) + level))
            val deleted = downloads - newPeople
            project.statistic.downloads += downloads.toLong()
            project.statistic.delete += deleted.toLong()
            if (project.statistic.delete > project.statistic.downloads)
                project.statistic.delete = project.statistic.downloads
            project.moneyPerDay = ((project.statistic.downloads - project.statistic.delete) * nextDouble(random, .95, 1.05) * adCoefficient).toInt()
            player.money.add(project.moneyPerDay.toLong())
        }
        project.statistic.reviewsCode.adaptationDec()
        project.statistic.reviewsDesign.adaptationDec()
    }

    private fun openNewMenu() {
        val newLevel = level.toInt()
        val activity = MainActivity.listener.getInstance()
        if (newLevel >= 5) {
            activity.nav_view.menu.findItem(R.id.bank_menu_drawer).isEnabled = true
            activity.nav_view.menu.findItem(R.id.bank_menu_drawer).title = "Банк"
        }
        if (newLevel >= 50) {
            activity.nav_view.menu.findItem(R.id.company_menu_drawer).isEnabled = true
            activity.nav_view.menu.findItem(R.id.company_menu_drawer).title = "Компании"
        }
        val list = activity.content.review_drawer_list_project
        if (list != null && newLevel >= list.level)
            list.unlock()
    }

    private fun getReviewCoefficient(value: Int): Double {
        if (value > 40)
            return (value - 40.0) / 10 + 1
        if (value > 30)
            return (value - 30.0) / 10 * .5 + .5
        if (value > 20)
            return (value - 20.0) / 10 * .25 + .25
        return (value - 10.0) / 10 * .15 + .1
    }

    override fun getWorkPoints() = config.getWorkPoints()


    fun moneyPerDay(): Int {
        var s = 0
        for (p in projects)
            s += p.moneyPerDay
        return s
    }

}

fun setMoneyBar() = MainActivity.listener.getInstance().setMoneyBar(Player.player.money, Player.player.wp, Player.player.level)

