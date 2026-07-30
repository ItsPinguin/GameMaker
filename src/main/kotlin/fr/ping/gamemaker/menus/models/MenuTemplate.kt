package fr.ping.gamemaker.menus.models

import com.google.gson.annotations.SerializedName
import fr.ping.utils.resources.Resource
import org.bukkit.event.inventory.InventoryType

data class MenuTemplate(
  var title : String = "",
  @SerializedName("inventory_type")
  var inventoryType : InventoryType = InventoryType.CHEST,
  var rows : Int = 3,
  var contents : MutableList<MenuButton> = mutableListOf(),
  @SerializedName("is_static")
  var isStatic : Boolean = true,
  @SerializedName("cancel_by_default")
  var cancelByDefault : Boolean = true
) : Resource(){
  fun getButton(index: Int) = contents.filter { it.getFilledSlots().contains(index) }.let { buttons ->
    buttons.firstOrNull { it.actions.isNotEmpty() } ?: buttons.firstOrNull() ?: MenuButton()
  }
}