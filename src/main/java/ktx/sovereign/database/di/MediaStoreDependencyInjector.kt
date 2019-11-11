package ktx.sovereign.database.di

import android.content.Context
import ktx.sovereign.database.ApplicationDatabase
import ktx.sovereign.database.contract.MediaStoreContract
import ktx.sovereign.database.repository.ImageRepository
import ktx.sovereign.database.repository.MediaRepository

class MediaStoreDependencyInjector(context: Context) : MediaStoreContract.DependencyInjection {
    private val _db: ApplicationDatabase = ApplicationDatabase.getDatabase(context)
//    override fun injectMediaRepository(): MediaRepository {
//
//    }

    override fun injectImageRepository(): ImageRepository = ImageRepository(_db.ImageDao())
}