package org.example.kimetsuservercore.kimetsuserverplugin.commands

import jdk.nashorn.internal.runtime.regexp.joni.Config
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.example.kimetsuservercore.kimetsuserverplugin.KimetsuServerPlugin
import org.example.kimetsuservercore.kimetsuserverplugin.applyHexColors
import org.example.kimetsuservercore.kimetsuserverplugin.playerExperianceList
import org.example.kimetsuservercore.kimetsuserverplugin.setPlayerExperiance
import java.awt.Component
import kotlin.random.Random

class BaseCommand : CommandExecutor {
    val cooldowns = HashMap<String, Long>()
    val cooldownTime = 10 * 1000

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = sender as Player
        when (label) {
            "me" -> {MeCommand(player, args)}
            "do" -> {DoCommand(player, args)}
            "try" -> {TryCommand(player, args)}
            "report" -> {MsgCommand(player, args)}
            "n" -> {NrpCommand(player, args)}
        }
        return true
    }

    fun MeCommand (player: Player ,args: Array<out String>?) : Boolean{
        val lastUsage = cooldowns[player.name]
        if (lastUsage != null) {
            val timePassed = System.currentTimeMillis() - lastUsage
            if (timePassed < cooldownTime) {
                val timeLeft = (cooldownTime - timePassed) / 1000
                player.sendMessage(applyHexColors("&cВы не можете использовать эту команду ещё $timeLeft секунд."))
                return false
            }
        }
        val action = args?.joinToString(" ")
        if (action!!.isEmpty()) {
            player.sendMessage(applyHexColors("&cВведите действие, которое вы хотите совершить."))
            return false
        }
        val message = applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.me-char")} &f${player.displayName} &#FFE399$action")
        for (target in Bukkit.getOnlinePlayers()) {
            val distance = player.location.distance(target.location)
            if (distance <= KimetsuServerPlugin.inst.config.getInt("BaseCommand.me-radius", 30)) {
                target.sendMessage(message)
            }
        }
        addExperiance(player, action)
        cooldowns[player.name] = System.currentTimeMillis()
        return true
    }

    fun DoCommand (player: Player ,args: Array<out String>?) : Boolean {
        val lastUsage = cooldowns[player.name]
        if (lastUsage != null) {
            val timePassed = System.currentTimeMillis() - lastUsage
            if (timePassed < cooldownTime) {
                val timeLeft = (cooldownTime - timePassed) / 1000
                player.sendMessage(applyHexColors("&cВы не можете использовать эту команду ещё $timeLeft секунд."))
                return false
            }
        }
        val action = args?.joinToString(" ")
        if (action!!.isEmpty()) {
            player.sendMessage(applyHexColors("&cВведите действие, которое вы хотите совершить."))
            return false
        }
        val message = applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.do-char")} &#D1D0FF$action &f${player.displayName}")
        for (target in Bukkit.getOnlinePlayers()) {
            val distance = player.location.distance(target.location)
            if (distance <= KimetsuServerPlugin.inst.config.getInt("BaseCommand.do-radius", 30)) {
                target.sendMessage(message)
            }
        }
        addExperiance(player, action)
        cooldowns[player.name] = System.currentTimeMillis()
        return true
    }

    fun TryCommand(player: Player ,args: Array<out String>?) : Boolean {
        val lastUsage = cooldowns[player.name]
        if (lastUsage != null) {
            val timePassed = System.currentTimeMillis() - lastUsage
            if (timePassed < cooldownTime) {
                val timeLeft = (cooldownTime - timePassed) / 1000
                player.sendMessage(applyHexColors("&cВы не можете использовать эту команду ещё $timeLeft секунд."))
                return false
            }
        }
        val action = args?.joinToString(" ")
        if (action!!.isEmpty()) {
            player.sendMessage(applyHexColors("&cВведите действие, которое вы хотите совершить."))
            return false
        }
        val message = applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.try-char")} &f${player.displayName} &7$action &f| ${getSuccess()}")
        for (target in Bukkit.getOnlinePlayers()) {
            val distance = player.location.distance(target.location)
            if (distance <= KimetsuServerPlugin.inst.config.getInt("BaseCommand.try-radius", 30)) {
                target.sendMessage(message)
            }
        }
        cooldowns[player.name] = System.currentTimeMillis()
        return true
    }

    fun NrpCommand(player: Player ,args: Array<out String>?) : Boolean {
        val lastUsage = cooldowns[player.name]
        if (lastUsage != null) {
            val timePassed = System.currentTimeMillis() - lastUsage
            if (timePassed < cooldownTime) {
                val timeLeft = (cooldownTime - timePassed) / 1000
                player.sendMessage(applyHexColors("&cВы не можете использовать эту команду ещё $timeLeft секунд."))
                return false
            }
        }
        val action = args?.joinToString(" ")
        if (action!!.isEmpty()) {
            player.sendMessage(applyHexColors("&cВведите OOC информацию которую хотите сказать."))
            return false
        }
        val message = applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.nrp-char")} &f${player.displayName} &7$action &f")
        for (target in Bukkit.getOnlinePlayers()) {
            val distance = player.location.distance(target.location)
            if (distance <= KimetsuServerPlugin.inst.config.getInt("BaseCommand.nrp-radius", 100)) {
                target.sendMessage(message)
            }
        }
        cooldowns[player.name] = System.currentTimeMillis()
        return true
    }

    fun MsgCommand(player: Player, args: Array<out String>?) : Boolean {
        val lastUsage = cooldowns[player.name]
        if (lastUsage != null) {
            val timePassed = System.currentTimeMillis() - lastUsage
            if (timePassed < cooldownTime) {
                val timeLeft = (cooldownTime - timePassed) / 1000
                player.sendMessage(applyHexColors("&cВы не можете использовать эту команду ещё $timeLeft секунд."))
                return false
            }
        }
        val action = args?.joinToString(" ")
        if (action!!.isEmpty()) {
            player.sendMessage(applyHexColors("&cВведите сообщение Ворону Уз."))
            return false
        }
        var count = 0
        val message = applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.report-char")} &f${player.displayName} &#34004E» <#5C3A6D>$action</#C16E75>")
        for (target in Bukkit.getOnlinePlayers()) {
            if (target.hasPermission("reports")) {
                target.sendMessage(message)
                target.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
                count += 1
            }
        }
        if (count == 0) {
            player.sendMessage(applyHexColors("${KimetsuServerPlugin.inst.config.getString("BaseCommand.report-char")} &#34004E» <#C8B6FF>Ворон Уз не нашёл адресата письма</#DC8BFF>"))
        }
        cooldowns[player.name] = System.currentTimeMillis()
        return true
    }

    fun getSuccess () : String {
        if (Random.nextInt(0, 100) > 50) {
            return applyHexColors("&#32E946Успешно")
        }
        else {
            return applyHexColors("&#E53333Неуспешно")
        }
    }

    fun addExperiance (player: Player ,action: String) {
        val experiencePoints = action.length / 10
        if (experiencePoints > 0) {
            val playerExperiance = playerExperianceList.get(player.name)
            if (playerExperiance != null) {
                playerExperianceList.set(player.name, playerExperiance + experiencePoints)
                val actionBarMessage = applyHexColors("&aВы получили ${experiencePoints} очков опыта")
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(actionBarMessage))
                setPlayerExperiance(player)
            }
        }
    }
}
