package fr.ping.gamemaker.items.templates.models

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import fr.ping.utils.resources.Resource

open class ItemTemplate(
  @SerializedName("components", alternate = ["component", "nbt", "nbt_compound", "NBT"])
  val components : JsonObject = JsonObject(),
  var material : String = "air",
  var amount : Int = 1,
  @SerializedName("custom_data", alternate = ["data", "customData"])
  val customData : JsonObject = JsonObject(),
  @SerializedName("removed_components", alternate = ["removedComponent", "removedComponents"])
  val removedComponents : MutableList<String> = mutableListOf()
) : Resource() {
  val data : Map<String, JsonElement>
    get() = customData.asMap()
}