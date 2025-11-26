package com.internshala.flash.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.internshala.flash.data.InternetItem


enum class FlashAppScreen(val title: String) {
    Start("FlashCart"),
    Item("Choose Item"),
    Cart("Your Cart")
}

var canNavigateBack = false
val auth= FirebaseAuth.getInstance()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashApp(
    c: FlashViewModal = viewModel<FlashViewModal>(),
    navControllerA: NavHostController = rememberNavController()

) /*viewModel<FlashViewModal>() → this is a Compose utility function that does one of two things:
Returns an existing FlashViewModal object tied to the Composable lifecycle if it already exists.
Creates a new FlashViewModal object by calling its constructor if it doesn’t exist yet.
a → the parameter of the function, which now refers to the object returned by viewModel().*/
{
//   StartScreen(a = c) // we are passing object c of FlashViewmodal into a
val logoutClicked by c.logoutClicked.collectAsState()
    val user by c.user.collectAsState()
    auth.currentUser?.let { c.setUser(it) }
    // backStack used to store all previous screen
    val isVisibleA by c.isVisible.collectAsState()
    val backStackEntry by navControllerA.currentBackStackEntryAsState()
    /*“This line gives the current screen, and
    AsState makes it reactive so the UI recomposes whenever the current screen changes.”*/
    val currentScreen = FlashAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FlashAppScreen.Start.name // understood
    )
    // canNavigateBack is used if it is null then false and when not then true .
    // it helps to navigate on the previous screen if we are not at start screen
    canNavigateBack = navControllerA.previousBackStackEntry != null // understood
    val cartItems by c.CartItem.collectAsState() // for implementing badge
    if (isVisibleA) {
        offerScreen()
    }else if (user==null)
    {
        logInUi(c)
    }
    else {
        Scaffold(topBar = {
//        Row(modifier = Modifier.fillMaxWidth().
//        background(Color(0xFF1976D2)).padding(15.dp),
//            horizontalArrangement = Arrangement.Center) { Text(
//            text = "Welcome everyone",
//            textAlign = TextAlign.Center,
//            fontSize = 30.sp, color = Color.White
//        ) }
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentScreen.title, //current tilte shows current screen title
                                fontSize = 26.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            if (currentScreen == FlashAppScreen.Cart) {
                                Text(
                                    text = "${cartItems.size}", //current tilte shows current screen title
                                    fontSize = 26.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }
                        }
                        Row(modifier = Modifier.clickable {
                            c.setLogoutStatus(true)
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                modifier = Modifier.size(34.dp))

                            Text(text = "Logout",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end =14.dp, start = 4.dp ))


                        }
                    }
                },
                navigationIcon = {
                    if (canNavigateBack) {

                        IconButton(onClick = {
                            navControllerA.navigateUp() // uses for back_Arrow
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                    }

                }
            )

        }, bottomBar = { bottomButton(navControllerA, currentScreen, cartItems) })
        {
            NavHost(
                navController = navControllerA, startDestination = FlashAppScreen.Start.name,
                modifier = Modifier.padding(it) // this is for scaffold mandatory
            ) {
                composable(FlashAppScreen.Start.name) {
                    StartScreen(c) { it ->
                        c.updateSelectedCategory(it)
                        navControllerA.navigate(FlashAppScreen.Item.name)
                        /*Why we put it inside the lambda
             The lambda is called when a category is clicked (CategoryCard).
             We want both actions — update data + navigate — to happen exactly when the user clicks.
             That’s why the lambda contains both lines, executed sequentially.*/
                    }
                }
                composable(FlashAppScreen.Item.name) {
                    // ItemScreen(c)
                    internetItemScreen(c, itemUiState = c.itemUistate) // for internet connectivity
                }
                composable(FlashAppScreen.Cart.name) {
                    CartScreen(c
                    ) {
                        navControllerA.navigate(FlashAppScreen.Start.name) {

                            popUpTo(0)
                        }
                    }
                }
            }
        }
        if (logoutClicked){
            AlertCheck(onYesButtonPressed = {
                c.setLogoutStatus(false)
                auth.signOut()
                c.ClearData()
            },
                onNoButtonPressed = {
                    c.setLogoutStatus(false)
                })
        }
    }


}

@SuppressLint("ComposableNaming")
@Composable
fun bottomButton(
    navControllerB: NavHostController,
    currentScreenB: FlashAppScreen,
    cartItem: List<InternetItem>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE))
            .padding(vertical = 45.dp, horizontal = 70.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navControllerB.navigate(FlashAppScreen.Start.name) {
                    // this PopUpTo Fun.. is for clearing all previous screens so that no...
                    // ...screen is remainning when user click in the icon
                    popUpTo(0)
                }
            }
        )
        {
            Icon(
                Icons.Outlined.Home, "Home",
                modifier = Modifier.size(40.dp)
            )
            Text(text = "Home", fontSize = 18.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                if (currentScreenB !== FlashAppScreen.Cart) {
                    navControllerB.navigate(FlashAppScreen.Cart.name) {
                        // this PopUpTo Fun.. is for clearing all previous screens so that no...
                        // ...screen is remainning when user click in the icon
                        //popUpTo(0)

                    }
                }

            }
        )
        {
            Box {
                Icon(
                    Icons.Outlined.ShoppingCart, "Cart",
                    modifier = Modifier.size(40.dp)
                )
                if (cartItem.isNotEmpty())
                Card(shape = CircleShape,
                    modifier = Modifier.size(22.dp)
                        .align(alignment = Alignment.TopEnd),
                    colors = CardDefaults.cardColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = cartItem.size.toString(),
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 1.dp, bottom = 2.dp),
                        maxLines = 1
                    )
                }
            }
            Text(text = "Cart", fontSize = 18.sp)
        }
    }
}

@Composable
fun AlertCheck(
    onYesButtonPressed:()->Unit,
    onNoButtonPressed:()-> Unit
){
    AlertDialog(
        title={
            Text(text = "Logout?", fontWeight = FontWeight.Bold)
        },
        containerColor= Color.White,
        text={
            Text(text = "Are you sure you want to logout?")
        },
        confirmButton={
            TextButton(onClick = {
                onYesButtonPressed()
            })
            {
                Text(text="Yes")
            }
        },
        dismissButton={
            TextButton(onClick = {
                onNoButtonPressed()
            }) {
                Text(text = "No")
            }
        },
        onDismissRequest={
            onNoButtonPressed()
        }

    )
}



