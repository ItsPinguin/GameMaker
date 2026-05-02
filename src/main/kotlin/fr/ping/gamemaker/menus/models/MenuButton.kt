package fr.ping.gamemaker.menus.models

import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.utils.resources.Resource
import fr.ping.utils.resources.WrappedResource

data class MenuButton(
  var item : WrappedResource<ItemTemplate>? = null,
  var slots : List<SlotFillInfo> = listOf(),
  var actions : List<Action> = listOf(),
  var cancel : Boolean? = null
) : Resource() {
  fun getFilledSlots() : List<Int> {
    return slots.flatMap { it.getSlots() }
  }
}