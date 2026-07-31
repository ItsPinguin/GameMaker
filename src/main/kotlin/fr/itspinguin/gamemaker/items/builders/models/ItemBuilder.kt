package fr.itspinguin.gamemaker.items.builders.models

import com.google.gson.JsonElement
import fr.itspinguin.gamemaker.items.ItemBuilderContext
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.resourcemanager.Resource
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

abstract class ItemBuilder : Resource() {
  /**
   * Builds the description for a given value of a key in the item template's custom data.
   *
   * @param template The item template containing the metadata and configuration for the item.
   * @param itemStack The specific item stack to which the lore will be applied.
   * @param key The key used to determine the lore entry from the template's data.
   * @param value The value associated with the key, which may be used to customize the lore content.
   * @param isKeyInConfig Determines if the provided key is part of the global configuration order.
   *                      Defaults to `true`.
   * @param context The context in which the item is being built, providing additional metadata and
   *                environmental information.
   * @return A list of components that will be added to the item's lore or `null` if no lore is to be added.
   */
  abstract fun buildItemLore(
    template : ItemTemplate,
    itemStack : ItemStack,
    key: String,
    value: JsonElement?,
    isKeyInConfig: Boolean = true,
    context: ItemBuilderContext
  ): List<Component>?

  /**
   * Builds and customizes the item metadata for a given item template and stack.
   *
   * @param template The item template containing metadata, configurations, and custom data
   *                 that defines the item.
   * @param itemStack The specific item stack that represents the physical item being built.
   * @param itemMeta The metadata object of the item stack that will be modified or overridden.
   * @param context The context in which the item is being constructed, which may provide
   *                additional metadata or application-specific information.
   * @return The modified or newly created item metaobject to be associated with the item stack.
   */
  abstract fun buildItemMeta(
    template: ItemTemplate,
    itemStack: ItemStack,
    itemMeta: ItemMeta,
    context: ItemBuilderContext
  ): ItemMeta
}