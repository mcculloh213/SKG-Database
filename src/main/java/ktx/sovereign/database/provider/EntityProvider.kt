package ktx.sovereign.database.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ktx.sovereign.database.SchemaInfo

class EntityProvider : ContentProvider() {
    companion object {
        const val Authority = "vnd.sovereign.entity"
        @JvmStatic val NoteUri: Uri = Uri.parse("content://$Authority/${SchemaInfo.Note.TableName}")
        private const val CODE_NOTE_DIR = 1
        private const val CODE_NOTE_ITEM = 2
        private val Matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(Authority, "notes", CODE_NOTE_DIR)
            addURI(Authority, "notes/*", CODE_NOTE_ITEM)
        }
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}