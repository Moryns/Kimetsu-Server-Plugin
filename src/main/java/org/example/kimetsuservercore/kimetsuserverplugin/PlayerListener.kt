package org.example.kimetsuservercore.kimetsuserverplugin

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerExpChangeEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import javax.xml.crypto.Data
import kotlin.random.Random

var playerExperianceList: MutableMap<String, Int> = mutableMapOf()

class PlayerListener : Listener {
    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        try {
            val player = event.player
            checkPlayerStats(player)
            playerExperianceList[player.name] = DataBase.getInt("${player.name}.experiance")
            updatePlayerAttributes(
                player,
                DataBase.getInt("${player.name}.lvl"),
                DataBase.getStr("${player.name}.trait")
            )
            if (!player.hasPlayedBefore()) {
                DataBase.set("${player.name}.lvl", "0")
                DataBase.set("${player.name}.primecoin", "0")
                DataBase.set("${player.name}.trait", "none")
                DataBase.set("${player.name}.experiance", "0")
                setStatistics(player)
            }
        } catch (e: Exception) {
            event.player.sendMessage("An error occurred: ${e.message}")
            e.printStackTrace()
        }

        @EventHandler
        fun playerRespawn(event: PlayerRespawnEvent) {
            val player = event.player
            updatePlayerAttributes(
                player,
                DataBase.getInt("${player.name}.lvl"),
                DataBase.getStr("${player.name}.trait")
            )
        }

        @EventHandler
        fun playerInteract(event: PlayerInteractEntityEvent) {
            if (event.rightClicked is Player) {
                val player = event.player
                val targetPlayer = event.rightClicked as Player
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    *TextComponent.fromLegacyText(
                        "${applyHexColors(targetPlayer.displayName)} §r| ${
                            applyHexColors(
                                getPlaceholderValue(targetPlayer, "%luckperms_primary_group_name%") ?: ""
                            )
                        } §r| ${applyHexColors(getPlayerTrait(targetPlayer))}"
                    )
                )
            }
        }

        @EventHandler
        fun onPlayerDamage (event: EntityDamageEvent) {
            if (event.entity is Player) {
                val player = event.entity
                player as Player
                val chance = Random.nextDouble(0.0, 101.0)
                if (chance <= 5.0) {
                    val dexterity = DataBase.getInt("${player.name}.dexterity")
                    val dodgechance = 1 + (dexterity * 0.25)
                    if (chance <= dodgechance ) {
                        event.damage = event.damage/4
                        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 3 * 20, 4, false,false,false))
                        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 40, 0, false,false,false))
                        player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 45, 10, false,false,false))
                    }
                }
            }
        }

        @EventHandler
        fun playerExpChange(event: PlayerExpChangeEvent) {
            val player = event.player
            event.amount = 0
            player.level = 0
            player.totalExperience = 0
            player.exp = 0.0F
            setPlayerExperiance(player)
        }

        @EventHandler
        fun onPlayerDrop(event: PlayerDropItemEvent) {
            val player = event.player
            val droppedItem = event.itemDrop.itemStack

            if (droppedItem != null && nichirinSwordList.contains(droppedItem.type.toString())) {
                event.isCancelled = true
                player.sendMessage("§aВы не можете выбросить этот предмет!")
            }
        }

        @EventHandler
        fun playerLeave(event: PlayerQuitEvent) {
            val playerName = event.player.name
            playerExperianceList.get(playerName)?.let { DataBase.set("${playerName}.experiance", it) }
            playerExperianceList.remove(playerName)
        }
    }
}