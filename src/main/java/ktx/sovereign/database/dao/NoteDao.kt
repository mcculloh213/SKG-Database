package ktx.sovereign.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import ktx.sovereign.database.entity.*
import ktx.sovereign.database.relation.BadgerNote

@Dao
abstract class NoteDao : BaseDao<Note>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertMedia(media: Image): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNoteMedia(relation: NoteMedia)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertLocation(location: Geolocation): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNoteLocation(relation: NoteLocation)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertTag(tag: MetaTag): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNoteTag(relation: NoteTag)

    @Delete
    abstract fun deleteNoteMedia(relation: NoteMedia): Int
    @Delete
    abstract fun deleteNoteLocation(relation: NoteLocation): Int
    @Delete
    abstract fun deleteNoteTag(relation: NoteTag): Int

    @Query("SELECT * FROM notes WHERE _id = :id")
    abstract fun getNote(id: Long): Note?
    @Query ("SELECT COUNT(*) FROM notes")
    abstract fun count(): Int

    @Query("SELECT * FROM notes")
    abstract fun getAll(): List<Note>
    @Query("SELECT * FROM notes")
    abstract fun getAllNotes(): LiveData<List<Note>>
    @Query("DELETE FROM notes")
    abstract fun deleteAll()

    @Query("""
        SELECT notes.* FROM notes
        JOIN fts_notes ON notes._id = fts_notes.docid
        WHERE fts_notes MATCH :query
    """)
    abstract fun search(query: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE :query")
    abstract fun searchNote(query: String): LiveData<List<Note>>
    @Query("SELECT * FROM notes WHERE title LIKE :query")
    abstract fun searchAsync(query: String): List<Note>

    @Query("""
        SELECT * FROM images
        LEFT OUTER JOIN note_media ON images._id = note_media._media
        WHERE note_media._note = :id
    """)
    abstract fun getImages(id: Long): List<Image>
    @Query("""
        SELECT * FROM locations
        LEFT OUTER JOIN note_locations ON locations._id = note_locations._location
        WHERE note_locations._note = :id
    """)
    abstract fun getLocations(id: Long): List<Geolocation>
    @Query("""
        SELECT * FROM _metadata
        LEFT OUTER JOIN note_tags ON _metadata._id = note_tags._meta
        WHERE note_tags._note = :id
    """)
    abstract fun getTags(id: Long): List<MetaTag>

    @Transaction
    open fun getBadgerNote(id: Long): BadgerNote? {
        try {
            val note = getNote(id) ?: return null
            val images = getImages(id)
            val locations = getLocations(id)
            val tags = getTags(id)
            return BadgerNote(note, images, locations, tags)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
    @Transaction
    open fun getBadgerNote(note: Note): BadgerNote? {
        val id = note.id
        try {
            val images = getImages(id)
            val locations = getLocations(id)
            val tags = getTags(id)
            return BadgerNote(note, images, locations, tags)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
    @Transaction
    open fun saveNote(
        note: Note,
        images: List<Image>,
        locations: List<Geolocation>,
        tags: List<MetaTag>
    ): BadgerNote? {
        try {
            if (note.id == 0L) {    // Create
                note.id = insert(note)
            } else {                // Update
                update(note)
            }
            images.forEach {
                val imageId = upsertMedia(it)
                insertNoteMedia(NoteMedia(note.id, imageId))
                it.id = imageId
            }
            locations.forEach {
                val locationId = upsertLocation(it)
                insertNoteLocation(NoteLocation(note.id, locationId))
                it.id = locationId
            }
            tags.forEach {
                val tagId = upsertTag(it)
                insertNoteTag(NoteTag(note.id, tagId))
                it.id = tagId
            }
            return BadgerNote(note, images, locations, tags)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    @Query("SELECT * FROM notes")
    abstract fun cSelectAll(): Cursor
    @Query("SELECT * FROM notes WHERE _id = :id")
    abstract fun cGetNote(id: Long): Cursor
}