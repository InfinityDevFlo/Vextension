package eu.vironlab.vextension.mojang

object MojangConstants {
    @JvmStatic
    val STATUS_CHECK_URL = "https://status.mojang.com/check"

    @JvmStatic
    val UUID_REQUEST_URL = "https://api.mojang.com/users/profiles/minecraft/"

    @JvmStatic
    val PLAYER_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%uuid%?unsigned=false"

    @JvmStatic
    val PLAYER_NAME_HISTORY = "https://api.mojang.com/user/profiles/%uuid%/names"
}