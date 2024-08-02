package org.example.kimetsuservercore.kimetsuserverplugin

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object DataBase{
    val file = File(KimetsuServerPlugin.inst.dataFolder, "Database.yml")
    private lateinit var dataBase: FileConfiguration

    fun init() {
        if (!file.exists()) file.createNewFile()
        dataBase = YamlConfiguration.loadConfiguration(file)
    }

    fun set(path: String, value: Any){
        dataBase.set(path, value)
        save()
    }

    fun getAny(path: String): Any? {
        return dataBase.get(path)
    }

    fun getInt(path: String): Int{
        return ((dataBase.getString(path, "0")!!.toIntOrNull()) ?: 0)
    }

    fun getStr(path: String): String{
        return dataBase.getString(path, "none")!!
    }

    fun getBoolean(path: String): Boolean{
        return dataBase.getBoolean(path)
    }

    fun save(){
        dataBase.save(file)
    }
}
