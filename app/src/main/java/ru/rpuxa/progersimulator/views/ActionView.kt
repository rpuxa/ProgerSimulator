package ru.rpuxa.progersimulator.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.action.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.activities.action.AbstractAction
import ru.rpuxa.progersimulator.files.addSign

class ActionView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object {

        fun getView(inflater: LayoutInflater, action: AbstractAction): View {
            val view = inflater.inflate(R.layout.action, null, false)
            view.name_action.text = action.name
            if (action.wp == 0L && action.wpAll == 0L)
                view.wp_layout_action.visibility = View.GONE
            else
                view.wp_action.text = addSign(if (action.wp != 0L) action.wp else action.wpAll)
            if (action.money == 0L)
                view.money_layout_action.visibility = View.GONE
            else
                view.money_action.text = addSign(action.money)
            view.setOnClickListener(action)

            return view
        }
    }

    fun setAction(action: AbstractAction, activity: MainActivity) {
        val view = getView(activity.layoutInflater, action)
        removeAllViews()
        addView(view)
    }
}