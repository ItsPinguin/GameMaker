package fr.ping.gamemaker.items.builders.models

import fr.ping.utils.resources.Resource

abstract class ItemBuilder : Resource() {
  abstract fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>): List<String>?
}