# Vextension - Database Example

##  First you need the ConnectionData, in this example by the config 

````kotlin
    fun fromConfig(doc: ConfigDocument): RemoteConnectionData {
        doc.loadConfig()
        doc.getString("host", "localhost")
        doc.getInt("port", 27017)
        doc.getString("database", "admin")
        doc.getString("user", "user")
        doc.getString("password", "password")
        doc.saveConfig()
        return object : RemoteConnectionData {
            override val host: String = doc.getString("host").get()
            override val port: Int = doc.getInt("port").get()
            override val database: String = doc.getString("database").get()
            override val user: String = doc.getString("user").get()
            override val password: String = doc.getString("password").get()
        }
    }
````

## The you have to create the client with the ConnectionData 

````kotlin
    val databaseClient = createDatabaseClient(MongoDatabaseClient::class.java) {
        this.connectionData = fromConfig(ConfigDocument(File("database.json")))
    }
````

## Note: If you want to use a SqlClient such as the MariaDatabaseClient you need to register a TableCreator

```kotlin
    val creator: TableCreator = TableCreator("tablename", TableEntry("name", ColumnType.VARCHAR, length = 40)).let { 
        it.addEntry(TableEntry("tokens", ColumnType.TEXT))
    }
    SqlRegistry.creators.put("tablename", creator)
```

## Now you can get a Database for Example with Corountine
```kotlin
    databaseClient.getDatabase("tablename").queue()
```

## Then you can use the Client
```kotlin
    databaseClient.getDatabase("tablename").queue { 
        //Insert a Value
        it.insert("Hans", document("name", "Hans").append("tokens", 6)).complete()
        if (it.contains("Hans").complete()) {
            it.get("Hans").queue { 
                if (it.isPresent) {
                    println(it.get().getInt("tokens").get()) // -> Prints 6
                }
            }
        }
    }
 ```