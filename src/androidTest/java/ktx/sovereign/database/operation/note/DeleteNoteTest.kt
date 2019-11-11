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
class DeleteNoteTest {
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
    fun testDeleteSingleNote() {
        // Get existing notes
        val preDeleteNote = noteDao.getAll()

        // Insert note & capture inserted ID
        val count = noteDao.delete(note)

        // ASSERTION: Returned count should be one, since we only deleted a single note from the database
        Assert.assertEquals("Original note was not deleted from the database.", 1, count)

        // Re-fetch existing notes
        val postDeleteNote = noteDao.getAll()

        // Calculate difference in pre/post note list sizes
        val diff = preDeleteNote.size - postDeleteNote.size

        // ASSERTION: Size should only be different by 1, since we deleted only one note
        Assert.assertEquals("Pre/Post Note lists differ by $diff.", 1, diff)

        val isNull = noteDao.getNote(note.id)

        // ASSERTION: Returned value should be null, because the note has been deleted from the database.
        Assert.assertNull("Database returned a found object", isNull)
    }

    @After
    fun tearDown() {
        database.close()
    }
}