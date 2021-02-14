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

//Dependency - Common
compile("eu.vironlab.vextension:vextension-common:1.0.0-SNAPSHOT")

//Dependency - Minecraft - Server
compile("eu.vironlab.vextension:vextension-minecraft-server:1.0.0-SNAPSHOT")

//Dependency - Minecraft - Proxy
compile("eu.vironlab.vextension:vextension-minecraft-proxy:1.0.0-SNAPSHOT")
```

## Examples

### Database


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
    <a style="color:#00ff00" target="_blank" href="https://discord.gg/wvcX92VyEH"><img src="https://img.shields.io/discord/785956343407181824?label=vironlab.eu%20Discord&logo=Discord&logoColor=%23ffffff&style=flat-square"></img></a>
</div>
