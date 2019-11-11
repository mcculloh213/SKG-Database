package ktx.sovereign.database

import android.provider.BaseColumns
import android.provider.MediaStore

object SchemaInfo {
    class Audio {
        companion object SCHEMA {
            const val TableName: String = "audio"
            const val PrimaryKey: String = BaseColumns._ID
        }
    }
    class Content {
        companion object SCHEMA {
            const val TableName: String = "content"
            const val PrimaryKey: String = BaseColumns._ID
            const val Title: String = "title"
            const val Path: String = "path"
            const val Index: String = "_idx"
            const val Volume: String = "_token"
            const val Owner: String = "_owner"
            const val Checksum: String = "_checksum"
        }
    }
    class Document {
        companion object SCHEMA {
            const val TableName: String = "documents"
            const val PrimaryKey: String = BaseColumns._ID
            const val RemoteIdentifier: String = "_ref"
            const val RemoteRepository: String = "remote"
            const val FileName: String = "filename"
            const val MimeType: String = "mime_type"
            const val IsLocal: String = "is_local"
        }
    }
    class Geolocation {
        companion object SCHEMA {
            const val TableName: String = "locations"
            const val PrimaryKey: String = BaseColumns._ID
            const val Latitude: String = "latitude"
            const val Longitude: String = "longitude"
            const val Altitude: String = "altitude"
            const val Description: String = "description"
            const val Timestamp: String = "_ts"
        }
    }
    class Image {
        companion object SCHEMA {
            const val TableName: String = "images"
            const val PrimaryKey: String = BaseColumns._ID
            const val DisplayName: String = MediaStore.MediaColumns.DISPLAY_NAME
            const val Path: String = "_path"
        }
    }
    class Keyword {
        companion object SCHEMA {
            const val TableName: String = "keywords"
            const val PrimaryKey: String = BaseColumns._ID
            const val Term: String = "term"
            const val Confidence: String = "_confidence"
            const val Method: String = "_method"
        }
    }
    class Media {
        companion object SCHEMA {
            const val TableName: String = "media"
            const val PrimaryKey: String = BaseColumns._ID
            const val DateAdded: String = MediaStore.MediaColumns.DATE_ADDED
            const val DateModified: String = MediaStore.MediaColumns.DATE_MODIFIED
            const val DisplayName: String = MediaStore.MediaColumns.DISPLAY_NAME
            const val Duration: String = "_duration"
            const val Height: String = MediaStore.MediaColumns.HEIGHT
            const val MimeType: String = MediaStore.MediaColumns.MIME_TYPE
            const val Orientation: String = "_orientation"
            const val Size: String = MediaStore.MediaColumns.SIZE
            const val Title: String = MediaStore.MediaColumns.TITLE
            const val Width: String = MediaStore.MediaColumns.WIDTH
        }
    }
    class Message {
        companion object SCHEMA {
            const val TableName: String = "messages"
            const val PrimaryKey: String = BaseColumns._ID
            const val Conversation: String = "conversation"
            const val To: String = "_to"
            const val From: String = "_from"
            const val Text: String = "text"
            const val Timestamp: String = "_ts"
        }
    }
    class MetaTag {
        companion object SCHEMA {
            const val TableName: String = "_metadata"
            const val PrimaryKey: String = BaseColumns._ID
            const val Key: String = "_key"
            const val Value: String = "_value"
        }
    }
    class Note {
        companion object SCHEMA {
            const val TableName: String = "notes"
            const val PrimaryKey: String = BaseColumns._ID
            const val Title: String = "title"
            const val Body: String = "body"
            const val IsFavorite: String = "is_favorite"
            const val CreatedAt: String = "utc_created"
            const val LastUpdated: String = "utc_updated"
        }
    }
    class NoteFts {
        companion object SCHEMA {
            const val TableName: String = "fts_notes"
            const val PrimaryKey: String = "rowid"
        }
    }
    class NoteLocation {
        companion object SCHEMA {
            const val TableName: String = "note_locations"
            const val Note: String = "_note"
            const val Location: String = "_location"
        }
    }
    class NoteMedia {
        companion object SCHEMA {
            const val TableName: String = "note_media"
            const val Note: String = "_note"
            const val Media: String = "_media"
        }
    }
    class NoteTag {
        companion object SCHEMA {
            const val TableName: String = "note_tags"
            const val PrimaryKey: String = BaseColumns._ID
            const val Note: String = "_note"
            const val Tag: String = "_meta"
        }
    }
    class ScrollingMenuItem {
        companion object SCHEMA {
            const val TableName: String = "_menu"
            const val PrimaryKey: String = BaseColumns._ID
            const val Context: String = "ctx"
            const val Label: String = "label"
            const val Item: String = "item"
            const val Directive: String = "_directive"
            const val Icon: String = "_icon"
            const val Tint: String = "_tint"
            const val Ordinal: String = "ordinal"
        }
    }
    class Video {
        companion object SCHEMA {
            const val TableName: String = "videos"
            const val PrimaryKey: String = BaseColumns._ID
        }
    }
    class Volume {
        companion object SCHEMA {
            const val TableName: String = "volumes"
            const val PrimaryKey: String = BaseColumns._ID
            const val Name: String = "name"
            const val Description: String = "description"
            const val Token: String = "_token"
            const val IsCredential: String = "_credential"
        }
    }
}