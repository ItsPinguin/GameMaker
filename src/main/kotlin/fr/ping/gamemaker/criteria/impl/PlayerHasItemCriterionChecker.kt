package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.criteria.CriteriaChecker
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.items.ItemManager
import org.bukkit.entity.Player

object PlayerHasItemCriterionChecker : CriteriaChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    GameMakerPlugin.getInstance().logger.info("PlayerHasItemCriterionChecker: received criterion: $criterion, context: $context")
    if (criterion.criterion != "player_has_items") {
      GameMakerPlugin.getInstance().logger.info("PlayerHasItemCriterionChecker: criterion not player_has_items, returning true")
      return true
    }
    val player = context["player"] as? Player ?: let {
      GameMakerPlugin.getInstance().logger.info("PlayerHasItemCriterionChecker: no player in context, returning false")
      return false
    }
    val items = criterion.data["items"] as? List<*> ?: let {
      GameMakerPlugin.getInstance().logger.info("PlayerHasItemCriterionChecker: no items in criterion, returning true")
      return true
    }

    val playerItems = mutableMapOf<String, Int>()
    player.inventory.forEach { item ->
      val id = ItemManager.getItemId(item) ?: return@forEach
      playerItems[id] = playerItems.getOrDefault(id, 0) + item.amount
    }

    val requiredItems = mutableMapOf<String, Int>()
    items.forEach { item ->
      if (item is String) {
        requiredItems[item] = requiredItems.getOrDefault(item, 0) + 1
      }
      if (item is Map<*, *>) {
        if (item["id"] !is String) return@forEach
        val id = item["id"] as String
        requiredItems[id] = (requiredItems.getOrDefault(id, 0) + (item["count"].toString().toDoubleOrNull()?.toInt() ?: 1))
      }
    }

    println("playerItems: $playerItems \nrequiredItems: $requiredItems")
    return playerItems.all { it.value >= requiredItems[it.key]!! }
  }
}