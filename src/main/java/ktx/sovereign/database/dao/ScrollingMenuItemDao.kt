package ktx.sovereign.database.dao

import androidx.room.*
import ktx.sovereign.database.entity.ScrollingMenuItem

@Dao
interface ScrollingMenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: ScrollingMenuItem): List<Long>
    @Update
    fun update(vararg items: ScrollingMenuItem): Int
    @Delete
    fun delete(vararg items: ScrollingMenuItem): Int

    @Query("""
        SELECT * FROM _menu WHERE ctx = :context
        ORDER BY _menu.ordinal ASC
    """)
    fun getByContext(context: String): List<ScrollingMenuItem>
    @Query("DELETE FROM _menu")
    fun deleteAll()
}