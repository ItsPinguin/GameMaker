package fr.ping.gamemaker.addon

interface ResourceHandlerHook : AddonHook {
  fun clearResources()
  fun loadResources()
  fun saveResources()
}