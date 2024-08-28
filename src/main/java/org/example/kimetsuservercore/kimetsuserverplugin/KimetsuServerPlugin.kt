package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.example.kimetsuservercore.kimetsuserverplugin.commands.*

class KimetsuServerPlugin : JavaPlugin() {
    companion object {
        lateinit var inst: KimetsuServerPlugin private set
    }

    override fun onEnable() {
        inst = this;
        dataFolder.mkdirs();
        DataBase.init();
        config
        saveDefaultConfig()
        getCommand("database")?.setExecutor(DataBaseCommand())
        getCommand("kimetsuexp")?.setExecutor(ExperianceCommand())
        getCommand("statreset")?.setExecutor(PlayerTraitResetCommand())
        getCommand("statsmenu")?.setExecutor(StatMenuCommand())
        getCommand("me")?.setExecutor(BaseCommand())
        getCommand("do")?.setExecutor(BaseCommand())
        getCommand("try")?.setExecutor(BaseCommand())
        getCommand("n")?.setExecutor(BaseCommand())
        getCommand("report")?.setExecutor(BaseCommand())
        server.pluginManager.registerEvents(PlayerListener(), this)
        startScheduler();
        Material.entries.forEach { logger.info(it.name) }
        playerExperianceReload()
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            MyPlaceholderExpansion(this).register()
        }

        logger.info("-----------------------[Kimetsu Server Plugin ]-----------------------")
        logger.info("                       Plugin has been started!")
    }

    override fun onDisable() {
        playerExperianceUnload()
    }


    fun startScheduler() {
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            if (playerExperianceList.isEmpty())
                else {
                println(playerExperianceList)
                }
            Bukkit.getOnlinePlayers().forEach { player ->
                var mastery = calculateStat(
                    DataBase.getInt("${player.name}.strength").toInt(),
                    DataBase.getInt("${player.name}.lvl").toInt(),
                    0.0,
                    0.05
                ) *
                calculateStat(
                    DataBase.getInt("${player.name}.mastery").toInt(),
                    DataBase.getInt("${player.name}.lvl").toInt(),
                    0.0,
                    0.05
                )
                player.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.INCREASE_DAMAGE,
                        8 * 20,
                        mastery.toInt(),
                        false,
                        false,
                        false
                    )
                )
                if (DataBase.getInt("${player.name}.lvl") >= 30) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 8 * 20, 2, false, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 8 * 20, 2, false, false, false))
                    if (player.health < player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value) {
                        if (player.health > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value / 3) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 1, false, false, false))
                        }
                    }
                } else if (DataBase.getInt("${player.name}.lvl") >= 20) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 11 * 20, 1, false, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 8 * 20, 1, false, false, false))
                    if (player.health < player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value) {
                        if (player.health > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value / 3) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 1, false, false, false))
                        }
                    }
                } else if (DataBase.getInt("${player.name}.lvl") >= 10) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 11 * 20, 0, false, false, false))
                    player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 8 * 20, 0, false, false, false))
                    if (player.health < player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value) {
                        if (player.health > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value / 3) {
                            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 0, false, false, false))
                        }
                    }
                }
            }
        }, 0, 20 * 7L)
    }
}
