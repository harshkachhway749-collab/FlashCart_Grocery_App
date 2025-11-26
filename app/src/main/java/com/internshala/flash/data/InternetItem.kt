package com.internshala.flash.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InternetItem(
    @SerialName(value = "stringResourceId")
    val itemName: String="",
    @SerialName(value = "itemCategoryId")
   val itemCategory: String="",
    @SerialName(value = "item_price")
    val itemPrice:Int=0,
    @SerialName(value = "itemQuantity")
    val itemQuantity:String="",
    @SerialName(value = "imageResourceId")
    val imageUrl:String="" )
