package ktx.sovereign.database.relation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ktx.sovereign.database.entity.Content
import ktx.sovereign.database.entity.Volume

@Parcelize
data class Book (
        val volume: Volume,
        val content: List<Content>
) : Parcelable