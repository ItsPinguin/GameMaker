package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.gamemaker.items.ItemManager
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.resourcemanager.ResourceManager
import org.bukkit.Sound

object GiveItemsAction: ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val criteria = ResourceManager.parseJson<List<Criterion>>(action.data["criteria"]) ?: listOf()
    val items = ResourceManager.parseJson<List<ItemTemplate>>(action.data["items"]) ?: listOf()

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
      itemMap[item.id] = itemMap.getOrDefault(item.id, 0) + 1
      val itemStack = ItemManager.buildItem(item)
      context.player.inventory.addItem(itemStack)
    }

    itemMap.forEach { (key, value) ->
      context.player.sendMessage(" §8+ §f$key §8[$value]")
    }
    context.player.playSound(context.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
  }
}