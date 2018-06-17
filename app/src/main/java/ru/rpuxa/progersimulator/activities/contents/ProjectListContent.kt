package ru.rpuxa.progersimulator.activities.contents

import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.project_list.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.activities.ProjectListAdapter
import ru.rpuxa.progersimulator.views.CreateProjectDialog

class ProjectListContent : Content() {

    override fun onCreate() {
        parent.content.create_project_list.setOnClickListener {
            CreateProjectDialog(parent).createDialog()
        }

    }

    override fun layout() = R.layout.project_list

    override fun update() {
        parent.content.project_list.adapter = ProjectListAdapter(parent)
    }
}