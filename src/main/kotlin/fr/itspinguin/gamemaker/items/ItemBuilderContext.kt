package fr.itspinguin.gamemaker.items

import fr.itspinguin.gamemaker.menus.models.MenuButton
import fr.itspinguin.gamemaker.menus.models.MenuInstance
import org.bukkit.entity.Player

sealed class ItemBuilderContext(
  open val metadata : Map<String, Any?> = mapOf()
) {
  open class GenericItemBuilderContext(
    override val metadata : Map<String, Any?> = mapOf()
  ) : ItemBuilderContext(metadata)

  class MenuSlotItemBuilderContext(
    override val metadata : Map<String, Any?> = mapOf(),
    val menuInstance : MenuInstance,
    val actualSlot : Int,
    val index : Int,
    val menuButton : MenuButton,
    val player : Player
  ) : GenericItemBuilderContext(metadata)
}