package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Player

object TradeItemsAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "trade_items") return
    val player = context["player"] as? Player ?: return
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
      mutableMapOf(
        "player" to player,
      )
    )
    println("price: $price\nitems: $items\ncriteria: $criteria\nnew criteria: ${
      criteria.toMutableList().apply { 
        add(
          mutableMapOf<String, Any?> (
            "criterion" to "player_has_items",
            "items" to price
          )
        )
      }
    }")
    TakeItemsAction.execute(
      Action().apply {
        this.action = "take_items"
        data["items"] = price
        data["criteria"] = criteria
      },
      mutableMapOf(
        "player" to player,
      )
    )

  }
}