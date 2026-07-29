package fr.ping.gamemaker.items.templates.models

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import fr.ping.gamemaker.GameMakerPlugin
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