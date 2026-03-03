package fr.ping.gamemaker.addon

interface ItemHandlerHook : AddonHook {
  fun buildItemLore(key: String, value: Any?, data: Map<String, Any?>): List<String>?

  fun deserializeItem(key: String, value: Any?): Any?
}