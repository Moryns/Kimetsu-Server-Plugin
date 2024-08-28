package org.example.kimetsuservercore.kimetsuserverplugin

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import sun.audio.AudioPlayer.player

class MyPlaceholderExpansion(private val plugin: JavaPlugin) : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "kimetsucore" // Идентификатор для вашего плейсхолдера
    }

    override fun getAuthor(): String {
        return plugin.description.authors.joinToString()
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    fun getPlayerLvl(player: Player?): Int{
        if (player == null) {
            return 0
        }
        return DataBase.getInt("${player?.name}.lvl")
    }

    fun getPlayerCoin(player: Player?): Int{
        if (player == null) {
            return 0
        }
        if (DataBase.getInt("${player?.name}.primecoin") == null) {
            return 0
        }
        return DataBase.getInt("${player?.name}.primecoin")
    }

    fun getDayTime(player: Player?): String {
        val timeString = PlaceholderAPI.setPlaceholders(player, "%server_time%")
        val time = timeString?.toLongOrNull() ?: return "N/A"
        val hours = (time / 1000 + 6) % 24
        val minutes = (time % 1000) * 60 / 1000
        return String.format("%02d:%02d", hours, minutes)
    }
    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        return when (params) {
            "level" -> getPlayerLvl(player).toString()
            "primecoin" -> getPlayerCoin(player).toString()
            "daytime" -> getDayTime(player)
            else -> null
        }
    }
}