package fr.ping.gamemaker.actions.impl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.utils.resources.ResourceManager

object TradeItemsAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val items = action.data["items"]?.asJsonArray ?: JsonArray()
    val price = action.data["price"]?.asJsonArray ?: JsonArray()
    val criteria = action.data["criteria"]?.asJsonArray ?: JsonArray()
    GiveItemsAction.execute(
      Action().apply {
        this.action = "give_items"
        data.add("items", items)
        data.add("criteria", criteria.apply {
          add(JsonObject().apply {
            addProperty("criterion", "player_has_items")
            add("items", price)
          })
        })
      },
      ActionContext.PlayerActionContext(context.player)
    )
    TakeItemsAction.execute(
      Action().apply {
        this.action = "take_items"
        data.add("items", price)
        data.add("criteria", criteria)
      },
      ActionContext.PlayerActionContext(context.player)
    )

  }
}