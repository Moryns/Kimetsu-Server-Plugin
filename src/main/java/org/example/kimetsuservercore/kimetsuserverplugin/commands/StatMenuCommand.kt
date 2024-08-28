package org.example.kimetsuservercore.kimetsuserverplugin.commands

import org.apache.commons.lang.ObjectUtils.Null
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.example.kimetsuservercore.kimetsuserverplugin.openGui

class StatMenuCommand : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            openGui(sender as Player, sender as Player)
            return true
        }
        val player = Bukkit.getPlayer(args[0])
        if (player == null) return false
        openGui(player, sender as Player)
        return true
    }
}