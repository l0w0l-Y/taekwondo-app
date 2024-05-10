package com.taekwondo.corecommon.ext

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int) = context.getString(stringResId)

    fun getDrawable(@DrawableRes drawableResId: Int) = context.getDrawable(drawableResId)
}