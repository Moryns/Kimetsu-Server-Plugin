package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.random.Random

class PlayerJoinEvent : Listener {
    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.hasPlayedBefore()) return
        DataBase.set("${player.name}.rank", "0")
        val stats = statGeneration()
        DataBase.set("${player.name}.strength", stats[0])
        DataBase.set("${player.name}.speed", stats[1])
        DataBase.set("${player.name}.mastery", stats[2])
        DataBase.set("${player.name}.dexterity", stats[3])
        DataBase.set("${player.name}.vitality", stats[4])
    }

    fun statGeneration(): Array<Int> {
        var strength: Int
        var speed: Int
        var mastery: Int
        var dexterity: Int
        var vitality: Int
        var allStats: Int

        do {
            speed = Random.nextInt(1, 12)
            strength = Random.nextInt(1, 12)
            dexterity = Random.nextInt(1, 12)
            vitality = Random.nextInt(1, 12)
            mastery = Random.nextInt(1, 12)
            allStats = speed + strength + mastery + vitality + dexterity
        } while (allStats < 35 || allStats > 45)

        // Возвращаем массив значений статов
        return arrayOf(strength, speed, mastery, dexterity, vitality)
    }
}