package fr.ping.gamemaker.items.builders.impl

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.builders.models.ItemBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.gamemaker.utils.adapter.ComponentTypeAdapter
import net.kyori.adventure.text.Component
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object BuiltinItemBuilder : ItemBuilder() {
  private val config : ConfigurationSection?
    get() = GameMakerPlugin.getInstance().config.getConfigurationSection("item-template.builtin")

  override fun buildItemLore(
    template: ItemTemplate,
    itemStack: ItemStack,
    key: String,
    value: JsonElement?,
    isKeyInConfig: Boolean,
    context: ItemBuilderContext
  ): List<Component>? {
    val lore = mutableListOf<Component>()
    when (key) {
      "lore" -> {
        if (config?.getBoolean("enable-lore", true) != true) return null
        if (value == null || value !is JsonArray) return null
        lore.addAll(value.map {
          if (it.asString.startsWith("$")) I18nManager[I18nManager.defaultLanguage, it.asString.removePrefix("$")]
          else Component.empty().append(ComponentTypeAdapter.parseComponent("<reset><!italic>${it.asString}"))
        })
      }
      "attributes" -> {
        if (config?.getBoolean("enable-attributes", true) != true) return null
        if (value == null || value !is JsonObject) return null
        lore.addAll(value.asMap().entries.map {
          I18nManager["lore.attribute", it.key, value[it.key]]
        })
      }
      "enchants" -> {
        if (config?.getBoolean("enable-enchants", true) != true) return null
        if (value == null || value !is JsonObject) return null
        lore.addAll(value.asMap().map {
          Component.text(
            I18nManager.getString("lore.enchant.${it.key}") +
                I18nManager.getString("lore.enchant_separator") +
                I18nManager.getString("lore.enchant_level", value[it.key])
          )
        })
      }
      "type" -> {
        val type = value?.asString ?: "item"
        val rarity = template.data["rarity"]?.asString ?: "common"
        val rarityFormat = Rarities.display(rarity)
        val typeFormat = I18nManager["type.$type.format"]
        if (value == null || template.data["rarity"] == null) return null
        return listOf(I18nManager["type_format", typeFormat, rarityFormat])
      }
      "item_trade" -> {
        //val slot = context["slot"] as? MenuButton ?: return listOf()
        //var lore = slot.actions
        //  .asSequence()
        //  .filter { it.action == "trade_items" }
        //  .flatMap { it.data["price"] as? List<*> ?: listOf() }
        //  .filter { it != null }
        //  .map {
        //    if (it is LinkedTreeMap<*, *> || it is Map<*, *>) {
        //      "§8- §7" + it["id"].toString().plus(" §8[${it["count"].toString().toDoubleOrNull()?.toInt()?:1}]")
        //    } else
        //      "§8- §7" + it.toString().plus(" §8[1]")
        //  }
        //  .filter { it.isNotBlank() && it.isNotEmpty() }
        //  .toMutableList()
        //  .let {
        //    if (it.isNotEmpty())
        //      it.plus(" ").plus("§e§lCLICK §7to trade").toMutableList().apply {
        //        it.add(0, "§7Cost")
        //      }
        //    else
        //      it
        //  }
        //if (lore.isEmpty()) return listOf()
        //lore = mutableListOf("§7Cost").apply { addAll(lore) }
        //return lore
        return null
      }
    }
    if (config?.getStringList("item-template.builtin.insert-space-after")?.contains(key) ?: false) lore.add(Component.empty())
    return lore
  }

  override fun buildItemMeta(
    template: ItemTemplate,
    itemStack: ItemStack,
    itemMeta: ItemMeta,
    context: ItemBuilderContext
  ): ItemMeta {
    if (template.customData["hide_flags"]?.asBoolean ?: false)
      itemMeta.itemFlags.addAll(ItemFlag.entries)
    if (template.customData["name"] != null)
      itemMeta.displayName(I18nManager.getComponentIfIndicator(I18nManager.defaultLanguage, template.customData["name"].asString))

    return itemMeta
  }

  object Rarities {
    fun getPrefix(rarity: String) = I18nManager["rarity.$rarity.prefix"]
    fun getSuffix(rarity: String) = I18nManager["rarity.$rarity.suffix"]
    fun getName(rarity: String) = I18nManager["rarity.$rarity.name"]
    fun display(rarity: String) = I18nManager["rarities.format", getName(rarity), getPrefix(rarity), getSuffix(rarity)]
  }
}