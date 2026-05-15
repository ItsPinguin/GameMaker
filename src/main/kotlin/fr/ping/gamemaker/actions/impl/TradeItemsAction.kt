package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import org.bukkit.entity.Player

object TradeItemsAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val items = action.data["items"] as? List<*> ?: listOf<Any?>()
    val price = action.data["price"] as? List<*> ?: listOf<Any?>()
    val criteria = action.data["criteria"] as? List<*> ?: listOf<Any?>()
    GiveItemsAction.execute(
      Action().apply {
        this.action = "give_items"
        data["items"] = items
        data["criteria"] = criteria.toMutableList().apply {
          add(
            mutableMapOf<String, Any?> (
              "criterion" to "player_has_items",
              "items" to price
            )
          )
        }
      },
      ActionContext.PlayerActionContext(context.player)
    )
    TakeItemsAction.execute(
      Action().apply {
        this.action = "take_items"
        data["items"] = price
        data["criteria"] = criteria
      },
      ActionContext.PlayerActionContext(context.player)
    )

  }
}