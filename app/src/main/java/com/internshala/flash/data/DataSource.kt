package com.internshala.flash.data

import androidx.annotation.StringRes
import com.internshala.flash.R

object DataSource
{
    fun loadCategories(): List<Categories>
    {
        return listOf(
            Categories(stringResourceIda = R.string.fresh_fruits, imageResourceIda = R.drawable.freshfoods),
            Categories(R.string.munchies,R.drawable.munchies),
            Categories(R.string.pet_food,R.drawable.petfood),
            Categories(R.string.bath_body,R.drawable.bathessentials),
            Categories(R.string.beverages,R.drawable.beverages),
            Categories(R.string.bread_biscuit,R.drawable.breadbiscuits),
            Categories(R.string.cleaning_essentials,R.drawable.cleaningessentials),
            Categories(R.string.kitchen_essentials,R.drawable.kitchenessentials),
            Categories(R.string.packaged_food,R.drawable.packagedfood),
            Categories(R.string.sweet_teeth,R.drawable._sweet_tooth),
            Categories(R.string.stationary,R.drawable.stationary),


        )
    }

    @Suppress("unused")
    fun loadItems(
        @StringRes categoryNameID: Int
    ): List<Item>{
        return listOf(
            Item(stringResourceIdb = R.string.banana_robusta,R.string.fresh_fruits,"1kg",50,R.drawable.bananarobusta),
            Item(stringResourceIdb = R.string.pine_Apple,R.string.fresh_fruits,"1kg",60,R.drawable.pineapple),
            Item(stringResourceIdb = R.string.pomegranate,R.string.fresh_fruits,"1kg",150,R.drawable.pomegranate),
            Item(stringResourceIdb = R.string.pepsi_can,R.string.beverages,"1L",60,R.drawable.pepsican),
            Item(stringResourceIdb = R.string.shimla_Apple,R.string.fresh_fruits,"1kg",200,R.drawable.shimlaapple),
            Item(stringResourceIdb = R.string.papaya_semi_ripe,R.string.fresh_fruits,"1kg",50,R.drawable.papaya)
        ).filter { it.itemCategoryIdb== categoryNameID }
    }
}