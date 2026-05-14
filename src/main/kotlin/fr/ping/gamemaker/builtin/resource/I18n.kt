package fr.ping.gamemaker.builtin.resource

import fr.ping.utils.resources.Resource

class I18n(
  var locale: String,
  val translations: MutableMap<String, Any?> = mutableMapOf()
) : Resource()