package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.criteria.CriteriaManager
import fr.ping.gamemaker.criteria.CriterionChecker
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.utils.resources.ResourceManager

object AnyOfCriterionChecker : CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    if (criterion.criterion != "any_of") return true
    val criteria : List<Criterion> =
      if ((criterion.data["criteria"] as? List<*>)?.all { it is Map<*, *> } == false)
        ResourceManager.parseAny<List<Criterion>>(criterion.data["criteria"]) ?: listOf()
      else
        criterion.data["criteria"] as List<Criterion>

    return criteria.any { CriteriaManager.checkCriterion(it, context) }
  }
}