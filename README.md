# Vextensions

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/785956343407181824.svg)](https://discord.gg/wvcX92VyEH)
[![Build Status](https://ci.vironlab.eu/job/Vextension/badge/icon)](https://ci.vironlab.eu/job/Vextension/)

### Project description 

Vextension JVM Utility

--- 
## Maven Dependency

```gradle

//Repository
maven {
    name = "vironlab"
    url = "https://repo.vironlab.eu/repository/snapshot/"
}

//Dependency - Core 
compile("eu.vironlab.vextension:vextension-core:1.0.0-SNAPSHOT")

//Dependency - Minecraft - Server
compile("eu.vironlab.vextension:vextension-minecraft-server:1.0.0-SNAPSHOT")

//Dependency - Minecraft - Proxy
compile("eu.vironlab.vextension:vextension-minecraft-proxy:1.0.0-SNAPSHOT")
```

## Examples

### Database

```kotlin
fun initDatabase(config: Document) {
    VextensionAPI.initialize()
    VextensionAPI.instance.initDatabase(
        DatabaseClientType.MONGO, DatabaseConnectionData(
            config.getString("host", "localhost"),
            config.getString("port", "27017"),
            config.getString("user", "admin"),
            config.getString("password", "password"),
            config.getString("database", "example")
        )
    )
}

class PersonObject() : DatabaseObject {
    var name: String = ""
    var age: Int = 0

    constructor(name: String, age: Int) : this() {
        this.name = name
        this.age = age
    }

    override fun init(document: Document) {
        this.age = document.getInt("age").get()
        this.name = document.getString("name").get()
    }

    override fun toDocument(): Document {
        return DocumentManagement.newDocument().insert("age", this.age).insert("name", this.name)
    }
}

fun getPerson(name: String): PersonObject {
    return VextensionAPI.instance.getDatabaseClient().getDatabase<PersonObject>("persons", PersonObject::class.java)
        .get(name).get()
}

fun createPerson(person: PersonObject) {
    VextensionAPI.instance.getDatabaseClient().getDatabase<PersonObject>("persons", PersonObject::class.java)
        .insertAsync(person.name, person)
}
```

### Bukkit

```kotlin
        //MojangWrapper
        val mojangWrapper: MojangWrapper = DefaultMojangWrapper()
        
        //Sidebar Example
        val sidebar: Sidebar = sidebar {
            this.title = "ExampleSidebar"
            addLine { 
                this.name = "profile"
                this.content = "Name: %name%"
                this.score = 2
                proceed { sidebarLine, uuid -> 
                    sidebarLine.content.replace("%name%", mojangWrapper.getPlayer(uuid).get().name)
                }
            }
            addEmptyLine("profile_sub", 1)
        }
```


<div align="center">
    <h1 style="color:#154444">Other Links:</h1>
    <a style="color:#00ff00" target="_blank" href="https://github.com/VironLab"><img src="https://img.shields.io/github/followers/VironLab?label=GitHub%20Followers&logo=GitHub&logoColor=%23ffffff&style=flat-square"></img></a>
    <a style="color:#00ff00" target="_blank" href="https://discord.gg/wvcX92VyEH"><img src="https://img.shields.io/discord/785956343407181824?label=vironlab.eu%20Discord&logo=Discord&logoColor=%23ffffff&style=flat-square"></img></a>
</div>
