package fr.ping.gamemaker.items.builders.models

import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.gamemaker.menus.models.PageState
import fr.ping.utils.resources.Resource
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class ItemListBuilder : Resource() {
  /**
   * Retrieves the item at the specified index within a given context.
   *
   * @param index The index of the item to retrieve. This is typically a zero-based index representing
   *              the position of the item within a list or collection.
   * @param context The context in which the item is being retrieved. This provides additional metadata
   *                and environment-specific information relevant to the item construction process.
   * @return The retrieved item as an ItemStack, or `null` if no item exists at the specified index.
   */
  open fun getItem(index: Int, context : ItemBuilderContext) : ItemStack? = null

  /**
   * Retrieves the size of a list within the specified context.
   *
   * @param context The context providing metadata and environment-specific information
   *                required to determine the list size.
   * @return The total number of items in the list.
   */
  open fun getListSize(context : ItemBuilderContext) : Int = 0

  /**
   * Creates an item stack representing a button for turning pages in a paginated interface.
   *
   * @param pageOffset The offset for the new page relative to the current page.
   *                   For example, a value of -1 indicates the previous page,
   *                   while 1 indicates the next page.
   * @param pageState The state data of the pagination, including the current page,
   *                  total number of items, and page size.
   * @return An ItemStack representing the page turn button, or `null` if the calculated
   *         page is out of bounds.
   */
  open fun getPageTurnItem(pageOffset : Int, pageState: PageState) : ItemStack? {
    if (pageState.page * pageState.pageSize + pageOffset * pageState.pageSize !in 0..<pageState.total) return null
    val itemStack = ItemStack(Material.ARROW)
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.lore = listOf(
      "§e§lCLICK §7to go to page ${pageOffset + pageState.page + 1}/${(pageState.total/pageState.pageSize) + 1}"
    )
    itemMeta.itemName(Component.text("§aTurn Page"))
    itemStack.itemMeta = itemMeta
    return itemStack
  }

  /**
   * Handles the click event on a specific slot within an inventory menu.
   *
   * @param index The zero-based index representing the position of the clicked item in the current context,
   *              adjusted for paginated menus if applicable.
   * @param event The event object representing the inventory click, containing details such as the
   *              clicked slot and the type of click performed by the user.
   * @param menuInstance The instance of the menu currently being interacted with, including metadata,
   *                     the inventory object, and pagination states.
   */
  open fun onClick(index: Int, event: InventoryClickEvent, menuInstance: MenuInstance) {
  }
}