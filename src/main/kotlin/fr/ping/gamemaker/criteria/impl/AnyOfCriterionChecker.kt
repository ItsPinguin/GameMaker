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
    return criterion.criterion != "any_of" ||
        (ResourceManager.parseAny<List<Criterion>>(criterion.data["criteria"]) ?: listOf())
          .any { CriteriaManager.checkCriterion(it, context) }
  }
}