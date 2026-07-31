package fr.itspinguin.gamemaker.menus.models

import com.google.gson.annotations.SerializedName
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.resourcemanager.Resource
import fr.itspinguin.resourcemanager.WrappedResource

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