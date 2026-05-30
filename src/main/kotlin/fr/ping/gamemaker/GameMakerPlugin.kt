package fr.ping.gamemaker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fr.ping.fr.ping.utils.resources.registry.ReadyRegistry
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.i18n.I18n
import fr.ping.gamemaker.utils.LocationTypeAdapter
import fr.ping.gamemaker.utils.VectorTypeAdapter
import fr.ping.gamemaker.dialog.Dialog
import fr.ping.gamemaker.commands.GameMakerCommand
import fr.ping.gamemaker.criteria.CriterionChecker
import fr.ping.gamemaker.editor.impl.RegistryItemListBuilder
import fr.ping.gamemaker.editor.impl.ResourceItemListBuilder
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.builders.impl.BuiltinItemBuilder
import fr.ping.gamemaker.items.builders.models.ItemBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.gamemaker.listeners.InventoryListener
import fr.ping.gamemaker.listeners.ItemListener
import fr.ping.gamemaker.listeners.TriggerEventListener
import fr.ping.gamemaker.items.builders.models.ItemListBuilder
import fr.ping.gamemaker.menus.TestListProvider
import fr.ping.gamemaker.menus.models.MenuTemplate
import fr.ping.gamemaker.notifications.impl.NotifyActionExecutor
import fr.ping.gamemaker.triggers.Trigger
import fr.ping.gamemaker.utils.SoundTypeAdapter
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Location
import org.bukkit.Sound
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

    ResourceManager.addAllResourcePaths(config.resourcePaths)
    ResourceManager.registerTypeAdapter(Vector::class.java, VectorTypeAdapter)
    ResourceManager.registerTypeAdapter(Location::class.java, LocationTypeAdapter)
    ResourceManager.registerTypeAdapter(Sound::class.java, SoundTypeAdapter)

    System.gc()
  }

  override fun onEnable() {

    ResourceManager.addResourcePath(getResourceFolder().path)
    ResourceManager.findSchemeResources(true)
    ResourceManager.loadAllResources(true, true)
    I18nManager.compileLoadedI18n()

    itemListBuilderRegistry.registerResource("levels", TestListProvider())
    itemListBuilderRegistry.registerResource("editor/registries", RegistryItemListBuilder)
    itemListBuilderRegistry.registerResource("editor/resource", ResourceItemListBuilder)

    registerCommands()
    registerEvents()
    System.gc()
  }

  override fun onDisable() {
    ResourceManager.clean()
    //langRegistry.listResources().forEach { i18n ->
    //  i18n.file?.writeText(gson.toJson(i18n))
    //}
  }

  private fun registerCommands() {
    getCommand("gamemaker")?.setExecutor(GameMakerCommand)
  }

  private fun registerEvents() {
    server.pluginManager.registerEvents(TriggerEventListener, this)
    server.pluginManager.registerEvents(InventoryListener, this)
    server.pluginManager.registerEvents(ItemListener, this)
  }

  data class Config(
    @SerializedName("resource_paths")
    var resourcePaths: List<String> = listOf("resources", "plugins/GameMaker/data"),
    var saveResources: Boolean = true,
    var builtins: Builtins = Builtins(),
    @SerializedName("item_lore_order")
    var itemLoreOrder: List<String> = listOf("attributes", "lore", "enchants")
  ) {
    data class Builtins(
      var itemBuilder : BuiltinItemBuilder.Config = BuiltinItemBuilder.Config()
    )
  }

  companion object {
    val itemTemplateRegistry = ReadyRegistry(ItemTemplate::class.java, "item_template")
    val itemBuilderRegistry = ReadyRegistry(ItemBuilder::class.java, "item_builder")
    val actionRegistry = ReadyRegistry(Action::class.java, "action")
    val actionExecutorRegistry = ReadyRegistry(ActionExecutor::class.java, "action_executor",)
    val triggerRegistry = ReadyRegistry(Trigger::class.java, "trigger")
    val dialogRegistry = ReadyRegistry(Dialog::class.java, "dialog")
    val criterionCheckerRegistry = ReadyRegistry(CriterionChecker::class.java, "criterion_checker")
    val langRegistry = ReadyRegistry(I18n::class.java, "lang")
    val menuTemplateRegistry = ReadyRegistry(MenuTemplate::class.java, "menu_template")
    val itemListBuilderRegistry = ReadyRegistry(ItemListBuilder::class.java, "item_list_provider")

    val gson : Gson = GsonBuilder().setPrettyPrinting().create()

    fun getInstance() : GameMakerPlugin {
      return getPlugin(GameMakerPlugin::class.java)
    }

    fun getResourceFolder() : File {
      return getInstance().dataFolder.resolve("data")
    }

    init {
      actionExecutorRegistry.registerResource("notify", NotifyActionExecutor())
    }
  }
}
