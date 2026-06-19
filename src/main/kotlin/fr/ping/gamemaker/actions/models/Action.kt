package fr.ping.gamemaker.actions.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.resource.MapResource
import org.bukkit.event.inventory.ClickType

@JsonAdapter(Action.Adapter::class)
class Action: MapResource() {
  var action: String = ""
  var criteria: List<Criterion>? = null
  @SerializedName("inventory_triggers")
  var inventoryTriggers : List<ClickType>? = null
  @SerializedName("interaction_triggers")
  var interactionTriggers : List<org.bukkit.event.block.Action>? = null
  @SerializedName("other_triggers")
  var otherTriggers : List<String>? = null

  class Adapter : GeneralAdapter<Action>(Action::class.java)

  override fun toString(): String {
    return "Action(data='$data')"
  }
}