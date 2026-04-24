package fr.ping.gamemaker.builtin.hook.criteria

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.addon.CriteriaCheckerHook
import fr.ping.gamemaker.resource.Criterion
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemCriterionCheckerHook: CriteriaCheckerHook {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    if (criterion.criterion != "item") return true
    val item = (context["item"] as? ItemStack) ?: (context["player"] as? Player)?.inventory?.itemInMainHand ?: return false
    val expectedItem = criterion.data["item"] as? String ?: return false
    val itemId = item.itemMeta.persistentDataContainer.get(NamespacedKey("gamemaker", "item"), PersistentDataType.STRING) ?: return false
    GameMakerPlugin.getInstance().logger.info("ItemCriterionCheckerHook: received item: $item, expected item: $expectedItem, itemId: $itemId")
    return expectedItem == itemId
  }
}