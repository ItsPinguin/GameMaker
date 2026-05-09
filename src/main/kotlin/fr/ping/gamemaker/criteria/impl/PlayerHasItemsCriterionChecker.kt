package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.criteria.CriterionChecker
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.items.ItemManager
import org.bukkit.entity.Player

object PlayerHasItemsCriterionChecker : CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    if (criterion.criterion != "player_has_items")
      return true
    val player = context["player"] as? Player ?: let {
      GameMakerPlugin.getInstance().logger.warning("[Criteria] Useless criterion: player_has_items, no player in context: $context.")
      return false
    }
    val items = criterion.data["items"] as? List<*> ?: let {
      GameMakerPlugin.getInstance().logger.warning("[Criteria] Useless criterion: player_has_items, no items in data")
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

    return requiredItems.all {
      (playerItems[it.key] ?: 0) >= it.value
    }
  }
}