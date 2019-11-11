package ktx.sovereign.database.operation.note

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import ktx.sovereign.database.ApplicationDatabase
import ktx.sovereign.database.dao.NoteDao
import ktx.sovereign.database.entity.Note
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class UpdateNoteTest {
    companion object {
        @JvmStatic val TAG: String = UpdateNoteTest::class.java.simpleName
    }
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: ApplicationDatabase
    private lateinit var noteDao: NoteDao

    private val note: Note = Note("Test Title", "This is a test note", false,
        SystemClock.elapsedRealtimeNanos(), SystemClock.elapsedRealtimeNanos())

    @Before
    fun setup() {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        try {
            database = Room.inMemoryDatabaseBuilder(ctx, ApplicationDatabase::class.java)
                .allowMainThreadQueries().build()
        } catch (ex: Exception) {
            Log.i(TAG, "Caught exception while creating database.")
            ex.printStackTrace()
        }
        noteDao = database.NoteDao()

        // Insert Note into database
        note.id = noteDao.insert(note)
    }

    @Test
    fun testUpdateSingleNote() {
        // Fetch the note from the database
        val original = noteDao.getNote(note.id)

        // ASSERTION: Our original should exist within the database
        Assert.assertNotNull("No note with id ${note.id} could be found", original)

        // Modify note values
        with (original!!) {
            title = "Updated title"
            body = "The body of this note has been updated"
            favorite = true
            updated = SystemClock.elapsedRealtimeNanos()
        }

        // Write our update to the database
        val count = noteDao.update(original)

        // ASSERTION: The returned count should be one, since we only updated a single note
        Assert.assertEquals("Database did not update note with id ${note.id}", 1, count)

        // Get our updated note from the database
        val updated = noteDao.getNote(note.id)

        // ASSERTION: Again, our updated value should exist within the database
        Assert.assertNotNull("No note with id ${note.id} could be found", updated)

        // ASSERTION: Finally, our original and updated notes should not be equal
        Assert.assertNotEquals("The values of the notes are identical.", note, updated)
    }

    @After
    fun tearDown() {
        database.close()
    }
}