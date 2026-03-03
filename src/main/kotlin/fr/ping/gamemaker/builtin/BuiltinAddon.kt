package fr.ping.gamemaker.builtin

import fr.ping.gamemaker.addon.AddonManager
import fr.ping.gamemaker.addon.AddonManager.registerHook
import fr.ping.gamemaker.builtin.hook.BuiltinItemBuilder
import fr.ping.gamemaker.builtin.hook.BuiltinRegistryCreator
import fr.ping.gamemaker.builtin.hook.BuiltinResourceHandler
import fr.ping.gamemaker.builtin.hook.action.DialogActionHook
import fr.ping.gamemaker.builtin.hook.action.GiveItemActionHook
import fr.ping.gamemaker.builtin.hook.action.MessagePlayerHook
import fr.ping.gamemaker.builtin.hook.criteria.CooldownCriterionChecker
import fr.ping.gamemaker.builtin.hook.criteria.EntityTagsCriterionChecker
import fr.ping.gamemaker.builtin.hook.criteria.ItemCriterionCheckerHook

object BuiltinAddon {
  fun registerHooks() {
    registerHook(BuiltinRegistryCreator)
    registerHook(BuiltinResourceHandler)
    registerHook(BuiltinItemBuilder)

    registerCriterionCheckerHooks()
    registerActionHooks()
  }

  fun registerCriterionCheckerHooks() {
    registerHook(EntityTagsCriterionChecker)
    registerHook(CooldownCriterionChecker)
    registerHook(ItemCriterionCheckerHook)
  }

  fun registerActionHooks() {
    registerHook(MessagePlayerHook)
    registerHook(GiveItemActionHook)
    registerHook(DialogActionHook)
  }
}