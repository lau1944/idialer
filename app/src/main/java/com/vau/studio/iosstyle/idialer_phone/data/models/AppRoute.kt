package com.vau.studio.iosstyle.idialer_phone.data.models

/**
 * Represent app routing object
 */
data class AppRoute(
    val route: String,
    val args: Map<String, Any>? = null
) {
    /**
     * Parse current route with extra params
     * output the real route
     */
    fun parseRoute() : String {
        if (args.isNullOrEmpty()) return route

        val buffer = StringBuffer("$route?")
        val iterator = args.asIterable().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            buffer.append(entry.key)
            buffer.append("=")
            buffer.append("${entry.value}")
            if (iterator.hasNext()) {
                buffer.append("&")
            }
        }
        return buffer.toString()
    }
}