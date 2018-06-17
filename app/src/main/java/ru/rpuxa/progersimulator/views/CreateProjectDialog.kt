package ru.rpuxa.progersimulator.views

import android.app.Dialog
import kotlinx.android.synthetic.main.create_project_dialog.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.activities.contents.ProjectContent
import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.gameplay.Project

class CreateProjectDialog(val parent: MainActivity) : Dialog(parent) {

    fun createDialog() {
        setContentView(R.layout.create_project_dialog)
        yes_create_project_dialog.setOnClickListener {
            val name = name_create_project_dialog.text.toString()
            if (name.length < 4) {
                parent.showMessage("Слишком короткое название", false)
                return@setOnClickListener
            }
            if (!Player.player.money.add(-25)) {
                parent.showMessage("Нехватает средств!", false)
                return@setOnClickListener
            }
            val project = Project(name)
            Player.player.projects.add(project)
            dismiss()
            parent.startContent(ProjectContent(project))
        }
        no_create_project_dialog.setOnClickListener {
            dismiss()
        }
        setTitle("Внимание!")
        show()
    }

}