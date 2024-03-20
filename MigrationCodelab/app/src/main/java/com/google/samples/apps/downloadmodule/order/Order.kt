package com.google.samples.apps.downloadmodule.order

import android.app.Notification
import android.util.Log
import com.google.samples.apps.http.utils.Preconditions


class Order(
    links: MutableList<Link>,
    dir: String?,
    name: String?,
    suffix: Suffix?,
    fileType: FileType?,
    notification: Notification?,
    crc32: Long,
    checkSignature: Boolean,
    orderType: OrderType?
) {
    val TAG = "Order"

    /**
     * link used to connect network to download file
     */
    private var mLinks: MutableList<Link>? = links

    private var mIterator: Iterator<Link>? = null
    private var mLastLink: Link? = null

    /**
     * 下载开始时的link（统计时使用）
     */
    private var mBeginLink: Link? = null

    /**
     * file directory
     */
    private var mDir: String? = dir

    /**
     * file name without suffix
     */
    private var mName: String? = name

    /**
     * determine whether to check the signature for apk or hpk file.
     */
    private var mCheckSignature = checkSignature

    /**
     * signature of apk file after downloading.
     */
    private var signature: String? = null

    /**
     * file name suffix
     * When it is [Suffix.EMPTY], don't rename file.
     */
    private var mSuffix: Suffix? = null

    /**
     * type that determine which channel would be chosen(apk, hpk, normal).
     * When it is [FileType.EMPTY], don't rename file.
     */
    private var mFileType: FileType? = fileType

    /**
     * Nullable
     */
    private var mNotification: Notification? = notification

    /**
     * crc32 value to verify file
     */
    private var mCrc32: Long = crc32

    private var mOrderType: OrderType? = orderType


    init {
        mIterator = mLinks?.iterator()
        val next: Link = next()
        mLastLink = next ?: mLastLink
        mBeginLink = mLastLink
        mSuffix = if (suffix == null) Suffix.EMPTY else suffix
    }

    fun getDir(): String? {
        return mDir
    }

    fun getName(): String? {
        return mName
    }

    fun getSuffix(): Suffix? {
        return mSuffix
    }

    fun getFileType(): FileType? {
        return mFileType
    }

    fun getNotification(): Notification? {
        return mNotification
    }

    fun getCrc32(): Long {
        return mCrc32
    }

    fun isCheckSignature(): Boolean {
        return mCheckSignature
    }

    fun getSignature(): String? {
        return signature
    }

    fun setSignature(signature: String?) {
        this.signature = signature
    }

    fun getOrderType(): OrderType? {
        return mOrderType
    }

    @Synchronized
    fun getLinks(): List<Link> {
        val copy: MutableList<Link> = ArrayList<Link>()
        mLinks?.map {
            copy.add(it)
        }
        return copy
    }

    @Synchronized
    fun current(): Link? {
        return mLastLink
    }

    @Synchronized
    fun begin(): Link? {
        return mBeginLink
    }

    @Synchronized
    operator fun next(): Link? {
        var nextLink: Link? = null
        while (mIterator!!.hasNext()) {
            val next: Link = mIterator!!.next()
            if (!next.used) {
                next.used()
                nextLink = next
                mLastLink = next
                break
            }
        }
        /**
         * 初次设置，全部地址就已经是被使用，纯粹是补救方案，不建议使用
         */
        if (mLastLink == null && FP.size(mLinks) > 0) {
            mLastLink = mLinks!![0]
            Log.e(TAG, "no unused link to connect")
        }
        return nextLink
    }

    @Synchronized
    fun moveToFirst(link: Link) {
        if (mLinks!!.contains(link)) {
            mLinks!!.remove(link)
            mLinks!!.add(0, link)
            mIterator = mLinks!!.iterator()
            next()
        }
    }

    fun containLink(linkUrl: String?): Boolean {
        mLinks?.map {
            if (it.url.equals(linkUrl)) return true
        }
        return false
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Order
        that.mLinks?.forEach {
            if (mLinks!!.contains(it)) {
                return true
            }
        }?: run { true }
        return false
    }

    override fun toString(): String {
        return "Order{" +
                "mName='" + mName + '\'' +
                ", mLinks=" + mLinks +
                ", mLastLink=" + mLastLink +
                ", mDir='" + mDir + '\'' +
                ", mSuffix=" + mSuffix +
                ", mFileType=" + mFileType +
                '}'
    }

    /**
     * any order's hashcode is the same
     *
     * @return
     */
    override fun hashCode(): Int {
        return 0
    }


    class Builder {
        private var mDir: String? = null
        private var mName: String? = null
        private var mSuffix: Suffix? = null
        private var mFileType: FileType? = null
        private var mNotification: Notification? = null
        private var mCrc32: Long = 0
        private var mCheckSignature = true
        private val mLinks: MutableList<Link> = ArrayList<Link>()
        private var mOrderType: OrderType? = null
        fun setOrderType(orderType: OrderType?): Builder {
            mOrderType = orderType
            return this
        }

        fun setDir(dir: String?): Builder {
            mDir = dir
            return this
        }

        fun setName(name: String?): Builder {
            mName = name
            return this
        }

        fun setSuffix(suffix: Suffix?): Builder {
            mSuffix = suffix
            return this
        }

        fun setFileType(fileType: FileType?): Builder {
            mFileType = fileType
            return this
        }

        fun setNotification(notification: Notification?): Builder {
            mNotification = notification
            return this
        }

        fun setCrc32(crc32: Long): Builder {
            mCrc32 = crc32
            return this
        }

        fun setCheckSignature(checkSignature: Boolean): Builder {
            mCheckSignature = checkSignature
            return this
        }

        fun addLink(url: String?, readerType: Link.ReaderType?): Builder {
            mLinks.add(Link(url, readType = readerType))
            return this
        }

        fun addLink(url: String?, readerType: Link.ReaderType?, rate: Int): Builder {
            mLinks.add(Link(url, readType = readerType, rate = rate))
            return this
        }

        fun addLink(url: String?, readerType: Link.ReaderType?, rate: Int, used: Boolean): Builder {
            mLinks.add(Link(url, rate, readerType, used))
            return this
        }

        fun addLink(link: Link): Builder {
            mLinks.add(link)
            return this
        }

        fun addLink(links: List<Link>?): Builder {
            mLinks.addAll(links!!)
            return this
        }

        fun build(): Order {
            Preconditions.checkArgument(!FP.empty(mLinks))
            Preconditions.checkArgument(!FP.empty(mDir))
            Preconditions.checkArgument(!FP.empty(mName))
            Preconditions.checkNotNull(mSuffix)
            Preconditions.checkNotNull(mFileType)
            return Order(
                mLinks, mDir, mName, mSuffix, mFileType, mNotification, mCrc32,
                mCheckSignature, mOrderType
            )
        }

        companion object {
            fun newBuilder(): Builder {
                return Builder()
            }
        }
    }


    // 为了使用 EventNotifyCenter时, 区分类型
    enum class OrderType {
        UNKNOWN,

        // 未知
        GAME,

        // 游戏
        RING,

        // 铃声
        VERSION,

        // 版本升级
        THEME,

        // 主题
        VIDEO,

        // 视频
        PLUGIN,

        // 插件
        VIDEOLIB_LOADER,

        // 视频插件
        TEST
        // 测试
    }
}