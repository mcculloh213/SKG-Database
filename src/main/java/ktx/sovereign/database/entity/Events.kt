package ktx.sovereign.database.entity

data class Events(var context: String,
                  var type: String,
                  var timestamp: Long,
                  var timezone: String,
                  var data: HashMap<String, Any>)