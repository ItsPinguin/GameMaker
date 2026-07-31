package fr.itspinguin.gamemaker.criteria.models

import com.google.gson.annotations.JsonAdapter
import fr.itspinguin.gamemaker.resource.MapResource

@JsonAdapter(Criterion.Adapter::class)
class Criterion : MapResource() {
  var criterion: String
    get() = data["criterion"] as? String ?: "no_criterion"
    set(value) { data.addProperty("criterion", value) }

  class Adapter : GeneralAdapter<Criterion>(Criterion::class.java)

  override fun toString(): String {
    return "Criterion(data='$data')"
  }


}