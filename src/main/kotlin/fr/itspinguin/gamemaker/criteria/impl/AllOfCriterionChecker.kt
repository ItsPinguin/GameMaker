package fr.itspinguin.gamemaker.criteria.impl

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.ResourceManager

object AllOfCriterionChecker : CriterionChecker() {
  override fun check(
    criterion: Criterion,
    context: JsonObject
  ): Boolean {
    return criterion.criterion != "all_of" || CriteriaManager.checkCriteria(
      ResourceManager.parseJson<List<Criterion>>(criterion.data["criteria"]) ?: listOf(),
      context)
  }
}