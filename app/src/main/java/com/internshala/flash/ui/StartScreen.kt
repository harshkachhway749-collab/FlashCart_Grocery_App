package com.internshala.flash.ui

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internshala.flash.R
import com.internshala.flash.data.DataSource


@Composable//flashViewModal
// I>M>P-> a → the ViewModel instance.
//a.uiState → the StateFlow (read-only state).
//.collectAsState() → converts it into Compose state so the UI can observe and recompose automatically.
fun StartScreen(
    a: FlashViewModal, onCategoryClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    //val view = LocalView.current


    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 155.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {   //span = { GridItemSpan(2) }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.bannerone),
                    "bannerOne",
                    modifier = Modifier.fillMaxWidth().size(150.dp)
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108, 194, 111, 255),
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                ) {
                    Text(
                        text = "Shop by Category",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.SemiBold,
                        color=Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }

        //item { Text(text = b.clickStatus) } // we only show one item thats why we use item instead of items// Mvvm architecture
        items(DataSource.loadCategories()) {

            CategoryCard(
                context,
                it.stringResourceIda,
                it.imageResourceIda,
                ab = a,
                onCategoryClick = onCategoryClicked // review


            )
        }
    }


}

@Composable
fun CategoryCard(
    context: Context,
    stringResourceIdv: Int,
    imageResourceIdv: Int,
    ab: FlashViewModal,
    onCategoryClick: (Int) -> Unit
) { // review
    val categoryName = stringResource(id = stringResourceIdv)
    val mediaPlayer = MediaPlayer.create(context, R.raw.scificlick)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Card(
            modifier = Modifier.clickable {
                onCategoryClick(stringResourceIdv)  // review
                ab.updatedUiText(categoryName)
                //view.playSoundEffect(SoundEffectConstants.CLICK)
                mediaPlayer.start()
                Toast.makeText(
                    context, "A card was clicked by user", Toast.LENGTH_SHORT
                ).show()
            }, colors = CardDefaults.cardColors(containerColor = Color(248, 221, 248, 255))
        ) {
            Column(

                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = categoryName,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()

                )
                Image(
                    painter = painterResource(imageResourceIdv),
                    contentDescription = "fruits",
                    modifier = Modifier.size(150.dp)
                )
            }

        }
    }
}