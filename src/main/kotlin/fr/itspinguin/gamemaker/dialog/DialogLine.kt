package fr.itspinguin.gamemaker.dialog

import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import fr.itspinguin.gamemaker.GameMakerPlugin
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.gamemaker.dialog.DialogLine.Adapter
import fr.itspinguin.resourcemanager.ResourceManager

@JsonAdapter(Adapter::class)
data class DialogLine(
  var text: String? = null,
  var actions: List<Action>? = null,
  var criteria: List<Criterion>? = null,
  var cooldown: Double? = null,
  var step: Int? = null,

  @SerializedName("should_use_chat")
  var useChat : Boolean? = null,
  @SerializedName("should_use_title")
  var useTitle: Boolean? = null,
  @SerializedName("should_use_action_bar")
  var useActionBar: Boolean? = null
) {
  class Adapter : TypeAdapter<DialogLine>() {
    override fun write(
      out: JsonWriter?,
      value: DialogLine?
    ) {
      if (value == null) return
      val data = mutableMapOf<String, Any?>()
      value.text.let { data["text"] = it }
      value.actions?.let { data["actions"] = it }
      value.criteria?.let { data["criteria"] = it }
      value.cooldown?.let { data["cooldown"] = it }
      value.step?.let { data["step"] = it }
      value.useTitle?.let { data["should_use_title"] = it }
      value.useActionBar?.let { data["should_use_action_bar"] = it }
      if (data.size == 1 && data.containsKey("text"))
        out?.value(value.text)
      else
        out?.jsonValue(GameMakerPlugin.gson.toJson(data))
    }

    override fun read(`in`: JsonReader?): DialogLine {
      if (`in` == null) return DialogLine()
      when (`in`.peek()) {
        JsonToken.STRING -> {
          return DialogLine(text = `in`.nextString())
        }
        JsonToken.BEGIN_OBJECT -> {
          val data = GameMakerPlugin.gson.fromJson<JsonObject>(`in`, JsonObject::class.java)
          return DialogLine(
            text = data["text"]?.asString ?: "",
            actions = ResourceManager.parseJson<List<Action>>(data["actions"]),
            criteria = ResourceManager.parseJson<List<Criterion>>(data["criteria"]),
            cooldown = data["cooldown"]?.asDouble ?: 0.0,
            step = data["step"]?.asInt ?: 1,
            useTitle = data["should_use_title"]?.asBoolean ?: false,
            useActionBar = data["should_use_action_bar"]?.asBoolean ?: false
          )
        }
        else -> return DialogLine()
      }
    }
  }
}