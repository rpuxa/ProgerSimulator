package ru.rpuxa.progersimulator.activities.contents

import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.pc_config.view.*
import ru.rpuxa.progersimulator.R
import ru.rpuxa.progersimulator.activities.Content
import ru.rpuxa.progersimulator.activities.action.BuyAction
import ru.rpuxa.progersimulator.files.*
import ru.rpuxa.progersimulator.gameplay.Components
import ru.rpuxa.progersimulator.gameplay.PCConfig
import ru.rpuxa.progersimulator.gameplay.Player

class PCConfigContent(var config: PCConfig) : Content() {

    override fun onCreate() {
        parent.content.buy_motherboard_pc_config.setAction(
                object : BuyAction(MOTHERBOARD, Player.player.config.motherBoard, parent.content.buy_motherboard_pc_config, parent) {
                    override fun updateViews() {
                        update()
                    }
                }, parent
        )

        parent.content.buy_video_card_pc_config.setAction(
                object : BuyAction(VIDEO_CARD, Player.player.config.videoCard, parent.content.buy_video_card_pc_config, parent) {
                    override fun updateViews() {
                        update()
                    }
                }, parent
        )

        parent.content.buy_ram_pc_config.setAction(
                object : BuyAction(RAM, Player.player.config.ram, parent.content.buy_ram_pc_config, parent) {
                    override fun updateViews() {
                        update()
                    }
                }, parent
        )

        parent.content.buy_processor_pc_config.setAction(
                object : BuyAction(PROCESSOR, Player.player.config.processor, parent.content.buy_processor_pc_config, parent) {
                    override fun updateViews() {
                        update()
                    }
                }, parent
        )
    }

    override fun layout() = R.layout.pc_config

    override fun update() {
        val s =  "${Components.components[MOTHERBOARD]!![config.motherBoard]!!.name} ${if (config.motherBoard != 0) "(${config.motherBoard} поколение)" else ""}"
        parent.content.name_motherboard_pc_config.text = s
        parent.content.wp_motherboard_pc_config.text = Components.components[MOTHERBOARD]!![config.motherBoard]!!.wp.toString()

        val s1 = "${Components.components[VIDEO_CARD]!![config.videoCard]!!.name} ${if (config.videoCard != 0) "(${config.videoCard} поколение)" else ""}"
        parent.content.name_video_card_pc_config.text = s1
        parent.content.wp_video_card_pc_config.text = Components.components[VIDEO_CARD]!![config.videoCard]!!.wp.toString()

        val s2 = "${Components.components[RAM]!![config.ram]!!.name} ${if (config.ram != 0) "(${config.ram} поколение)" else ""}"
        parent.content.name_ram_pc_config.text = s2
        parent.content.wp_ram_pc_config.text = Components.components[VIDEO_CARD]!![config.videoCard]!!.wp.toString()

        val s3 = "${Components.components[PROCESSOR]!![config.processor]!!.name} ${if (config.processor != 0) "(${config.processor} поколение)" else ""}"
        parent.content.name_processor_pc_config.text = s3
        parent.content.wp_processor_pc_config.text = Components.components[PROCESSOR]!![config.processor]!!.wp.toString()

        parent.content.wp_pc_config.text = addDivides(config.getWorkPoints())
    }

}