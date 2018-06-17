package ru.rpuxa.progersimulator.views

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RatingBar
import kotlinx.android.synthetic.main.review_dialog.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.MainActivity
import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.statistic.AppStatistic

class ReviewDialog(val parent: MainActivity) : Dialog(parent) {

    fun createDialog() {
        setContentView(R.layout.review_dialog)
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, _ ->
            AppStatistic.statistic.rating = rating.toInt()
            if (rating >= 4) {
                dismiss()
                parent.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(parent.getString(R.string.google_play_link))))
            } else {
                ratingBar.visibility = View.GONE
                hide_buttons_review_dialog.visibility = View.GONE
                review_review_dialog.visibility = View.VISIBLE
            }
            Player.player.settings.showReview = Int.MAX_VALUE
        }
        send_review_dialog.setOnClickListener {
            AppStatistic.statistic.review = edittext_review_review_dialog.text.toString()
            dismiss()
        }
        show_later_review_dialog.setOnClickListener {
            Player.player.settings.showReview = 20
            dismiss()
        }
        dont_show_review_dialog.setOnClickListener {
            Player.player.settings.showReview = Int.MAX_VALUE
            dismiss()
        }
        setTitle("Внимание!")
        show()
    }
}