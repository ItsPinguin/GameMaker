package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.CriteriaManager
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.items.ItemManager
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Sound

object GiveItemsAction: ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val criteria = action.data["criteria"] as? List<*> ?: mutableListOf<String>()
    val items = action.data["items"] as? List<*> ?: return

    val parsedCriteria = criteria.map { ResourceManager.getGson().fromJson(
      ResourceManager.getGson().toJson(it), Criterion::class.java) }
    if (!CriteriaManager.checkCriteria(parsedCriteria, context.metadata)) {
      if (!(action.data["ignore_criteria"].toString().toBooleanStrictOrNull() ?: true)) {
        context.player.sendMessage("§cYou didn't meet the criteria to give the item.")
      }
      return
    }

    if (items.isEmpty()) return

    val itemMap = mutableMapOf<String, Int>()
    items.forEach { item ->
      if (item is String) {
        val itemStack = ItemManager.buildItem(item)
        context.player.inventory.addItem(itemStack)
        itemMap[item] = itemMap.getOrDefault(item, 0) + 1
      }
      if (item is Map<*, *>) {
        val itemStack = ItemManager.buildItem(item["id"] as? String ?: return)
        val count = item["count"].toString().toDoubleOrNull()?.toInt() ?: 1
        context.player.inventory.addItem(itemStack.apply { amount = count })
        itemMap[item["id"] as? String ?: return] = itemMap.getOrDefault(item["id"] as? String ?: return, 0) + count
      }
    }

    itemMap.forEach { (key, value) ->
      context.player.sendMessage(" §8+ §f$key §8[$value]")
    }
    context.player.playSound(context.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
  }
}