package fr.ping.gamemaker.items.templates.impl

import fr.ping.gamemaker.items.templates.models.ItemTemplate

class BuiltinItemTemplate : ItemTemplate() {

  var lore: List<String>
    @Suppress("UNCHECKED_CAST")
    get() = data["lore"] as? List<String> ?: listOf()
    set(value) { data["lore"] = value }

  var enchants: MutableMap<String, Int>
    @Suppress("UNCHECKED_CAST")
    get() = data["enchants"] as? MutableMap<String, Int> ?: mutableMapOf()
    set(value) { data["enchants"] = value }

  var attributes: MutableMap<String, Double>
    @Suppress("UNCHECKED_CAST")
    get() = data["attributes"] as? MutableMap<String, Double> ?: mutableMapOf()
    set(value) { data["attributes"] = value }


}