package fr.ping.gamemaker.items

import fr.ping.gamemaker.menus.models.MenuButton
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.gamemaker.menus.models.SlotFillInfo
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