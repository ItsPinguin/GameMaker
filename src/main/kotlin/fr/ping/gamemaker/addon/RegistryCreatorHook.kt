package fr.ping.gamemaker.addon

interface RegistryCreatorHook: AddonHook {
  fun createRegistry()
}