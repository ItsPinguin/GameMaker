package fr.ping.gamemaker.addons

interface RegistryCreatorHook: AddonHook {
  fun createRegistry()
}