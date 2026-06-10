package fr.ping.gamemaker.items.builders.models

import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.utils.resources.Resource
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

abstract class ItemBuilder : Resource() {
  abstract fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>, context: ItemBuilderContext, isKeyInConfig: Boolean = true): List<Component>?

  abstract fun buildItemMaterial(data: Map<String, Any?>, context: ItemBuilderContext): Material?

  abstract fun buildItemMeta(itemMeta: ItemMeta, data: Map<String, Any?>, context: ItemBuilderContext): ItemMeta
}