package com.dev.droid

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
@GlideModule
class AppGlide : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}