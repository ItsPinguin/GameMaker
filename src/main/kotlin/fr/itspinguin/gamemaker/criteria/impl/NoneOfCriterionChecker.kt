package fr.itspinguin.gamemaker.criteria.impl

import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.ResourceManager

object NoneOfCriterionChecker : CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    return criterion.criterion != "none_of" ||
        (ResourceManager.parseAny<List<Criterion>>(criterion.data["criteria"]) ?: listOf())
          .none { CriteriaManager.checkCriterion(it, context) }
  }

}