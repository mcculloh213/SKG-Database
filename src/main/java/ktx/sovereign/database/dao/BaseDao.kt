package ktx.sovereign.database.dao

import androidx.room.*

@Dao
abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(obj: T): Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insert(vararg obj: T): List<Long>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(obj: T): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(vararg obj: T): List<Long>
    @Update
    abstract fun update(vararg obj: T): Int
    @Delete
    abstract fun delete(vararg obj: T): Int
}