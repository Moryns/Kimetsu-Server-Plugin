package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Config {
    val file = File(KimetsuServerPlugin.inst.dataFolder, "config.yml")
    private lateinit var config : FileConfiguration

    fun configInit() {
        if (!file.exists()) DataBase.file.createNewFile()
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun set (path: String, value: Any) {
        config.set(path, value)
        save()
    }

    fun getInt (path: String) : Int {
        return ((config.getString(path, "0")!!.toIntOrNull()) ?: 0)
    }

    fun getStr (path: String) : String {
        return config.getString(path, "none")!!
    }

    fun getBoolean (path: String) {
        return getBoolean(path)
    }

    fun getAny (path: String) : Any? {
        return config.get(path)
    }

    fun save () {
        config.save(file)
    }
}
