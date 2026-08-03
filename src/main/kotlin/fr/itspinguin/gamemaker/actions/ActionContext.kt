package fr.itspinguin.gamemaker.actions

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.gamemaker.menus.models.MenuInstance
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent

sealed class ActionContext(
  open val metadata: JsonObject = JsonObject()
) {
  data class GenericActionContext(
    override val metadata: JsonObject = JsonObject()
  ) : ActionContext(metadata)

  open class PlayerActionContext(
    open val player: Player
  ) : ActionContext()

  data class MenuClickActionContext(
    override val player: Player,
    val menu : MenuInstance,
    val event : InventoryClickEvent
  ) : PlayerActionContext(player)

  data class ItemInteractActionContext(
    override val player: Player,
    val item : ItemTemplate,
    val event : PlayerInteractEvent
  ) : PlayerActionContext(player)
}