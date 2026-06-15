package fr.ping.gamemaker.menus.models

import com.google.gson.annotations.SerializedName
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.utils.resources.Resource
import fr.ping.utils.resources.WrappedResource

data class MenuButton(
  var item : WrappedResource<ItemTemplate>? = null,
  var slots : List<SlotFillInfo> = listOf(),
  var actions : List<Action> = listOf(),
  var cancel : Boolean? = null,
  var context : MutableMap<String, Any?> = mutableMapOf(),
  var list : String? = null,
  @SerializedName("page_offset")
  var pageOffset : Int? = null,
  @SerializedName("update_later")
  var updateLater : List<Long> = listOf()
) : Resource() {
  fun getFilledSlots() : List<Int> {
    return slots.flatMap { it.getSlots() }
  }
}