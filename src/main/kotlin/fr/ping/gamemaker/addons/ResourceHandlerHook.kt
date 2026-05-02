package fr.ping.gamemaker.addons

interface ResourceHandlerHook : AddonHook {
  fun clearResources()
  fun loadResources()
  fun saveResources()
}