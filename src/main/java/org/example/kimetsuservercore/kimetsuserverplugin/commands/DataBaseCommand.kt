package org.example.kimetsuservercore.kimetsuserverplugin.commands

import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.example.kimetsuservercore.kimetsuserverplugin.*

class DataBaseCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args[0] == "reload") {
            DataBase.init()
            return true
        }
        if (args.size <= 3) {
            sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&#454448] &r<#C8B6FF>Агрументы должны находиться в порядке: Операция -> Никнейм -> Путь -> Значение</#DC8BFF>"))
            return false
        }
        val player = Bukkit.getPlayer(args[1])
        if (player == null) {
            sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&#454448] &r<#C8B6FF>Данного игрока нету на сервере</#DC8BFF>"))
            return true
        }
        when (args[0]) {
            "set" -> {
                DataBase.set("${args[1]}.${args[2]}", args[3])
                sender.sendMessage("§3[§bKimetsu CP§3]§aВы успешно изменили ${args[2]} игрока ${args[1]} на ${args[3]}")
                if (args[2] == "lvl") {
                    val lvl = DataBase.getInt("${args[1]}.lvl")
                    //send(rank.toString())
                    updatePlayerAttributes(player, lvl, DataBase.getStr("${args[1]}.trait"))
                    player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?:20.0
                    //send(DataBase.getInt("${player.name}.rank"))
                }

            }

            "get" -> {
                sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&#454448] &r<#C8B6FF>${DataBase.getAny("${args[1]}.${args[2]}")}</#DC8BFF>"))
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        if (args.isEmpty()) return null

        return when (args.size) {
            1 -> listOf("reload", "set", "get").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> Bukkit.getOnlinePlayers().map(Player::getName).filter { it.startsWith(args[1], ignoreCase = true) }
            3 -> listOf("lvl", "strength", "speed", "mastery", "dexterity", "vitality")
            4 -> listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
            else -> null
        }
    }

    fun setPlayerAttribute(player: Player, attribute: Attribute, value: Double) {
        player.getAttribute(attribute)?.let { it.baseValue = value }
        //player.getAttribute(attribute)?.addModifier(AttributeModifier("SVO", value, AttributeModifier.Operation.ADD_NUMBER))
        //send(player.getAttribute(attribute)?.baseValue)
    }


}