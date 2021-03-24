package eu.vironlab.vextension.rest

import java.net.HttpURLConnection
import java.net.URL

object RestUtil {

    var DEFAULT_AGENT =
        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"

    /**
     * Get a RestClient with the Default User-Agent
     */
     val DEFAULT_CLIENT: RestClient = RestClient(DEFAULT_AGENT)

    @JvmStatic
    fun getStatusCode(url: URL): Int {
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod = HttpMethod.GET.toString()
        connection.setRequestProperty("User-Agent", DEFAULT_AGENT)
        connection.connect()
        val rs = connection.responseCode
        connection.disconnect()
        return rs
    }

}