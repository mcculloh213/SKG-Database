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
class ReadNoteTest {
    companion object {
        @JvmStatic val TAG: String = ReadNoteTest::class.java.simpleName
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
    fun testReadSingleNote() {
        // Fetch the note from the database
        val subject = noteDao.getNote(note.id)

        // ASSERTION: Our subject should exist
        Assert.assertNotNull("No note with id ${note.id} could be found", subject)

        // ASSERTION: Our subject should be equivalent to the original note we inserted
        Assert.assertEquals("The two notes differ in value", note, subject)
    }

    @After
    fun tearDown() {
        database.close()
    }
}