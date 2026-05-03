package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.CriteriaManager
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.items.ItemManager
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Sound
import org.bukkit.entity.Player
import kotlin.collections.mutableListOf

object TakeItemsAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "take_items") return
    val player = context["player"] as? Player ?: let {
      GameMakerPlugin.getInstance().logger.info("TakeItemsAction: no player in context, context: $context")
      return
    }
    val criteria = action.data["criteria"] as? List<*> ?: mutableListOf<String>()
    val items = action.data["items"] as? List<*> ?: return

    val parsedCriteria = criteria.map { ResourceManager.getGson().fromJson(it.toString(), Criterion::class.java) }
    if (!CriteriaManager.checkCriteria(parsedCriteria, context)) {
      player.sendMessage("§cYou didn't meet the criteria to give the item.")
      return
    }

    if (items.isEmpty()) return

    val itemsToTake = mutableMapOf<String, Int>()
    items.forEach { item ->
      if (item is String) {
        itemsToTake[item] = itemsToTake.getOrDefault(item, 0) + 1
      }
      if (item is Map<*, *>) {
        if (item["id"] !is String) return@forEach
        itemsToTake[item["id"] as String] = (itemsToTake.getOrDefault(item["id"] as String, 0) + (item["count"].toString().toDoubleOrNull()?.toInt() ?: 1))
      }
    }

    val ownedItems = mutableMapOf<String, Int>()
    player.inventory.forEach { item ->
      val id = ItemManager.getItemId(item) ?: return@forEach
      if (!itemsToTake.containsKey(id)) return@forEach
      ownedItems[id] = ownedItems.getOrDefault(id, 0) + item.amount
    }

    val hasAllItems = itemsToTake.all { (ownedItems[it.key] ?: 0) >= it.value }

    if (!hasAllItems) {
      player.sendMessage("§cYou don't have enough items in your inventory!")
      player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.1f)
      return
    }

    itemsToTake.forEach { (id, amount) ->
      player.sendMessage(" §c- §f$id §8[$amount]")
    }

    player.inventory.forEach { item ->
      val id = ItemManager.getItemId(item) ?: return@forEach
      if (!itemsToTake.containsKey(id)) return@forEach
      if (itemsToTake[id]!! <= 0) return@forEach
      val takenAmount = minOf(item.amount, itemsToTake[id]!!)
      item.amount -= takenAmount
      itemsToTake[id] = itemsToTake[id]!! - takenAmount
    }

    player.updateInventory()
  }
}