package fr.ping.gamemaker.addons

interface ItemHandlerHook : AddonHook {
  fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>): List<String>?

  fun deserializeItem(key: String, value: Any?): Any?
}