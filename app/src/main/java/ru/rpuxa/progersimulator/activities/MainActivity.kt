package ru.rpuxa.progersimulator.activities

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.circle_progress_bar.view.*
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.project.view.*
import kotlinx.android.synthetic.main.project_code.view.*
import kotlinx.android.synthetic.main.project_design.view.*
import kotlinx.android.synthetic.main.project_list.view.*
import kotlinx.android.synthetic.main.project_review.view.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.contents.*
import ru.rpuxa.progersimulator.cache.deserializePlayer
import ru.rpuxa.progersimulator.cache.deserializeStatistic
import ru.rpuxa.progersimulator.cache.saveAll
import ru.rpuxa.progersimulator.files.addDivides
import ru.rpuxa.progersimulator.files.addPoint
import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.gameplay.Project
import ru.rpuxa.progersimulator.gameplay.player_fields.Money
import ru.rpuxa.progersimulator.gameplay.player_fields.WorkPoints
import ru.rpuxa.progersimulator.statistic.AppStatistic
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainActivityListener {

    companion object {
        lateinit var listener: MainActivityListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this
        setContentView(R.layout.main)
        Player.player = deserializePlayer(filesDir)
        Player.player.onCreate()
        AppStatistic.statistic = deserializeStatistic(filesDir)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        startContent(ProjectListContent())
        var cooldown = 0L
        next_day.setOnClickListener {
            if (System.currentTimeMillis() - cooldown > 1500) {
                Player.player.nextDay()
                showMessage("День №${Player.player.day}", false)
                if (contentQueue.peekFirst() is ProjectContent)
                    setProject((contentQueue.peekFirst() as ProjectContent).project)
                else if (contentQueue.peekFirst() is ProjectListContent)
                    contentQueue.peekFirst().update()
                saveAll(filesDir)
                cooldown = System.currentTimeMillis()
            }
        }
        setMoneyBar(Player.player.money, Player.player.wp, Player.player.level)
        MaterialShowcaseView.Builder(this).setTarget(content.create_project_list).setContentText("Для начала создайте новый проект").setDismissText("(нажмите здесь, чтобы продолжить)").setDelay(500).singleUse("1").show()
        loadAd()
    }

    private lateinit var mainBanner: InterstitialAd

    private fun loadAd() {
        MobileAds.initialize(this, getString(R.string.app_id))
        mainBanner = InterstitialAd(this)
        mainBanner.adUnitId = getString(R.string.main_banner)
        mainBanner.loadAd(AdRequest.Builder().build())
        mainBanner.setRewardedVideoAdListener(object: RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                mainBanner.loadAd(AdRequest.Builder().build())
            }

            override fun onRewardedVideoAdLeftApplication() {
            }

            override fun onRewardedVideoAdLoaded() {
            }

            override fun onRewardedVideoAdOpened() {
            }

            override fun onRewardedVideoCompleted() {
            }

            override fun onRewarded(p0: RewardItem?) {
            }

            override fun onRewardedVideoStarted() {
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
            }
        })
    }

    fun showBanner() : Boolean {
        val b = mainBanner.isLoaded
        if (b) {
            AppStatistic.statistic.successfullyAdLoaded++
            mainBanner.show()
        }
        else {
            AppStatistic.statistic.unsuccessfullyAdLoaded++
            mainBanner.loadAd(AdRequest.Builder().build())
        }
        return b
    }

    fun checkNetworkConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            contentQueue.pollFirst()
            val content = contentQueue.pollFirst()
            if (content != null)
                startContent(content)
        }
    }

    override fun onPause() {
        saveAll(filesDir)
        super.onPause()
    }

    override fun onDestroy() {
        saveAll(filesDir)
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //Drawer меню
        when (item.itemId) {
            R.id.project_list_menu -> {
                startContent(ProjectListContent())
            }
            R.id.pc_config_menu -> {
                startContent(PCConfigContent(Player.player.config))
            }
            R.id.author_menu_item -> {
                startContent(AuthorContent())
            }
            R.id.bank_menu_drawer -> {
                startContent(BankMenuContent())
            }
            R.id.exit_menu_item -> {
                finish()
                moveTaskToBack(true)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    var contentQueue = ArrayDeque<Content>()

    fun startContent(content: Content) {
        if (!contentQueue.isEmpty() && contentQueue.peekFirst().javaClass.name.equals(content.javaClass.name, false))
            return
        content.parent = this
        contentQueue.addFirst(content)
        val layout = findViewById<LinearLayout>(R.id.content)
        layout.removeAllViews()
        val mainGroup = layoutInflater.inflate(content.layout(), layout, false) as ViewGroup
        layout.addView(mainGroup)
        content.update()
        content.onCreate()
    }

    fun setMoneyBar(money: Money, wp: WorkPoints, level: Double) {
        money_money_bar.text = addDivides(money.value)
        val wpSt = "${addDivides(wp.value)}/${addDivides(wp.all)}"
        wp_money_bar.text = wpSt
        val s = "Уровень ${level.toInt()}"
        level_toolbar.text = s
        progress_bar_toolbar.progress = ((level - level.toInt().toDouble()) * 100).toInt()
    }


    @SuppressLint("SetTextI18n")
    fun setProject(project: Project) {
        content.name_project.text = project.name
        content.downloads_project.text = addDivides(project.statistic.downloads)
        content.delete_project.text = addDivides(project.statistic.delete)
        content.drawer_list_project_code.project_code.wp_project_code.text = addDivides(project.code.spentWP)
        if (project.code.spentWP != 0L) {
            val progress = project.code.bugs * 100 / project.code.spentWP
            content.drawer_list_project_code.project_code.progress_bar_project_code.text_circle_progress_bar.text = "$progress%"
            content.project_code.progress_bar_project_code.progress_bar_circle_progress_bar.progress = progress.toInt()
        } else {
            content.drawer_list_project_code.project_code.progress_bar_project_code.text_circle_progress_bar.text = "0%"
            content.drawer_list_project_code.project_code.progress_bar_project_code.progress_bar_circle_progress_bar.progress = 0
        }
        content.income_project.text = project.moneyPerDay.toString()
        content.project_design.wp_project_design.text = project.design.spentWP.toString()

        content.project_review.all_project_review.text_circle_progress_bar.text = addPoint(project.statistic.reviews)
        content.project_review.all_project_review.progress_bar_circle_progress_bar.progress = project.statistic.reviews * 2
        content.project_review.code_project_review.text_circle_progress_bar.text = addPoint(project.statistic.reviewsCode.evaluation)
        content.project_review.code_project_review.progress_bar_circle_progress_bar.progress = project.statistic.reviewsCode.evaluation * 2
        content.project_review.design_project_review.text_circle_progress_bar.text = addPoint(project.statistic.reviewsDesign.evaluation)
        content.project_review.design_project_review.progress_bar_circle_progress_bar.progress = project.statistic.reviewsDesign.evaluation * 2

        content.drawer_list_project_code.setText("Код")
        content.drawer_list_project_code.setTextColor(R.color.code)
        content.drawer_list_project_code.setIcon(R.drawable.code)
        content.design_drawer_list_project.setText("Дизайн")
        content.design_drawer_list_project.setIcon(R.drawable.design)
        content.design_drawer_list_project.setTextColor(R.color.design)
        content.review_drawer_list_project.setText("Оценка")
        content.review_drawer_list_project.setIcon(R.drawable.review)
        content.review_drawer_list_project.setTextColor(R.color.review)
        content.income_drawer_list_project.setText("Монетизация")
        content.income_drawer_list_project.setIcon(R.drawable.euro)
        content.income_drawer_list_project.setTextColor(R.color.euro)
        content.income_drawer_list_project.setLevel(4)
    }

    fun showMessage(string: String, isShort: Boolean) {
        Snackbar.make(content, string, if (isShort) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    private var toast: Toast? = null
    fun showToast(string: String, isShort: Boolean) {
        if (toast != null)
            toast!!.cancel()
        toast = Toast.makeText(this, string, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
        toast!!.show()
    }

    override fun getInstance() = this
}

interface MainActivityListener {
    fun getInstance() : MainActivity
}
