package ktx.sovereign.database.relation

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.entity.Image

@Parcelize
data class MediaObject(
        val image: Image,
        val imageUri: Uri,
        var videoUri: Uri?
) : Parcelable