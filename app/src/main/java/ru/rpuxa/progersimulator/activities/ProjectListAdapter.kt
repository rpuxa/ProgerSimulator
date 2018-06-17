package ru.rpuxa.progersimulator.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.project_item.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.contents.ProjectContent
import ru.rpuxa.progersimulator.files.addDivides
import ru.rpuxa.progersimulator.gameplay.Player

class ProjectListAdapter(private val activity: MainActivity) : BaseAdapter() {

    private val inflater: LayoutInflater = activity.layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.project_item, parent, false) ?: return null
        val project = Player.player.projects[position]
        view.name_project_item.text = project.name
        view.downloads_project_item.text = addDivides(project.statistic.downloads)
        view.income_project_item.text = addDivides(project.moneyPerDay.toLong())
        view.setOnClickListener {
            activity.startContent(ProjectContent(project))
        }

        return view
    }

    override fun getItem(p0: Int) = Player.player.projects[p0]

    override fun getItemId(p0: Int) = p0.toLong()

    override fun getCount() = Player.player.projects.size

}