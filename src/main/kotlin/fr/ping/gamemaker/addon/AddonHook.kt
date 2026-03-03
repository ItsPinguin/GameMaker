package fr.ping.gamemaker.addon

import fr.ping.utils.resources.Resource

interface AddonHook : Resource {
  override fun setId(id: String) = Unit
}