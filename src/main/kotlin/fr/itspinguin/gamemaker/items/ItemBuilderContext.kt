package fr.itspinguin.gamemaker.items

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.menus.models.MenuButton
import fr.itspinguin.gamemaker.menus.models.MenuInstance
import org.bukkit.entity.Player

sealed class ItemBuilderContext(
  open val metadata : JsonObject = JsonObject()
) {
  open class GenericItemBuilderContext(
    override val metadata : JsonObject = JsonObject()
  ) : ItemBuilderContext(metadata)

  class MenuSlotItemBuilderContext(
    override val metadata : JsonObject = JsonObject(),
    val menuInstance : MenuInstance,
    val actualSlot : Int,
    val index : Int,
    val menuButton : MenuButton,
    val player : Player
  ) : GenericItemBuilderContext(metadata)
}