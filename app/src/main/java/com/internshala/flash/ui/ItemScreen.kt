package com.internshala.flash.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem

@Composable
fun ItemScreen(
    d: FlashViewModal,
    items: List<InternetItem>
) {


    val flashUiState by d.uiState.collectAsState()//collectAsState() converts the StateFlow into a Compose State.
    // /*Think of it like a box containing the FlashUiState object.
    val selectedCategoryA = stringResource(id = flashUiState.selectedCategory)
    /*The by keyword unwraps the box automatically.
    So flashUiState becomes the actual FlashUiState object stored inside the Compose State wrapper.*/
    val database = items.filter {
        it.itemCategory.lowercase() == selectedCategoryA.lowercase()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.bannertwo),
                    "bannerOne",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp)
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108, 194, 111, 255),
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                ) {
                    Text(
                        text = "${stringResource(id = flashUiState.selectedCategory)}(${database.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }
        items(database)
        {
            itemCard(
                it.itemName,
                it.imageUrl,
                it.itemQuantity,
                it.itemPrice,
                d
            )
        }


    }
}

@SuppressLint("ComposableNaming")
@Composable
fun internetItemScreen(
    f: FlashViewModal,
    itemUiState: FlashViewModal.ItemUiState
) {
    when (itemUiState) {
        is FlashViewModal.ItemUiState.Loading -> {
            loadingScreen()
        }

        is FlashViewModal.ItemUiState.Success -> {
//            Text(text = itemUiState.item.toString())
            ItemScreen(f, itemUiState.item)
        }

        else -> {
            errorScreen(f)
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun itemCard(
    stringResourceIdm: String,
    imageResourceIdm: String,
    itemQantitym: String,
    itemPricem: Int,
    flashViewModal: FlashViewModal
) {

    Column(modifier = Modifier.size(160.dp))
    {
        Card(
            modifier = Modifier.padding(3.dp),
            colors = CardDefaults.cardColors(containerColor = Color(248, 221, 248, 255))
        )
        {
            Box()
            {
                AsyncImage(
                    model = imageResourceIdm, contentDescription = stringResourceIdm,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(
                                244,
                                67,
                                54,
                                255
                            )
                        )
                    ) {
                        Text(
                            text = "25% off", color = Color.White, fontSize = 6.sp,
                            modifier = Modifier.padding(5.dp, vertical = 2.dp)
                        )

                    }
                }
            }
        }
        Text(
            text = stringResourceIdm,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            maxLines = 1,
            textAlign = TextAlign.Left
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column(verticalArrangement = Arrangement.spacedBy((-7).dp)) {
                Text(
                    text = "Rs${itemPricem}", fontSize = 8.sp,
                    maxLines = 1, textAlign = TextAlign.Center,
                    color = Color(109, 109, 109, 255),
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "Rs${itemPricem * 75 / 100}",
                    fontSize = 12.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(255, 116, 105, 255)
                )
            }
            Text(
                modifier = Modifier.padding(top = 14.dp, end = 3.dp),
                text = "Qty.${itemQantitym}",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                color = Color(144, 144, 144, 255)
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable {
                    flashViewModal.addToDatabase(
                        InternetItem(
                            stringResourceIdm,
                            "",
                            itemPricem,
                            itemQantitym,
                            imageResourceIdm
                        )
                    )
                }, colors = CardDefaults.cardColors(containerColor = Color(108, 194, 111, 255))
        ) {//108, 194, 111, 255
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Add to Cart",
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun loadingScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable._loading),
            contentDescription = "Loading"
        )
    }

}

@SuppressLint("ComposableNaming")
@Composable
fun errorScreen(flashViewModal: FlashViewModal) {
    //val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable._error),
            contentDescription = "Error"
        )
        Text(
            text = "Oops ! Internat unavailable. Please Check your Connection or retry after turning your mobile data or wifi on",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { flashViewModal.getFlashItems() })
        {
            Text(
                text = "Retry", color = Color.White,
                fontSize = 19.sp
            )
        }
    }
}


