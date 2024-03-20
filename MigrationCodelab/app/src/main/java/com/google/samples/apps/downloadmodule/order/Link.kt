package com.google.samples.apps.downloadmodule.order

data class Link(
    var url: String? = null,
    var rate: Int = 0,
    var readType: ReaderType? = null,
    var used: Boolean = false,
) : Cloneable {

    enum class ReaderType {
        /**
         * no encrypted reader type
         */
        NORMAL,

        /**
         * no encrypted reader type that limit bandwidth
         */
        NORMAL_RATE,

        /**
         * rpk xor encrypted reader type
         */
        RPK_XOR,

        /**
         * rpk xor encrypted reader type that limit bandwidth
         */
        RPK_XOR_RATE,

        @Deprecated("")
        XOR_DEPRECATED
    }


    /**
     * default access control
     */
    fun used() {
        used = true
    }

    override fun toString(): String {
        return "Link{" +
                "mUrl='" + url + '\'' +
                ", mRate=" + rate +
                ", mReadType=" + readType +
                ", mUsed=" + used +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val link = o as Link
        return if (url != null) url == link.url else link.url == null
    }

    override fun hashCode(): Int {
        return if (url != null) url.hashCode() else 0
    }

    override fun clone(): Link {
        return Link(url = url, readType = readType, rate = rate)
    }
}