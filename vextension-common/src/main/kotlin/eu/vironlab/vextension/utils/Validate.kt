package eu.vironlab.vextension.utils

import java.net.URL

object Validate {

    @JvmStatic
    fun isUrl(url: String): Boolean {
        try {
            URL(url)
            return true
        } catch (e: Exception) {
        }
        return false
    }

    @JvmStatic
    fun isBoolean(bool: String): Boolean {
        try {
            bool.toBoolean()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    @JvmStatic
    fun isInt(int: String): Boolean {
        try {
            int.toInt()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    @JvmStatic
    fun isLong(long: String): Boolean {
        try {
            long.toLong()
            return true
        } catch (e: Exception) {
            return false
        }
    }

}