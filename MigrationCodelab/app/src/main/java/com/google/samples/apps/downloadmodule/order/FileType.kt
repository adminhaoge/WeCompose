package com.google.samples.apps.downloadmodule.order


enum class FileType {
    EMPTY,
    APK_OR_RPK,
    HPK,
    ZIP,
    RMVB,
    GBA,
    GBC,
    NGP,
    NES,
    NDS,
    SFC,
    SMD,
    N64,
    MAME,
    MAME4Droid,
    ARCADE,
    MP4,
    MP3,
    ISO,
    CSO,
    APKPATCH
}

enum class Suffix(val suffix: String) {
    EMPTY("rmvb"),
    APK_OR_RPK("apk"),
    HPK("hpk"),
    ZIP("zip"),
    RMVB("rmvb"),
    GBA("gba"),
    NGP("ngp"),
    NES("nes"),
    NDS("nds"),
    MP4("mp4"),
    MP3("mp3"),
    ISO("iso"),
    CSO("cso"),
    APKPATCH("patch");
}

