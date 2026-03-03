package fr.ping.gamemaker.builtin.hook.action

import fr.ping.gamemaker.GameMakerPlugin.Companion.gmk
import fr.ping.gamemaker.addon.ActionExecutorHook
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import org.bukkit.entity.Player

object GiveItemActionHook: ActionExecutorHook {
  val lang by lazy { gmk.useRegistry<I18n>("lang").getHandle("actions/en_US") }

  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action !in listOf("give", "give_item", "item")) return
    val itemId = action.data["item"] as? String ?: return
    val player = context["player"] as? Player ?: return
    @Suppress("DEPRECATION")
    gmk.useRegistry<ItemTemplate>("items")[itemId]?.let { item ->
      val itemStack = item.buildItem()
      player.inventory.addItem(itemStack)
      if ((action.data["should_message"] as? Boolean == true))
        player.sendMessage(lang?.resource?.translateAndInsert("give_item.give_message", mapOf(
          "item_name" to itemStack.itemMeta.displayName
        )).toString())
    }
  }

  override fun getId(): String {
    return "give_item"
  }

  override fun clean() {
    lang?.release()
  }
}