package fr.itspinguin.gamemaker.i18n

import fr.itspinguin.resourcemanager.Resource

class I18n(
  var locale: String,
  val translations: MutableMap<String, Any?> = mutableMapOf()
) : Resource()