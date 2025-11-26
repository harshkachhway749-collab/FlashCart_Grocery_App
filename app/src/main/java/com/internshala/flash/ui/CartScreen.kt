package com.internshala.flash.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem
import com.internshala.flash.data.InternetItemsWithQuantity

@Composable
fun CartScreen(flashViewModal: FlashViewModal,
               onHomeButtonClicked:()-> Unit) {
    val cartItem by flashViewModal.CartItem.collectAsState()
    val cartitemsWithQuantity = cartItem.groupBy { it }
        .map { (item, cartItem) ->
            InternetItemsWithQuantity(item, cartItem.size)
        }

    if (cartItem.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Image(
                    painter = painterResource(R.drawable.bannerthree),
                    contentDescription = "bannerThree",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Text(
                    text = "Review Items",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            items(cartitemsWithQuantity)
            {
                cartCard(
                    it.item,
                    flashViewModal,
                    it.quantity
                )
            }

            item {
                Text(
                    text = "Bill Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            val totalPrice = cartItem.sumOf { it.itemPrice * 75 / 100 }
            val handlingPrice = totalPrice * 1 / 100
            val deliveryCharge = 30
            val grandTotal = totalPrice + handlingPrice + deliveryCharge
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(236, 236, 236, 255)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp))
                    {
                        billRow("Item Total", totalPrice, FontWeight.Normal)
                        billRow("Handling Price", handlingPrice, FontWeight.Light)
                        billRow("DELIVERY Charge", deliveryCharge, FontWeight.Light)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                        billRow("To Pay", grandTotal, FontWeight.ExtraBold)

                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.tonalscreen),
                "App Icon", modifier = Modifier.size(70.dp)
            )
            Text(
                text = "Your Cart is Empty",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
            FilledTonalButton(onClick = {onHomeButtonClicked()})
            { Text(text = "Browse Products") }
        }
    }

}

@SuppressLint("ComposableNaming")
@Composable
fun billRow(
    itemName: String,
    itemPrice: Int,
    fontWeight: FontWeight
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    )
    {
        Text(text = itemName, fontWeight = fontWeight)
        Text(text = "Rs.${itemPrice}", fontWeight = fontWeight)
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun cartCard(
    cartItem: InternetItem,
    flashViewModal: FlashViewModal,
    cartItemQuatity: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            cartItem.imageUrl,
            "ItemImages", modifier = Modifier
                .fillMaxHeight()
                .padding(5.dp)
                .weight(4f)
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f)
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        )
        {
            Text(text = cartItem.itemName, fontSize = 16.sp, maxLines = 1)
            Text(text = cartItem.itemQuantity, fontSize = 14.sp, maxLines = 1)
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .padding(horizontal = 5.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        )
        {
            Text(
                text = "Rs.${cartItem.itemPrice}",
                fontSize = 12.sp,
                maxLines = 1,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
            Text(
                text = "Rs.${cartItem.itemPrice * 75 / 100}",
                fontSize = 18.sp,
                maxLines = 1,
                color = Color.Red
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f),
            verticalArrangement = Arrangement.SpaceEvenly
        )
        {
            Text(
                text = "Quantity: $cartItemQuatity",
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier
                    .clickable {
                        flashViewModal.removeFromCart( cartItem)
                    }
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(254, 116, 105, 255))
            )
            {
                Text(
                    text = "Remove",
                    color = Color.White,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}