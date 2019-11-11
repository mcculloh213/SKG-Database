package ktx.sovereign.database.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import ktx.sovereign.database.dao.NoteDao
import ktx.sovereign.database.entity.*
import ktx.sovereign.database.relation.BadgerNote

class NoteRepository(private val dao: NoteDao) : EntityRepository() {
//    val tempNotes = dao.getAllNotes()
    var notes: LiveData<List<Note>> = dao.getAllNotes()
    // Preload searched notes with all available notes
    val searchedNotes: MutableLiveData<List<Note>> = MutableLiveData<List<Note>>().also {
        // launch query from Dispatchers.IO to avoid querying on UI thread
        scope.launch {
            // post results to MLD upon completion
            it.postValue(dao.getAll())
        }
    }

    suspend fun count(): Int = withContext(scope.coroutineContext) { dao.count() }

    /**
     * Inserts a [Note] into the database.
     * @return The inserted [Note.id]
     */
    suspend fun insert(note: Note): Long = withContext(scope.coroutineContext) { dao.insert(note) }
    suspend fun addLocation(location: Geolocation): Long = withContext(scope.coroutineContext) { dao.upsertLocation(location) }
    suspend fun addMedia(media: Image): Long = withContext(scope.coroutineContext) { dao.upsertMedia(media) }
    suspend fun addTag(tag: MetaTag): Long = withContext(scope.coroutineContext) { dao.upsertTag(tag) }
    suspend fun removeLocation(note: Long, location: Long) : Int = withContext(scope.coroutineContext) {
        dao.deleteNoteLocation(NoteLocation(note, location)
        )}
    suspend fun removeMedia(note: Long, media: Long): Int = withContext(scope.coroutineContext) {
        dao.deleteNoteMedia(NoteMedia(note, media))
    }
    suspend fun removeTag(note: Long, tag: Long): Int = withContext(scope.coroutineContext) {
        dao.deleteNoteTag(NoteTag(note, tag))
    }

    /**
     * Gets a [Note] from the database, searching on [Note.id]
     * @return The found note
     */
    suspend fun get(id: Long): Note? = withContext(scope.coroutineContext) { dao.getNote(id) }

    /**
     * Updates an existing [Note] in the database
     * @return The number of rows in the database updated.
     */
    suspend fun update(note: Note): Int = withContext(scope.coroutineContext) { dao.update(note) }

    /**
     * Deletes an existing [Note] from the database
     * @return The number of rows in the database deleted.
     */
    suspend fun delete(note: Note): Int = withContext(scope.coroutineContext) { dao.delete(note) }

    suspend fun getNote(id: Long): BadgerNote? = withContext(scope.coroutineContext) {
        dao.getBadgerNote(id)
    }
    suspend fun getNote(note: Note): BadgerNote? = withContext(scope.coroutineContext) {
        dao.getBadgerNote(note)
    }
    suspend fun saveNote(
            note: Note,
            media: List<Image>,
            locations: List<Geolocation>,
            tags: List<MetaTag>
    ): BadgerNote? = withContext(scope.coroutineContext) {
        dao.saveNote(note, media, locations, tags)
    }

//    fun search(query: String, owner: LifecycleOwner): MutableLiveData<List<Note>> {
//        if (query.isNotEmpty()) {
//            val searchQuery = "%$query%"
//            val list = dao.searchNote(searchQuery)
//            list.observe(owner, androidx.lifecycle.Observer {
//                notes = list
//                searchedNotes.postValue(it)
//            })
//        } else
//            searchedNotes.postValue(tempNotes.value)
//        return searchedNotes;
//    }

    fun search(query: String) : MutableLiveData<List<Note>> {
        // Launch asynchronously using Dispatchers.IO to prevent query on UI thread
        scope.launch {
            searchedNotes.postValue(
                    if (query.isNotEmpty()) {
                        val q = "%$query%"
                        dao.searchAsync(q)
                    } else {
                        dao.getAll()
                    }
            )
        }
        // immediately return value for observation
        return searchedNotes
    }
}

