package fr.ping.gamemaker

import com.google.gson.Gson
import fr.ping.gamemaker.listeners.TriggerEventListener
import fr.ping.gamemaker.items.builders.impl.BuiltinItemBuilder
import fr.ping.gamemaker.commands.GameMakerCommand
import fr.ping.utils.resources.ResourceManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fr.ping.fr.ping.utils.resources.ReadyRegistry
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.builtin.resource.VectorTypeAdapter
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.criteria.CriteriaChecker
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.builders.models.ItemBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.gamemaker.triggers.Trigger
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.io.File

class GameMakerPlugin : JavaPlugin() {
  var config: Config = Config()

  override fun onLoad() {
    dataFolder.mkdirs()
    if (!File(dataFolder, "config.json").exists())
      File(dataFolder, "config.json").writeText(gson.toJson(config))
    else
      config = gson.fromJson<Config>(File(dataFolder, "config.json").readText(), Config::class.java) ?: config

    ResourceManager.registerTypeAdapter(Vector::class.java, VectorTypeAdapter())

    System.gc()
  }

  override fun onEnable() {

    ResourceManager.addResourcePath(getResourceFolder().path)
    ResourceManager.findSchemeResources(true)
    ResourceManager.loadAllResources(true, true)

    registerCommands()
    registerEvents()
    System.gc()
  }

  override fun onDisable() {
    ResourceManager.clean()
    langRegistry.listResources().forEach { i18n ->
      i18n.file?.writeText(gson.toJson(i18n))
    }
  }

  private fun registerCommands() {
    getCommand("gamemaker")?.setExecutor(GameMakerCommand)
  }

  private fun registerEvents() {
    server.pluginManager.registerEvents(TriggerEventListener, this)
  }

  data class Config(
    var save_resources: Boolean = true,
    var builtins: Builtins = Builtins(),
    @SerializedName("item_lore_order")
    var itemLoreOrder: List<String> = listOf("attributes", "lore", "enchants")
  ) {
    data class Builtins(
      var itemBuilder : BuiltinItemBuilder.Config = BuiltinItemBuilder.Config()
    )
  }

  companion object {
    val itemRegistry = ReadyRegistry(ItemTemplate::class.java, "item")
    val itemBuilderRegistry = ReadyRegistry(ItemBuilder::class.java, "item_builder")
    val actionRegistry = ReadyRegistry(Action::class.java, "action")
    val actionExecutorRegistry = ReadyRegistry(ActionExecutor::class.java, "action_executor",)
    val triggerRegistry = ReadyRegistry(Trigger::class.java, "trigger")
    val dialogRegistry = ReadyRegistry(Dialog::class.java, "dialog")
    val criterionCheckerRegistry = ReadyRegistry(CriteriaChecker::class.java, "criterion_checker")
    val langRegistry = ReadyRegistry(I18n::class.java, "lang")

    val gson : Gson = GsonBuilder().setPrettyPrinting().create()

    fun getInstance() : GameMakerPlugin {
      return getPlugin(GameMakerPlugin::class.java)
    }

    fun getResourceFolder() : File {
      return getInstance().dataFolder.resolve("data")
    }
  }
}
