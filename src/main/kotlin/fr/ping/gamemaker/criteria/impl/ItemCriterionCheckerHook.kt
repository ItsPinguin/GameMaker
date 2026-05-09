package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.criteria.CriterionChecker
import fr.ping.gamemaker.criteria.models.Criterion
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemCriterionCheckerHook: CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    if (criterion.criterion != "item") return true
    val item = (context["item"] as? ItemStack) ?: (context["player"] as? Player)?.inventory?.itemInMainHand ?: return false
    val expectedItem = criterion.data["item"] as? String ?: return false
    val itemId = item.itemMeta.persistentDataContainer.get(NamespacedKey("gamemaker", "item"), PersistentDataType.STRING) ?: return false
    return expectedItem == itemId
  }
}