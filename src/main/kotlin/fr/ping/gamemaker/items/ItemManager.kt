package fr.ping.gamemaker.items

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.GameMakerPlugin.Companion.itemBuilderRegistry
import fr.ping.gamemaker.items.builders.impl.BuiltinItemBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
  fun buildItem(id: String, context: Map<String, Any?> = mutableMapOf()) =
    buildItem(GameMakerPlugin.itemRegistry.getResource(id), context)

  fun buildItem(template: ItemTemplate?, context: Map<String, Any?> = mutableMapOf()) : ItemStack {
    val itemStack = ItemStack(template?.material ?: Material.AIR)
    if (template == null || itemStack.type == Material.AIR) return itemStack
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.setDisplayName(template.name)

    val builders = GameMakerPlugin.itemBuilderRegistry.resourceMap.toMutableMap()
    val lore = mutableListOf<String>()
    GameMakerPlugin.getInstance().config.itemLoreOrder.forEach { propertyName ->
      builders.values.forEach { builder ->
        lore.addAll(builder.resource?.buildItemLore(propertyName, template.data[propertyName], context) ?: listOf())
      }
    }
    if (lore.size == 1 && lore.firstOrNull() == "")
      lore.clear()
    else
      while (lore.lastOrNull() == "") {
        lore.removeAt(lore.lastIndex)
      }
    itemMeta.lore = lore
    itemMeta.addItemFlags(*ItemFlag.entries.toTypedArray())
    itemMeta.isUnbreakable = true

    itemMeta.persistentDataContainer.set(NamespacedKey("gamemaker", "item"), PersistentDataType.STRING, template.id)

    itemStack.itemMeta = itemMeta

    return itemStack
  }

  init {
    itemBuilderRegistry.registerResource("builtin_builder", BuiltinItemBuilder)
  }
}