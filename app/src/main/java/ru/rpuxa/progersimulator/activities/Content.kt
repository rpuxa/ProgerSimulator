package ru.rpuxa.progersimulator.activities

abstract class Content {

    lateinit var parent: MainActivity

    init {

    }

    abstract fun onCreate()

    abstract fun layout(): Int

    abstract fun update()
}