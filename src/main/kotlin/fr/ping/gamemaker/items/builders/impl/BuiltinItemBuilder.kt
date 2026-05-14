package fr.ping.gamemaker.items.builders.impl

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.builders.models.ItemBuilder
import fr.ping.gamemaker.menus.models.MenuButton
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Material

object BuiltinItemBuilder : ItemBuilder() {
  private val config : Config
    get() = GameMakerPlugin.getInstance().config.builtins.itemBuilder
  private val i18n by lazy {
    ResourceManager["item_build/en_US", I18n::class.java]
  }

  override fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>, context: Map<String, Any?>, isKeyInConfig : Boolean): List<String>? {
    when (key) {
      "lore" -> {
        if (value == null || value !is List<*>) return null
        @Suppress("UNCHECKED_CAST")
        return value.map { if (it.toString().startsWith("$")) I18nManager["ENGLISH", it.toString().removePrefix("$")] else it.toString() }.let {
          if (config.insertSpaceAfter.getOrPut(key) { true }) it + listOf("") else it
        }
      }
      "attributes" -> {
        if (value == null || value !is Map<*, *>) return null
        val attributesLore = mutableListOf<String>()
        value.entries.forEach {
          attributesLore.add(I18nManager["lore.attribute", it.key, value[it.key]])
        }
        if (config.insertSpaceAfter.getOrPut(key) { true }) attributesLore.add("")
        return attributesLore
      }
      "enchants" -> {
        if (value == null || value !is Map<*, *>) return null
        val enchantsLore = mutableListOf<String>()
        value.entries.forEach {
          enchantsLore.add(I18nManager["lore.enchant_level", it.key, value[it.key]])
        }
        if (config.insertSpaceAfter.getOrPut(key) {true}) enchantsLore.add("")
        return enchantsLore
      }
      "type" -> {
        val type = value as? String ?: "item"
        val rarity = data["rarity"] as? String ?: "common"
        val rarityFormat = Rarities.display(rarity)
        val typeFormat = I18nManager["type.$type.format"]
        return listOf(I18nManager["type_format", typeFormat, rarityFormat])
      }
      "item_trade" -> {
        val slot = context["slot"] as? MenuButton ?: return listOf()
        var lore = slot.actions
          .asSequence()
          .filter { it.action == "trade_items" }
          .flatMap { it.data["price"] as? List<*> ?: listOf() }
          .filter { it != null }
          .map {
            if (it is LinkedTreeMap<*, *> || it is Map<*, *>) {
              "§8- §7" + it["id"].toString().plus(" §8[${it["count"].toString().toDoubleOrNull()?.toInt()?:1}]")
            } else
              "§8- §7" + it.toString().plus(" §8[1]")
          }
          .filter { it.isNotBlank() && it.isNotEmpty() }
          .toMutableList()
          .let {
            if (it.isNotEmpty())
              it.plus(" ").plus("§e§lCLICK §7to trade").toMutableList().apply {
                it.add(0, "§7Cost")
              }
            else
              it
          }
        if (lore.isEmpty()) return listOf()
        lore = mutableListOf("§7Cost").apply { addAll(lore) }
        return lore
      }
    }
    return null
  }

  override fun buildItemMaterial(data: Map<String, Any?>, context: Map<String, Any?>): Material? {
    return (data["material"] as? String)?.let {  Material.getMaterial(it) }
  }

  data class Config(
    var lore: Boolean = true,
    var attributes: Boolean = true,
    var enchants: Boolean = true,
    @SerializedName("insert_space_after")
    var insertSpaceAfter: MutableMap<String, Boolean> = mutableMapOf(
      "lore" to true,
      "attributes" to true,
      "enchants" to true
    ),
    @SerializedName("rounded_attributes")
    var roundedAttributes: List<String> = listOf(),
    @SerializedName("should_translate_items")
    var shouldTranslateItems: Boolean = true
  )

  object Rarities {
    fun getPrefix(rarity: String) = I18nManager["rarity.$rarity.prefix"]
    fun getSuffix(rarity: String) = I18nManager["rarity.$rarity.suffix"]
    fun getName(rarity: String) = I18nManager["rarity.$rarity.name"]
    fun display(rarity: String) = I18nManager["rarities.format", getName(rarity), getPrefix(rarity), getSuffix(rarity)]
  }
}