package fr.ping.gamemaker.resource

import com.google.gson.annotations.JsonAdapter

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