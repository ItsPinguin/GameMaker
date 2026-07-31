package fr.itspinguin.gamemaker.actions

import fr.itspinguin.gamemaker.GameMakerPlugin.Companion.actionExecutorRegistry
import fr.itspinguin.gamemaker.actions.impl.*
import fr.itspinguin.gamemaker.actions.models.Action

object ActionManager {
  fun executeAction(action: Action, context: ActionContext) =
    actionExecutorRegistry.getResource(action.action)?.execute(action, context)

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