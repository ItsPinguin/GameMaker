package fr.itspinguin.gamemaker.criteria.impl

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemCriterionCheckerHook: CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: JsonObject
  ): Boolean {
    if (criterion.criterion != "item") return true
    val item = context["item"]?.asString ?: return false
    val expectedItem = criterion.data["item"]?.asString ?: return false
    return item == expectedItem
  }
}