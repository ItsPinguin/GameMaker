package fr.itspinguin.gamemaker.criteria.impl

import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.ResourceManager

object AllOfCriterionChecker : CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    return criterion.criterion != "all_of" || CriteriaManager.checkCriteria(
      ResourceManager.parseAny<List<Criterion>>(criterion.data["criteria"]) ?: listOf(),
      context)
  }
}