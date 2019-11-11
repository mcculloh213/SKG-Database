package ktx.sovereign.database.callback

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ktx.sovereign.database.ApplicationDatabase
import ktx.sovereign.database.entity.Content
import ktx.sovereign.database.entity.Volume

class SeedDemoVolumesCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
//        ApplicationDatabase.getDatabase(context)
//                .VolumeDao().apply {
//                    ApplicationDatabase.DatabaseThread {
//                        insert(Volume(
//                                "c659e022-f202-49dd-b409-f2ca7ed710bf",
//                                "MN Read Paragraphs",
//                                "Paragraphs ranging from 0.13M to 10M",
//                                "809d1610-4958-41d0-80c1-992343baaddb"
//                        ))
//                        insert(Volume(
//                                "e661d3b5-771e-4c44-a4fa-11bdc931afb1",
//                                "MN Read Vowels",
//                                "Vowel chart ranging from 0.13M to 10M",
//                                "6dcd9967-429e-4d64-ba3e-fad3d8f867cd"
//                        ))
//                        insertContent(
//                                Content(
//                                        "d42c6e48-847d-4781-8b1c-58c668f7ab7c",
//                                        "MN Read AEIOU",
//                                        "00ul0qcsdcT1MJnp50h7",
//                                        "",
//                                        "e661d3b5-771e-4c44-a4fa-11bdc931afb1",
//                                        ""
//                                )
//                        )
//                        insert(Volume(
//                                "94ff475a-9542-430a-8f0e-59a351576f66",
//                                "Low Vision Paragraphs",
//                                "Sample paragraphs for testing gyroscope functionality",
//                                "be73b7bf-64d6-4091-8d07-41814b88539d"
//                        ))
//                        insert(Volume(
//                                "b1099713-8518-4d2f-865d-cdb2ceeb670e",
//                                "FS100: Startup & Shutdown",
//                                "Startup & Shutdown operations on the FS100",
//                                "71ea0744-b28d-4fb3-b2b9-5db19c757769"
//                        ))
//                        insert(Volume(
//                                "00183f62-ccb4-4ca9-878f-cd3b2d4e2f36",
//                                "FS100: Pendant",
//                                "An overview of the FS100 Programming Pendant",
//                                "2641845a-68f5-4c7d-bc39-048561313115"
//                        ))
//                        insert(Volume(
//                                "f125c175-d5be-4b86-8962-bbaadb13ea3d",
//                                "FS100: Pendant Screen",
//                                "An overview of the FS100 Pendant Screen",
//                                "ecf97520-5ce5-42d6-8971-a0b67fc200b1"
//                        ))
//                        insert(Volume(
//                                "19f7522e-5e45-4432-80ac-a79bf5810797",
//                                "FS100: Control Group & Jogging Coordinate",
//                                "Controlling a robot using the FS100",
//                                "c502c007-9d57-4346-9531-3cc297ef9390"
//                        ))
//                        insert(Volume(
//                                "2141366b-7f3c-42c1-a600-52cf97275629",
//                                "YRC1000 Basic Operations Manual",
//                                "Basic operations manual for the YRC1000 Robot Controller",
//                                "fd7c516f-2c4f-4977-b094-e9d7b936eb00"
//                        ))
//                        insert(Volume(
//                                "0f6e695d-220f-4dcf-ac47-6a85497051da",
//                                "YRC1000 General Operations Manual",
//                                "General operations manual for the YRC1000 Robot Controller",
//                                "ce044606-dd9c-45d8-8bdf-35e3b4f747f5"
//                        ))
//                    }
//                }
    }
}