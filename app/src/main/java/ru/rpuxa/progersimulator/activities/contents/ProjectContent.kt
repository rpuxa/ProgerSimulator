package ru.rpuxa.progersimulator.activities.contents

import android.widget.SeekBar
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.project.view.*
import kotlinx.android.synthetic.main.project_code.view.*
import kotlinx.android.synthetic.main.project_design.view.*
import kotlinx.android.synthetic.main.project_income.view.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.activities.action.AbstractAction
import ru.rpuxa.progersimulator.activities.action.ActionListAdapter
import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.gameplay.Project
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView

class ProjectContent(val project: Project) : Content() {

    override fun onCreate() {
        parent.content.income_drawer_list_project.bar_agressive_ad_project_income.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                project.ad.agressive = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun update() {
        parent.setProject(project)
        MaterialShowcaseView.Builder(parent).setTarget(parent.next_day).setContentText("Если закончатся очки работы, просто начните новый день").setDismissText("(нажмите здесь, чтобы продолжить)").setDelay(500).singleUse("2").show()
        var adapter = ActionListAdapter(parent)
        parent.content.drawer_list_project_code.project_code.code_list_view_project_code.adapter = adapter
        var actions = arrayOf(
                object : AbstractAction("Средний рабочий день", -Player.player.wp.all / 3, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.code.addWP(-wp)
                        parent.setProject(project)
                        project.statistic.reviewsCode.adaptationInc()
                    }
                },
                object : AbstractAction("Усердный рабочий день", -Player.player.wp.all, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.code.addWP(-wp)
                        parent.setProject(project)
                        project.statistic.reviewsCode.adaptationInc()
                    }
                }
        )
        adapter.list.addAll(actions)
        adapter.notifyDataSetChanged()

        adapter = ActionListAdapter(parent)
        parent.content.drawer_list_project_code.project_code.bugs_list_view_project_code.adapter = adapter
        actions = arrayOf(
                object : AbstractAction("Исправить несколько багов", -Player.player.wp.all / 3, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.code.addBugs(wp)
                        parent.setProject(project)
                        project.statistic.reviewsCode.adaptationInc()
                    }
                },
                object : AbstractAction("Усердное исправление багов", -Player.player.wp.all, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.code.addBugs(wp)
                        parent.setProject(project)
                        project.statistic.reviewsCode.adaptationInc()
                    }
                }
        )
        adapter.list.addAll(actions)
        adapter.notifyDataSetChanged()

        adapter = ActionListAdapter(parent)
        parent.content.design_list_view_project_code.adapter = adapter
        actions = arrayOf(
                object : AbstractAction("Сделать пару иконок", -Player.player.wp.all / 10, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.design.addWP(-wp)
                        parent.setProject(project)
                        project.statistic.reviewsDesign.adaptationInc()
                    }
                },
                object : AbstractAction("Улучшить дизайн", -Player.player.wp.all / 3, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.design.addWP(-wp)
                        parent.setProject(project)
                        project.statistic.reviewsDesign.adaptationInc()
                    }
                },
                object : AbstractAction("Усердная работа над дизайном", -Player.player.wp.all, 0, 0, project) {
                    override fun action(player: Player) {
                        val project = userData as Project
                        project.design.addWP(-wp)
                        parent.setProject(project)
                        project.statistic.reviewsDesign.adaptationInc()
                    }
                }
        )
        adapter.list.addAll(actions)
        adapter.notifyDataSetChanged()
    }

    override fun layout() = R.layout.project
}