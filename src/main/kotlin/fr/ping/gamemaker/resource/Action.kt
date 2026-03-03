package fr.ping.gamemaker.resource

import com.google.gson.annotations.JsonAdapter

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