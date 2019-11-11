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
class CreateNoteTest {
    companion object {
        @JvmStatic val TAG: String = CreateNoteTest::class.java.simpleName
    }
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: ApplicationDatabase
    private lateinit var noteDao: NoteDao

    val note: Note = Note("Test Title", "This is a test note", false,
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
    }

    @Test
    fun testInsertSingleNote() {
        // Get existing notes
        val preInsertNote = noteDao.getAll()

        // Insert note & capture inserted ID
        note.id = noteDao.insert(note)

        // Re-fetch existing notes
        val postInsertNote = noteDao.getAll()

        // Calculate difference in pre/post note list sizes
        val diff = postInsertNote.size - preInsertNote.size

        // ASSERTION: Size should only be different by 1, since we inserted only one note
        Assert.assertEquals("Pre/Post Note lists differ by $diff.", 1, diff)
        // ASSERTION: The value of [Note.id] should no longer be the default 0L
        Assert.assertNotEquals("Note.id was not updated.", 0, note.id)
        // ASSERTION: Post insert Note list should contain the note we originally created
        Assert.assertTrue(postInsertNote.contains(note))
    }

    @After
    fun tearDown() {
        database.close()
    }
}