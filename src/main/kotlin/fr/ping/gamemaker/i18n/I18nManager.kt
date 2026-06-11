package fr.ping.gamemaker.i18n

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.utils.adapter.ComponentTypeAdapter
import fr.ping.utils.resources.ResourceManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern
import kotlin.toString

object I18nManager {
  val languages : MutableMap<String, MutableMap<String, Any?>> = mutableMapOf()
  var config = I18nConfig()

  val playerLanguages : MutableMap<UUID, String> = mutableMapOf()

  operator fun get(key: String, vararg args: Any?) : Component = get(config.defaultLanguage, key, *args)

  operator fun get(locale: String, key: String, vararg args: Any?) : Component {
    return getComponent(locale, key, *args)
  }

  operator fun get(player: Player, key: String, vararg args: Any?) : Component =
    get(I18nManager.playerLanguages[player.uniqueId] ?: config.defaultLanguage, key, *args)

  fun getString(locale : String, key : String, vararg args : Any?) : String {
    val value = getAnyOrFallback(locale, key).toString()
    return if (args.isEmpty()) value else insertIntoString(value, *args)
  }

  fun getStringIfIndicator(locale: String, key: String, vararg args : Any?) : String {
    return if (!key.startsWith("$")) key
    else getString(locale, key.substring(1), *args)
  }

  fun getComponent(locale : String, key : String, vararg args : Any?) : Component {
    val current = getAnyOrFallback(locale, key)
    val component = ResourceManager.parseAny<Component>(current) ?: ComponentTypeAdapter.parseComponent(current.toString())

    return if (args.isEmpty()) component else insertIntoComponent(component, *args)
  }

  fun getComponentIfIndicator(locale: String, key: String, vararg args : Any?) : Component {
    return if (!key.startsWith("$")) ComponentTypeAdapter.parseComponent(key)
    else getComponent(locale, key.substring(1), *args)
  }

  fun getFallback(locale: String) : String {
    return config.languageFallbacks[locale] ?: config.defaultLanguage
  }

  fun getAny(locale : String, key : String) : Any? {
    val translations = languages[locale] ?: languages[config.languageFallbacks[locale]] ?: languages[config.defaultLanguage] ?: return null
    val path = key.lowercase().split(".")
    var current: Any? = translations
    for (part in path) {
      if (current !is Map<*, *>) {
        return null
      }
      current = current[part]
    }
    return current
  }

  fun getAnyOrFallback(locale : String, key : String) : Any {
    if (locale == config.defaultLanguage) return getAny(locale, key) ?: key
    return getAny(locale, key) ?: getAnyOrFallback(getFallback(locale), key)
  }

  fun insertIntoString(value : String, vararg args : Any?) : String {
    val pattern = Pattern.compile("\\{(\\d+)(?::([^}]+))?}")

    var newValue = value
    val matcher = pattern.matcher(value)

    val processedPlaceholders = mutableSetOf<String>()

    while (matcher.find()) {
      val fullPlaceholder = matcher.group(0) // "{0:%.2f}", "{1}" ...
      if (processedPlaceholders.contains(fullPlaceholder)) continue
      processedPlaceholders.add(fullPlaceholder)

      val index = matcher.group(1).toInt() //todo change to map ?
      val formatRule = matcher.group(2) // "%.2f", "%d", null ...

      if (index >= args.size) continue
      val argumentValue = args[index]

      val formattedReplacement = if (formatRule != null) {
        try {
          val cleanFormat = if (formatRule.startsWith("%")) formatRule else "%$formatRule"
          String.format(cleanFormat, argumentValue)
        } catch (_: Exception) {
          argumentValue?.toString() ?: "null"
        }
      } else {
        argumentValue?.toString() ?: "null"
      }
      newValue = newValue.replace(fullPlaceholder, formattedReplacement)
    }
    return value
  }

  fun insertIntoComponent(component : Component, vararg args : Any?) : Component {
    var component = component
    val pattern = Pattern.compile("\\{(\\d+)(?::([^}]+))?}")

    val flatText = component.compact().toString()
    val matcher = pattern.matcher(flatText)

    val processedPlaceholders = mutableSetOf<String>()

    while (matcher.find()) {
      val fullPlaceholder = matcher.group(0) // "{0:%.2f}", "{1}" ...
      if (processedPlaceholders.contains(fullPlaceholder)) continue
      processedPlaceholders.add(fullPlaceholder)

      val index = matcher.group(1).toInt() //todo change to map ?
      val formatRule = matcher.group(2) // "%.2f", "%d", null ...

      if (index >= args.size) continue
      val argumentValue = args[index]

      val formattedReplacement = if (formatRule != null) {
        try {
          val cleanFormat = if (formatRule.startsWith("%")) formatRule else "%$formatRule"
          String.format(cleanFormat, argumentValue)
        } catch (_: Exception) {
          argumentValue?.toString() ?: "null"
        }
      } else {
        argumentValue?.toString() ?: "null"
      }

      val replacementConfig = TextReplacementConfig.builder()
        .matchLiteral(fullPlaceholder)
        .replacement(formattedReplacement)
        .build()

      component = component.replaceText(replacementConfig)
    }

    return component
  }

  @Deprecated("") @ApiStatus.ScheduledForRemoval
  fun translateIfIndicator(key : String, indicator : String = "$", vararg args : Any?) : Component = translateIfIndicator(Component.text(key), indicator, *args)

  @Deprecated("") @ApiStatus.ScheduledForRemoval
  fun translateIfIndicator(component: Component, indicator: String = "$", vararg args: Any?): Component {
    val plainText = PlainTextComponentSerializer.plainText().serialize(component)

    if (!plainText.startsWith(indicator)) return component

    val translationKey = plainText.substring(indicator.length)
    val languageMap = languages[config.defaultLanguage] ?: return component

    return translateAndInsert(languageMap, translationKey, *args)
  }

  fun compileLoadedI18n() {
    GameMakerPlugin.langRegistry.listResources().forEach { i18n ->
      languages.getOrPut(i18n.locale) { mutableMapOf() }.apply {
        i18n.translations.forEach { (key, value) -> put(key, value) }
      }
    }
  }

  @ApiStatus.ScheduledForRemoval @Deprecated("")
  private fun translate(translations: MutableMap<String, Any?>, key: String): Component {
    if (translations.containsKey("_cache.$key")) return translations["_cache.$key"] as Component
    val path = key.lowercase().split(".")
    var current: Any? = translations

    for (part in path) {
      if (current !is Map<*, *>) {
        return Component.text(key)
      }
      current = current[part]
      if (current == null) {
        return Component.text(key)
      }
    }
    val component = ResourceManager.parseAny<Component>(current) ?: Component.text(current.toString())

    translations["_cache.$key"] = component
    return component
  }

  @ApiStatus.ScheduledForRemoval @Deprecated("")
  private fun translateAndInsert(translations: MutableMap<String, Any?>, key: String, vararg args: Any?): Component {
    val baseComponent = translate(translations, key)
    return if (args.isEmpty()) baseComponent else insertIntoComponent(baseComponent, *args)
  }
}