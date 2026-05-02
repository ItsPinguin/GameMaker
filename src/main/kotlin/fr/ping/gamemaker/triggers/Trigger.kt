package fr.ping.gamemaker.triggers

import com.google.gson.annotations.JsonAdapter
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.resource.MapResource

@JsonAdapter(Trigger.Adapter::class)
class Trigger : MapResource() {
  var trigger: String
    get() = data["trigger"] as? String ?: "no_trigger"
    set(value) { data["trigger"] = value }

  var criteria: List<Criterion>? = null
    get() {
      if (field == null) {
        if (data["criteria"] !is List<*>) {
          field = listOf()
        } else {
          val toConvert = data["criteria"] as List<*>
          field = toConvert.map { Criterion().apply {
            if (it is Map<*, *>)
              @Suppress("UNCHECKED_CAST")
              this.data = it.toMutableMap() as MutableMap<String, Any?>
          } }
        }
      }
      return field
    }
    set(value) {
      field = value
      data["criteria"] = value
    }

  var actions: List<Action>? = null
    get() {
      if (field == null) {
        if (data["actions"] !is List<*>) {
          field = listOf()
        } else {
          val toConvert = data["actions"] as List<*>
          field = toConvert.map {
            Action().apply {
              if (it is Map<*, *>)
                @Suppress("UNCHECKED_CAST")
                this.data = it.toMutableMap() as MutableMap<String, Any?>
            }
          }
        }
      }
      return field
    }
    set(value) {
      field = value
      data["actions"] = value
    }

  class Adapter : GeneralAdapter<Trigger>(Trigger::class.java)

  override fun toString(): String {
    return "Trigger(data='$data')"
  }

}