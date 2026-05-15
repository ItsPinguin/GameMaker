package fr.ping.gamemaker.menus.models

import fr.ping.utils.resources.ResourceHandle
import org.bukkit.inventory.Inventory

data class MenuInstance(
  var template: ResourceHandle<MenuTemplate> = ResourceHandle(),
  var inventory: Inventory? = null,
  var data: Map<String, Any?> = mutableMapOf(),
  var pageStates : MutableMap<String, PageState> = mutableMapOf()
)
