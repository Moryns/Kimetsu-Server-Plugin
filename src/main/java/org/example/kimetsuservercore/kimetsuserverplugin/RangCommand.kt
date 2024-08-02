package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class RangCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size < 3) {
            sender.sendMessage("§3[§bKimetsu CP§3]§cАгрументы должны находиться в порядке: Операция -> Никнейм -> Путь -> Значение")
            return false
        }
        val player = Bukkit.getPlayer(args[1])
        if (player == null) {
            sender.sendMessage("§3[§bKimetsu CP§3]§cДанного игрока нету на сервере")
            return true
        }
        when (args[0]) {
            "set" -> {
                DataBase.set("${args[1]}.${args[2]}", args[3])
                sender.sendMessage("§3[§bKimetsu CP§3]§aВы успешно изменили ${args[2]} игрока ${args[1]} на ${args[3]}")
                if (args[2] == "rank") {
                    val rank = DataBase.getInt("${args[1]}.rank")
                    //send(rank.toString())
                    updatePlayerAttributes(player, rank)
                    //send(DataBase.getInt("${player.name}.rank"))
                }

            }
            "get" -> {
                sender.sendMessage("§3[§bKimetsu CP§3] §a${DataBase.getAny("${args[1]}.${args[2]}")}")
            }
        }
        return true
    }

    fun setPlayerAttribute(player: Player, attribute: Attribute, value: Double) {
        player.getAttribute(attribute)?.let { it.baseValue = value }
        //player.getAttribute(attribute)?.addModifier(AttributeModifier("SVO", value, AttributeModifier.Operation.ADD_NUMBER))
        //send(player.getAttribute(attribute)?.baseValue)
    }

    fun send(text: Any?) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage("[DEBUG] $text")
            return
        }
    }

    fun updatePlayerAttributes(player: Player, rank: Int) {
        when (rank) {
            0 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 20.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.1)
            }
            1 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.1)
            }
            2 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.15)
            }
            3 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.2)
            }
            4 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.23)
            }
            5 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.25)
            }
            6 -> {
                setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, 24.0)
                setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.3)
            }
        }
    }
}