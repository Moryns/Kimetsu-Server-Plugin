package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.example.kimetsuservercore.kimetsuserverplugin.ConfigMenager.configInit

class KimetsuServerPlugin : JavaPlugin() {
    companion object {
        lateinit var inst: KimetsuServerPlugin private set
    }

    override fun onEnable() {
        inst = this;
        dataFolder.mkdirs();
        DataBase.init();
        configInit()
        getCommand("database")?.setExecutor(RangCommand());
        getCommand("statreset")?.setExecutor(PlayerTraitCommand());
        server.pluginManager.registerEvents(PlayerJoinEvent(), this)
        startScheduler();
        logger.info("-----------------------[Kimetsu Server Plugin ]-----------------------")
        logger.info("                       Plugin has been started!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


    fun startScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            Bukkit.getOnlinePlayers().forEach{ player ->
                when (DataBase.getInt("${player.name}.rank")) {
                    1 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    2 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    3 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    4 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    5 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    6 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    7 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    8 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                    9 -> {
                        player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 0, true, false, false))
                    }
                }
            }
        }, 0, 20 * 5L)
    }
}
