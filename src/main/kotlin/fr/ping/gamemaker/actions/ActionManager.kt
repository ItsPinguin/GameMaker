package fr.ping.gamemaker.actions

import fr.ping.gamemaker.GameMakerPlugin.Companion.actionExecutorRegistry
import fr.ping.gamemaker.actions.impl.*
import fr.ping.gamemaker.actions.models.Action

object ActionManager {
  fun executeAction(action: Action, context: Map<String, Any?> = mapOf()) =
    actionExecutorRegistry.listResources()
      .forEach { executor -> executor.execute(action, context) }

  init {
    actionExecutorRegistry.registerResource("message_player", MessagePlayer)
    actionExecutorRegistry.registerResource("dialog", DialogAction)
    actionExecutorRegistry.registerResource("give_items", GiveItemsAction)
    actionExecutorRegistry.registerResource("take_items", TakeItemsAction)
    actionExecutorRegistry.registerResource("command", CommandAction)
    actionExecutorRegistry.registerResource("trade_items", TradeItemsAction)
    actionExecutorRegistry.registerResource("action_list", ActionListAction)
    actionExecutorRegistry.registerResource("open_menu", OpenMenuAction)
    actionExecutorRegistry.registerResource("change_page", ChangePageActionExecutor)
  }
}