package org.example.kimetsuservercore.kimetsuserverplugin.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.example.kimetsuservercore.kimetsuserverplugin.DataBase
import org.example.kimetsuservercore.kimetsuserverplugin.applyHexColors
import org.example.kimetsuservercore.kimetsuserverplugin.setStatistics
import org.example.kimetsuservercore.kimetsuserverplugin.updatePlayerAttributes

class PlayerTraitResetCommand : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            val player = sender as Player
            setStatistics(player)
            updatePlayerAttributes(player, DataBase.getInt("${player.name}.lvl"), DataBase.getStr("${player.name}.trit"))
            return true
        }
        if (args.size > 1) return false
        if (Bukkit.getPlayer(args[0]) in Bukkit.getOnlinePlayers()) {
            val player = Bukkit.getPlayer(args[0]) as Player
            setStatistics(player)
            updatePlayerAttributes(player, DataBase.getInt("${player.name}.lvl"), DataBase.getStr("${player.name}.trit"))
            return true
        }
        sender.sendMessage(applyHexColors("&#454448[&#582CAC&lK&#8533C8&lS&#B13AE4&lC&#454448] <#C8B6FF>Первым аргументом вы можете указать игрока. Иначе команда применится к вам.</#DC8BFF>"))
        return true
    }
}