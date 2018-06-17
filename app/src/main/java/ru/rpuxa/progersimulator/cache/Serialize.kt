package ru.rpuxa.progersimulator.cache

import ru.rpuxa.progersimulator.gameplay.Player
import ru.rpuxa.progersimulator.statistic.AppStatistic
import java.io.*

const val PLAYER = "player"
const val STATISTIC = "statistic"

fun saveAll(file: File) {
    val player = Player.player
    save(player.serializable(), file, PLAYER)
    AppStatistic.statistic.save()
}

fun deserializePlayer(file: File): Player {
    val p = SuperDeserializator.deserialize(file, PLAYER)
    if (p != null)
        return p as Player
    return Player()
}

fun deserializeStatistic(file: File): AppStatistic {
    val s = SuperDeserializator.deserialize(file, STATISTIC)
    if (s != null)
        return s as AppStatistic
    return AppStatistic(Player.player)
}

fun save(serializable: Any, file: File, name: String) {
    try {
        ObjectOutputStream(FileOutputStream(File(file, name))).use { out ->
            out.writeObject(serializable)
            out.flush()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun load(file: File, name: String): Any? {
    try {
        ObjectInputStream(FileInputStream(File(file, name))).use { out ->
            return out.readObject()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}