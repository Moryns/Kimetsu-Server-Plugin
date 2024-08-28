package org.example.kimetsuservercore.kimetsuserverplugin

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import net.md_5.bungee.api.ChatColor
import net.melion.rgbchat.api.RGBApi
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import sun.audio.AudioPlayer.player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window
import javax.xml.crypto.Data
import kotlin.random.Random

fun checkPlayerStats(player: Player) {
    val dataList = listOf<String>("lvl", "trait", "primecoin", "experiance")
    for (i in 0..dataList.size - 1) {
        if (DataBase.getAny("${player.name}.${dataList[i]}") == null) {
            DataBase.set("${player.name}.${dataList[i]}", "0")
        }
    }
}

fun statGeneration(): Array<Int> {
    var strength: Int
    var speed: Int
    var mastery: Int
    var dexterity: Int
    var vitality: Int
    var allStats: Int

    do {
        speed = Random.nextInt(1, 12)
        strength = Random.nextInt(1, 12)
        dexterity = Random.nextInt(1, 12)
        vitality = Random.nextInt(1, 12)
        mastery = Random.nextInt(1, 12)
        allStats = speed + strength + mastery + vitality + dexterity
    } while (allStats < 38 || allStats > 45)

    // Возвращаем массив значений статов
    return arrayOf(strength, speed, mastery, dexterity, vitality)
}

fun setStatistics(player: Player) {
    val stats = statGeneration()
    DataBase.set("${player.name}.strength", "${stats[0]}")
    DataBase.set("${player.name}.speed", "${stats[1]}")
    DataBase.set("${player.name}.mastery", "${stats[2]}")
    DataBase.set("${player.name}.dexterity", "${stats[3]}")
    DataBase.set("${player.name}.vitality", "${stats[4]}")
}

fun calculateStat(StatLVL: Int, playerLvL: Int, baseValue: Double, statIncrease: Double): Double {
    var multiplier = 1.0 + (StatLVL * 0.04)
    var resultValue = baseValue
    for (i in 0..playerLvL) {
        resultValue += statIncrease * multiplier
    }
    return resultValue
}

fun updatePlayerAttributes(player: Player, lvl: Int, trait: String) {
    var traitStatVitality = 1.0
    var traitStatSpeed = 1.0
    var traitStatDexterity = 1.0
    var dexterityBuffSpeed = 1.0 + (DataBase.getInt("${player.name}.dexterity") * 0.04) * 0.95
    when (trait) {
        "king_of_the_kings" -> {
            traitStatVitality = 1.05
            traitStatSpeed = 1.01
            traitStatDexterity = 1.02
        }
        "genius" -> {
            traitStatVitality = 1.1
            traitStatSpeed = 1.1
            traitStatDexterity = 1.1
        }
    }
    var vitality = calculateStat(
        DataBase.getInt("${player.name}.vitality").toInt(),
        DataBase.getInt("${player.name}.lvl").toInt(),
        20.0,
        0.5
    ) * traitStatVitality
    val speed = calculateStat(
        DataBase.getInt("${player.name}.speed").toInt(),
        DataBase.getInt("${player.name}.lvl").toInt(),
        1.0,
        0.007
    ) * dexterityBuffSpeed * traitStatSpeed
    val dexterity = calculateStat(
        DataBase.getInt("${player.name}.dexterity").toInt(),
        DataBase.getInt("${player.name}.lvl").toInt(),
        2.9,
        0.01
    ) * traitStatDexterity
    vitality = vitality - (vitality - vitality.toInt())
    if ((vitality % 2).toInt() != 0) vitality -= 1
    setPlayerAttribute(player, Attribute.GENERIC_MAX_HEALTH, vitality.toDouble())
    setPlayerAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, speed / 10)
    setPlayerAttribute(player, Attribute.GENERIC_ATTACK_SPEED, dexterity)
}

fun setPlayerAttribute(player: Player, attribute: Attribute, value: Double) {
    player.getAttribute(attribute)?.let { it.baseValue = value }
}

fun send(text: Any?) {
    Bukkit.getOnlinePlayers().forEach { player ->
        player.sendMessage("[DEBUG] $text")
        return
    }
}

fun playerExperianceReload() {
    Bukkit.getOnlinePlayers().forEach { player ->
        playerExperianceList.put(player.name, DataBase.getInt("${player.name}.experiance"))
    }
}

fun playerExperianceUnload() {
    playerExperianceList.forEach { (playerName, experience) ->
        DataBase.set("${playerName}.experiance", experience)
    }
}

fun setPlayerExperiance(player: Player) {
    player.level = calculateLevel(playerExperianceList[player.name]!!.toInt())
    player.exp = calculateProgress(playerExperianceList[player.name]!!)
    DataBase.set("${player.name}.lvl", player.level)
    updatePlayerAttributes(player, DataBase.getInt("${player.name}.lvl"), DataBase.getStr("${player.name}.trait"))
}

fun calculateLevel(exp: Int): Int {
    var level = 0
    var expNeeded = 7
    var remainingExp = exp / 100

    while (remainingExp >= expNeeded) {
        remainingExp -= expNeeded
        level++
        expNeeded += 2
    }

    return level
}

fun calculateProgress(exp: Int): Float {
    var level = 0
    var expNeeded = 7
    var remainingExp = exp / 100

    while (remainingExp >= expNeeded) {
        remainingExp -= expNeeded
        level++
        expNeeded += 2
    }

    return remainingExp.toFloat() / expNeeded.toFloat()
}

fun applyHexColors(text: String): String {
    return RGBApi.toColoredMessage(text.replace("&r", "§r").replace("&l", "§l"))
}

fun openGui(player: Player, viewer: Player) {
    val playerHead = ItemStack(Material.PLAYER_HEAD)
    val skullMeta = playerHead.itemMeta as SkullMeta
    skullMeta.owningPlayer = player
    playerHead.itemMeta = skullMeta
    val gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
        .setStructure(
            ". . - . + . = . .",
        )
        .addIngredient('#', SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
        .addIngredient(
            '+', SimpleItem(
                ItemBuilder(playerHead).setDisplayName("§6Врождённые таланты").addLoreLines(
                    "",
                    "§6§l> §eТелосложение: §f${statsTransfer(player, "${player.name}.strength")}",
                    "§6§l> §eМастерство: §f§f${statsTransfer(player, "${player.name}.mastery")}",
                    "§6§l> §eСкорость: §f§f${statsTransfer(player, "${player.name}.speed")}",
                    "§6§l> §eЛовкость: §f§f${statsTransfer(player, "${player.name}.dexterity")}",
                    "§6§l> §eВыносливость: §f§f${statsTransfer(player, "${player.name}.vitality")}",
                    ""
                )
            )
        )
        .addIngredient(
            '-', SimpleItem(
                ItemBuilder(Material.FLOWER_BANNER_PATTERN).setDisplayName("§6Статистика ${player.name}").addLoreLines(
                    "",
                    "§6§l> §eЧасов наиграно: §f${getPlaceholderValue(player, "%statistic_time_played%")}",
                    ""
                )
            )
        ).addIngredient(
            '=', SimpleItem(
                when (DataBase.getStr("${player.name}.trait")) {
                    "king_of_the_kings" -> {
                        ItemBuilder(Material.ENDER_EYE).setDisplayName(getPlayerTrait(player))
                            .addLoreLines(
                                applyHexColors("&#F6E168Виличайший среди величайших,"),
                                applyHexColors("&#F6E168Сильнейши среди сильнейших"),
                                applyHexColors("&#F6E168Прошлое твоё не известно,"),
                                applyHexColors("&#F6E168А будущее сокрыто,"),
                                applyHexColors("&#F6E168Обратив на себя взор судьбы"),
                                applyHexColors("&#F6E168Осилишь ты под натиском её прожить?")
                            )
                    }
                    "genius" -> {
                        ItemBuilder(Material.ENDER_EYE).setDisplayName(getPlayerTrait(player))
                            .addLoreLines(
                                applyHexColors("&#52367Подобным тебе нет"),
                                applyHexColors("&#52367AИ быть не может"),

                            )
                    }

                    else -> {
                        ItemBuilder(Material.ENDER_PEARL).setDisplayName(getPlayerTrait(player))
                    }
                }
            )
        )
        .build()

    val window = Window.single()
        .setViewer(viewer)
        .setTitle("Обо мне")
        .setGui(gui)
        .build()

    window.open()
}

fun getPlayerTrait(player: Player): String {
    when (DataBase.getStr("${player.name}.trait")) {
        "king_of_the_kings" -> {
            return applyHexColors("&#9863E7Стигма &#E7BE63« &#E7BE63К&#E7BE63о&#E7BE63р&#E7BE63о&#E7B861л&#E6B15Fь&#E6AB5D, &#E59E58в&#E49756с&#E49154е&#E38A52х &#E27E4DК&#E2774Bо&#E17149р&#E16A47о&#E06445л&#E05D43е&#DF5740й &#DE4A3C» ")
        }
        "genius" -> {
            return applyHexColors("&#9021FFС&#9733FCт&#9E46FAи&#A558F7г&#AC6BF5м&#B37DF2а &#C1A2ED« &#C0ADE1В&#B8A6D7о&#B09FCEз&#A897C4в&#A090BBе&#9889B1д&#9082A8ё&#887B9Eн&#807495н&#786D8Bы&#706682й &#60576Fв &#50495CА&#484252б&#403B49с&#38343Fо&#302D36л&#28252Cю&#201E23т &#101010»")
        }

        else -> {
            return applyHexColors("&#626262Стигма")
        }
    }
}

fun getPlaceholderValue(player: Player, placeholder: String): String? {
    return PlaceholderAPI.setPlaceholders(player, placeholder)
}

fun statsTransfer(player: Player, path: String): String {
    val list = listOf("F", "F+", "D", "D+", "C", "C+", "B", "B+", "A", "A+", "S", "S+")
    val index = DataBase.getInt(path)

    return if (index in 1..list.size) {
        list[index - 1] // Возвращаем значение из списка
    } else {
        "Unknown" // Возвращаем значение по умолчанию, если индекс вне диапазона
    }
}

val staminaMap: MutableMap<String, Double> = mutableMapOf()
val maxStamina = 20.0

fun getStamina(player: Player): Double? {
    return staminaMap[player.name]
}

fun setStamina(player: Player, stamina: Double) {
    staminaMap[player.toString()] = stamina.coerceIn(0.0, maxStamina).toDouble()
    updateFoodLevel(player)
}

fun decreaseStamina(player: Player, amount: Int) {
    getStamina(player)?.minus(amount)?.let { setStamina(player, it) }
}

fun increaseStamina(player: Player, amount: Double) {
    getStamina(player)?.plus(amount)?.let { setStamina(player, it) }
}

private fun updateFoodLevel(player: Player) {
    player.foodLevel = getStamina(player)!!.toInt()
}

val nichirinSwordList = listOf(
    "KIMETSUNOYAIBA_NICHIRINSWORD",
    "KIMETSUNOYAIBA_NICHIRINSWORD_BAMBOO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_BAMBOO_2",
    "KIMETSUNOYAIBA_NICHIRINSWORD_BLACK",
    "KIMETSUNOYAIBA_NICHIRINSWORD_CHERRY_BLOSSOM",
    "KIMETSUNOYAIBA_NICHIRINSWORD_FLAME",
    "KIMETSUNOYAIBA_NICHIRINSWORD_GENYA",
    "KIMETSUNOYAIBA_NICHIRINSWORD_GOLDEN",
    "KIMETSUNOYAIBA_NICHIRINSWORD_HIMEJIMA_1",
    "KIMETSUNOYAIBA_NICHIRINSWORD_HIMEJIMA_2",
    "KIMETSUNOYAIBA_NICHIRINSWORD_IGURO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_INOSUKE",
    "KIMETSUNOYAIBA_NICHIRINSWORD_KAIGAKU",
    "KIMETSUNOYAIBA_NICHIRINSWORD_KANAE",
    "KIMETSUNOYAIBA_NICHIRINSWORD_KANAWO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_KANROJI",
    "KIMETSUNOYAIBA_NICHIRINSWORD_KOCHO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_MIST",
    "KIMETSUNOYAIBA_NICHIRINSWORD_RENGOKU",
    "KIMETSUNOYAIBA_NICHIRINSWORD_SENIOR",
    "KIMETSUNOYAIBA_NICHIRINSWORD_SENIOR_2",
    "KIMETSUNOYAIBA_NICHIRINSWORD_SENIOR_SUPER",
    "KIMETSUNOYAIBA_NICHIRINSWORD_SHINAZUGAWA",
    "KIMETSUNOYAIBA_NICHIRINSWORD_TANJIRO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_TANJIRO_2",
    "KIMETSUNOYAIBA_NICHIRINSWORD_THUNDER",
    "KIMETSUNOYAIBA_NICHIRINSWORD_TOKITO",
    "KIMETSUNOYAIBA_NICHIRINSWORD_TOMIOKA",
    "KIMETSUNOYAIBA_NICHIRINSWORD_UZUI",
    "KIMETSUNOYAIBA_NICHIRINSWORD_WATER",
    "KIMETSUNOYAIBA_NICHIRINSWORD_WIND",
    "KIMETSUNOYAIBA_NICHIRINSWORD_YORIICHI",
    "KIMETSUNOYAIBA_NICHIRINSWORD_ZENITSU",
    "KIMETSUNOYAIBA_NICHIRINSWORDMOON",
    "BARRIER"
)