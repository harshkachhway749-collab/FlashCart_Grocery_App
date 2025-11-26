@file:Suppress("PackageName")

package com.internshala.flash.Network

import com.internshala.flash.data.InternetItem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val Base_URL="https://training-uploads.internshala.com"
private val retrofit= Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(
        "application/json".toMediaType()))
    .baseUrl(Base_URL).build()

/* I.M.P-> in your code, you are creating a Retrofit instance/object...
...that your app can use to make API calls.*/

interface FlashApiService{
    @GET("/android/grocery_delivery_app/items.json")
   suspend fun getItems(): List<InternetItem>
// suspend don't block the thread on which its running on ,
// suspend keyword is used to ensure that this func is handle by coroutines
}
object FlashApi{
    val retrofitService: FlashApiService by lazy {
        retrofit.create(FlashApiService::class.java) }
}