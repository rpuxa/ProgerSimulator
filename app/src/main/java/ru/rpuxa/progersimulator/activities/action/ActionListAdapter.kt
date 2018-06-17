package ru.rpuxa.progersimulator.activities.action

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ru.rpuxa.progersimulator.views.ActionView

class ActionListAdapter : BaseAdapter {

    private val inflater: LayoutInflater
    val list: MutableList<AbstractAction>

    constructor(activity: Activity) : super() {
        this.inflater = activity.layoutInflater
        this.list = ArrayList()
    }

    constructor(activity: Activity, list: ArrayList<AbstractAction>) : super() {
        this.inflater = activity.layoutInflater
        this.list = list
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        if (convertView != null)
            return convertView
        return ActionView.getView(inflater, list[position])
    }

    override fun getItem(p0: Int) = list[p0]

    override fun getItemId(p0: Int) = p0.toLong()

    override fun getCount() = list.size
}