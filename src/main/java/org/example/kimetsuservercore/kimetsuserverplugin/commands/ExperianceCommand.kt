package org.example.kimetsuservercore.kimetsuserverplugin.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.example.kimetsuservercore.kimetsuserverplugin.applyHexColors
import org.example.kimetsuservercore.kimetsuserverplugin.playerExperianceList
import org.example.kimetsuservercore.kimetsuserverplugin.setPlayerExperiance

class ExperianceCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            println("1")
            return false
        }
        if (args.size < 2 || args.size > 3) {
            println("2")
            return false
        }
        val playerName = if (args.size == 3) args[1] else sender.name
        val player = Bukkit.getPlayer(playerName)
        if (Bukkit.getPlayer(args[1]) in Bukkit.getOnlinePlayers()) {
            when (args[0]) {
                "set" -> {
                    ExperianceSet(player!!, args[2].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно установили экспирианс ${playerName}</#DC8BFF>"))
                }

                "add" -> {
                    ExperianceIncrease(player!!, args[2].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно увеличили экспирианс ${playerName}</#DC8BFF>"))
                }

                "reduce" -> {
                    ExperianceDecrease(player!!, args[2].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно уменьшили экспирианс ${playerName}</#DC8BFF>"))
                }
            }
            setPlayerExperiance(player!!)
        } else {
            when (args[0]) {
                "set" -> {
                    ExperianceSet(player!!, args[1].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно установили экспирианс ${playerName}</#DC8BFF>"))
                }

                "add" -> {
                    ExperianceIncrease(player!!, args[1].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно увеличили экспирианс ${playerName}</#DC8BFF>"))
                }

                "reduce" -> {
                    ExperianceDecrease(player!!, args[1].toInt())
                    sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&r&#454448] <#C8B6FF>Вы успешно уменьшили экспирианс ${playerName}</#DC8BFF>"))
                }
            }
        }
        setPlayerExperiance(player!!)
        return true
    }

    fun ExperianceIncrease(player: Player, value: Int) {
        val result = playerExperianceList.get(player.name)
        playerExperianceList.set(player.name, result!! + value)
    }

    fun ExperianceDecrease(player: Player, value: Int) {
        val result = playerExperianceList.get(player.name)
        playerExperianceList.set(player.name, result!! - value)
    }

    fun ExperianceSet(player: Player, value: Int) {
        playerExperianceList.set(player.name, value)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        if (args.isEmpty()) return null

        return when (args.size) {
            1 -> listOf("set", "add", "reduce").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> Bukkit.getOnlinePlayers().map(Player::getName).filter { it.startsWith(args[1], ignoreCase = true) }
            else -> null
        }
    }
}

