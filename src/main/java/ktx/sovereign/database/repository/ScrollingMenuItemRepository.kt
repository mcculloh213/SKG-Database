package ktx.sovereign.database.repository

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ktx.sovereign.database.dao.ScrollingMenuItemDao
import ktx.sovereign.database.entity.ScrollingMenuItem

class ScrollingMenuItemRepository(
        private val dao: ScrollingMenuItemDao
) : EntityRepository() {
    suspend fun insert(vararg items: ScrollingMenuItem): List<Long> = withContext(scope.coroutineContext) {
        dao.insert(*items)
    }
    fun getMenuItems(context: String): List<ScrollingMenuItem> {
        val items = ArrayList<ScrollingMenuItem>()
        scope.launch {
            items.addAll(dao.getByContext(context))
        }
        return items
    }
}