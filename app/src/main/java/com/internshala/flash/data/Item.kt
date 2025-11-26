package com.internshala.flash.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

@Suppress("ANNOTATION_WILL_BE_APPLIED_ALSO_TO_PROPERTY_OR_FIELD")
data class Item(
    @StringRes val stringResourceIdb:Int,
    @StringRes val itemCategoryIdb:Int,
    val itemQuantityIdb: String,
    val itemPriceb:Int,
    @DrawableRes val imageResourceIdb: Int)
