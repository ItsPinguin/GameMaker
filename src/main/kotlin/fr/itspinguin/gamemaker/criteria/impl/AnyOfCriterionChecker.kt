package fr.itspinguin.gamemaker.criteria.impl

import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.ResourceManager

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