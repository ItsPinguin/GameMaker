package fr.ping.gamemaker.actions.models

import com.google.gson.annotations.JsonAdapter
import fr.ping.gamemaker.resource.MapResource

@JsonAdapter(Action.Adapter::class)
class Action: MapResource() {
  var action: String
    get() = data["action"] as? String ?: "no_action"
    set(value) { data["action"] = value }

  class Adapter : GeneralAdapter<Action>(Action::class.java)

  override fun toString(): String {
    return "Action(data='$data')"
  }


}