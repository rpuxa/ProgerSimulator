package ru.rpuxa.progersimulator.activities.contents

import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.author_content.view.*
import kotlinx.android.synthetic.main.content.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.statistic.AppStatistic

class AuthorContent : Content() {
    override fun layout() = R.layout.author_content

    override fun update() {
        parent.content.vk_author_content.setOnClickListener {
            parent.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/grishayurkov")))
        }
    }

    override fun onCreate() {
        AppStatistic.statistic.clickToAuthorContent++
    }

}