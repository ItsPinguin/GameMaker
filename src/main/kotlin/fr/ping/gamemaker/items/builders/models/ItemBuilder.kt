package fr.ping.gamemaker.items.builders.models

import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.utils.resources.Resource
import org.bukkit.Material

abstract class ItemBuilder : Resource() {
  abstract fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>, context: ItemBuilderContext, isKeyInConfig: Boolean = true): List<String>?

  abstract fun buildItemMaterial(data: Map<String, Any?>, context: ItemBuilderContext): Material?
}