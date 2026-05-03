package fr.ping.gamemaker.items.builders.impl

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.items.builders.models.ItemBuilder
import fr.ping.gamemaker.menus.models.MenuButton
import fr.ping.utils.resources.ResourceManager
import kotlin.collections.get

object BuiltinItemBuilder : ItemBuilder() {
  private val config : Config
    get() = GameMakerPlugin.getInstance().config.builtins.itemBuilder
  private val i18n by lazy {
    ResourceManager["item_build/en_US", I18n::class.java]
  }

  override fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>, context: Map<String, Any?>): List<String>? {
    when (key) {
      "lore" -> {
        if (value == null || value !is List<*>) return null
        @Suppress("UNCHECKED_CAST")
        return value.map { if (it.toString().startsWith($$"$t")) i18n?.resource?.translateAndInsert("lore_line", mapOf("lore" to it.toString().removePrefix($$"$t"))).toString() else it.toString() }.let {
          if (config.insertSpaceAfter.getOrPut(key) { true }) it + listOf("") else it
        }
      }
      "attributes" -> {
        if (value == null || value !is Map<*, *>) return null
        val attributesLore = mutableListOf<String>()
        value.entries.forEach {
          attributesLore.add(("§7" + i18n?.resource?.translateAndInsert("lore.attribute", mapOf(
            "attribute" to (i18n?.resource?.translate("attribute." + it.key) ?: ""),
            "value" to (value[it.key] as Double)
              .let { attributeValue -> if (config.roundedAttributes.contains(it.key)) attributeValue.toInt() else attributeValue }
          ))))
        }
        if (config.insertSpaceAfter.getOrPut(key) { true }) attributesLore.add("")
        return attributesLore
      }
      "enchants" -> {
        if (value == null || value !is Map<*, *>) return null
        val enchantsLore = mutableListOf<String>()
        value.entries.forEach {
          enchantsLore.add(i18n?.resource?.translateAndInsert(
            "lore.enchant",
            mapOf(
              "enchant" to i18n?.resource?.translate("enchant." + it.key),
              "level" to i18n?.resource?.translate("enchant_level." + (it.value as? Double)?.toInt().toString())
            )
          ) ?: "")
        }
        if (config.insertSpaceAfter.getOrPut(key) {true}) enchantsLore.add("")
        return enchantsLore
      }
      "type" -> {
        val type = value as? String ?: "item"
        val rarity = data["rarity"] as? String ?: "common"
        val rarityFormat = Rarities.display(rarity)
        val typeFormat = i18n?.resource?.translate("type.$type.format") ?: "type format {*}"
        return listOf(i18n?.resource?.translateAndInsert("type_format", mapOf("type" to typeFormat, "rarity" to rarityFormat)) ?: "")
      }
      "item_trade" -> {
        val slot = context["slot"] as? MenuButton ?: return listOf()
        return slot.actions
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
              it.plus(" ").plus("§e§lCLICK §7to trade")
            else
              it
          }
      }
    }
    return null
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
    fun getPrefix(rarity: String) = i18n?.resource?.translate("rarity.$rarity.prefix") ?: "§c"

    fun getSuffix(rarity: String) = i18n?.resource?.translate("rarity.$rarity.suffix") ?: "§c♥"

    fun getName(rarity: String) = i18n?.resource?.translate("rarity.$rarity.name") ?: "Rarity"

    fun display(rarity: String) = i18n?.resource?.translateAndInsert("rarities.format", mapOf(
      "rarity" to getName(rarity),
      "prefix" to getPrefix(rarity),
      "suffix" to getSuffix(rarity)
    ))
  }
}