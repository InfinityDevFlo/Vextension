package eu.vironlab.vextension.rest

object RestUtil {

    /**
     * Get a RestClient with the Default User-Agent
     */
    @JvmStatic
    fun getDefaultClient(): RestClient {
        return RestClient("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
    }

}