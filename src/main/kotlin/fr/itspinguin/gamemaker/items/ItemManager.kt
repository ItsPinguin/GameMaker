package fr.itspinguin.gamemaker.items

import com.google.gson.Gson
import fr.itspinguin.gamemaker.GameMakerPlugin
import fr.itspinguin.gamemaker.GameMakerPlugin.Companion.itemBuilderRegistry
import fr.itspinguin.gamemaker.items.builders.impl.BuiltinItemBuilder
import fr.itspinguin.gamemaker.items.builders.impl.BuiltinItemTemplateItemListBuilder
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
  fun buildItem(id: String, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) =
    buildItem(GameMakerPlugin.itemTemplateRegistry.getResource(id), context)

  fun buildItem(template: ItemTemplate?, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) : ItemStack {
    if (template == null) return ItemStack(Material.AIR)
    val componentString = template.components.asMap()
      .map { "${it.key}=${Gson().toJson(it.value)}" }
      .toMutableList().apply {
        addAll(
          template.removedComponents.map { "!$it" }
        )
      }
      .joinToString(",")
    val itemStack = try {
      Bukkit.getItemFactory().createItemStack("${template.material.lowercase()}[${componentString}]")
    } catch (e: Exception) {
      try {
        Bukkit.getItemFactory().createItemStack(template.material.lowercase())
      } catch (e: Exception) {
        return ItemStack(Material.AIR)
      }
    }
    itemStack.amount = template.amount
    if (itemStack.type == Material.AIR) return itemStack
    var itemMeta = itemStack.itemMeta ?: return itemStack

    itemBuilderRegistry.listResources().forEach { builder ->
      itemMeta = builder.buildItemMeta(template, itemStack, itemMeta, context)
    }

    val unorderedKeys = template.customData.asMap().keys.filter { it !in GameMakerPlugin.getInstance().config.getStringList("item-template.lore-order") }
    val builders = itemBuilderRegistry.resourceMap.toMutableMap()
    val lore = mutableListOf<Component>()
    GameMakerPlugin.getInstance().config.getStringList("item-template.lore-order").forEach { propertyName ->
      builders.values.forEach { builder ->
        lore.addAll(builder.resource?.buildItemLore(template, itemStack, propertyName, template.data[propertyName], true, context) ?: listOf())
      }
    }
    unorderedKeys.forEach {
      builders.values.forEach { builder ->
        lore.addAll(builder.resource?.buildItemLore(template, itemStack, it, template.data[it], false, context) ?: listOf())
      }
    }
    if (lore.size == 1 && lore.firstOrNull()?.equals(Component.empty()) ?: false)
      lore.clear()
    else
      while (lore.lastOrNull()?.equals(Component.empty()) ?: false) {
        lore.removeAt(lore.lastIndex)
      }
    itemMeta.lore(lore)

    itemMeta.persistentDataContainer.set(NamespacedKey("gamemaker", "id"), PersistentDataType.STRING,
      template.id)

    itemStack.itemMeta = itemMeta

    return itemStack
  }

  fun buildItem(item: ItemStack, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) : ItemStack {
    return getItemId(item)?.let { buildItem(it, context) } ?: ItemStack(Material.AIR)
  }

  operator fun get(item: ItemStack, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) = buildItem(item, context)

  operator fun get(id: String, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) = buildItem(id, context)

  operator fun get(template: ItemTemplate, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) = buildItem(template, context)

  fun getItemId(item: ItemStack?) : String? {
    return item?.itemMeta?.persistentDataContainer?.get(NamespacedKey("gamemaker", "id"), PersistentDataType.STRING)
  }

  init {
    itemBuilderRegistry.registerResource("builtin_builder", BuiltinItemBuilder)
    GameMakerPlugin.itemListBuilderRegistry.registerResource("item_templates", BuiltinItemTemplateItemListBuilder)
  }
}