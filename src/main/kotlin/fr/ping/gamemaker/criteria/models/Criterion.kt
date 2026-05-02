package fr.ping.gamemaker.criteria.models

import com.google.gson.annotations.JsonAdapter
import fr.ping.gamemaker.resource.MapResource

@JsonAdapter(Criterion.Adapter::class)
class Criterion : MapResource() {
  var criterion: String
    get() = data["criterion"] as? String ?: "no_criterion"
    set(value) { data["criterion"] = value }

  class Adapter : GeneralAdapter<Criterion>(Criterion::class.java)

  override fun toString(): String {
    return "Criterion(data='$data')"
  }


}