package ktx.sovereign.database.relation

import ktx.sovereign.database.entity.*

data class BadgerNote (
        val note: Note,
        val media: List<Image> = ArrayList(),
        val locations: List<Geolocation> = ArrayList(),
        val tags: List<MetaTag> = ArrayList()
)