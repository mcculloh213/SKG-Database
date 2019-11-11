@file:Suppress("ClassName")

package ktx.sovereign.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ktx.sovereign.database.SchemaInfo

class Migration_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE ${SchemaInfo.Content.TableName} ADD COLUMN ${SchemaInfo.Content.Owner} TEXT NOT NULL
        """.trimIndent())
    }
}
class Migration_2_3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //language=RoomSql
        database.execSQL("""
            DROP TABLE IF EXISTS `${SchemaInfo.Content.TableName}`
        """.trimIndent())
        //language=RoomSql
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS ${SchemaInfo.Content.TableName} (
                `${SchemaInfo.Content.PrimaryKey}` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `${SchemaInfo.Content.Title}` TEXT NOT NULL,
                `${SchemaInfo.Content.Path}` TEXT NOT NULL,
                `${SchemaInfo.Content.Volume}` TEXT NOT NULL,
                `${SchemaInfo.Content.Index}` INTEGER NOT NULL,
                `${SchemaInfo.Content.Checksum}` TEXT NOT NULL
            )
        """.trimIndent())
    }
}
class Migration_3_4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //language=RoomSql
        database.execSQL("""
            ALTER TABLE ${SchemaInfo.Volume.TableName} ADD COLUMN ${SchemaInfo.Volume.IsCredential} TINYINT NOT NULL
        """.trimIndent())
    }

}