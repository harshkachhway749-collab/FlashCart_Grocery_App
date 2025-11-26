@file:Suppress("PropertyName", "PrivatePropertyName")

package com.internshala.flash.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internshala.flash.Network.FlashApi
import com.internshala.flash.data.InternetItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.minus

class FlashViewModal: ViewModel()
{     //_uiState = mutable inside the ViewModel.
    private val auth = Firebase.auth

    private val  _uiState= MutableStateFlow(FlashUIState())// Backing property
    val uiState: StateFlow<FlashUIState> = _uiState.asStateFlow()// .asStateFlow() → converts it to a read-only StateFlow.
// public property the the other parts of the program can read it but cannot change it
    private val _isVisible = MutableStateFlow(true) // include backing property for offer screen
     val isVisible=_isVisible // for read access by other classes...
    // .....or observeing by other classes it allows external classes to observe changes in this property
       private val _CartItem= MutableStateFlow<List<InternetItem>>(emptyList())
       val CartItem :StateFlow<List<InternetItem>>get()=_CartItem.asStateFlow()

               // for data persistance

       private lateinit var timerJob: Job
    private val _user=MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?>get() = _user

     private val _loading= MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean>get() = _loading

    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked: MutableStateFlow<Boolean>get() = _logoutClicked

    fun setLogoutStatus(
        logoutStatus: Boolean
    ){
        _logoutClicked.value=logoutStatus
    }

    val database = Firebase.database
    val myRef = database.getReference("users/${auth.currentUser?.uid}/cart")

//    fun addToDatabase(item: InternetItem){
//        _CartItem.value+=item
//    }
fun addToDatabase(item: InternetItem) {
    myRef.push().setValue(item)
    _CartItem.value = _CartItem.value + item
}

    fun setUser(user: FirebaseUser)
    {
        _user.value=user
    }
    fun setLoading(isLoading: Boolean){
        _loading.value=isLoading
    }
    // fillCartItems() is a function that is used to read from database....
    // ...and when reopens the app user will get all the data stored in real time database
    fun fillCartItems() {
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<InternetItem>()
                for (child in snapshot.children) {
                    val item = child.getValue(InternetItem::class.java)
                    item?.let { list.add(it) }
                }
                _CartItem.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    @Suppress("FunctionName")
    fun ClearData(){
        _user.value=null
        _phoneNumber.value=""
        _otp.value=""
        verificationId.value=""
        resetTimer()
    }

    private val _ticks=MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long>get() = _ticks
    fun runTimer(){
        if (::timerJob.isInitialized && timerJob.isActive) {
            timerJob.cancel()
        }
       timerJob= viewModelScope.launch {
            while (_ticks.value>0)
            {
                delay(1000)
                _ticks.value-=1
            }
        }
    }

    fun resetTimer(){
        try {
            timerJob.cancel()
        }catch (_: Exception){

        } finally {
            _ticks.value=60L
        }
    }

    private val _vericationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String>get() = _vericationId

    fun setVerificationId(verifiactionId:String){
        _vericationId.value=verifiactionId
    }

    @Suppress("RemoveExplicitTypeArguments")
    private val _phoneNumber=MutableStateFlow<String>("")
    val PhoneNumber: MutableStateFlow<String>get() = _phoneNumber

    fun setPhoneNumber(phoneNumber:String){
        _phoneNumber.value=phoneNumber
    }

    private val _otp= MutableStateFlow("")
    val otp: MutableStateFlow<String>get() =_otp

    fun setOtp(otp: String){
        _otp.value=otp
    }


//    fun removeFromCart(item: InternetItem){
//        _CartItem.value-=item
//       // viewModelScope.launch { saveItemsToDataStore() }
//    }
fun removeFromCart(item: InternetItem) {

    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (child in snapshot.children) {

                val currentItem = child.getValue(InternetItem::class.java)
                if (currentItem?.itemName == item.itemName &&
                    currentItem.itemPrice == item.itemPrice) {

                    child.ref.removeValue()      // remove from Firebase
                    break
                }
            }

            // REMOVE LOCALLY
            _CartItem.value = _CartItem.value - item
        }

        override fun onCancelled(error: DatabaseError) { }
    })
}



    lateinit var internetJob: Job // why use lateinit ?
    var screenJob: Job

    sealed interface ItemUiState{
        data class Success(val item: List<InternetItem>): ItemUiState
        object Loading: ItemUiState
        object Error: ItemUiState
    }


    fun updatedUiText(updatedText:String)
    {
        _uiState.update {
            it.copy( // copy of current ui state
                clickStatus = updatedText // here we pass the new...
               // ...updatedText into clickStatus to display new value
            )
        }
    }
     var itemUistate: ItemUiState by mutableStateOf(ItemUiState.Loading)
         private set
    // private set → This means:
    //Only this class can modify (set) the value of itemUiState.
    //Other classes can only read it, not change it.
    //In Simple Words:
   // private set = Read-only from outside, but writable from inside.
    //--------
    // we use this variable itemUistate to hold the fetch data so that we can able to display it on the ui
    fun updateSelectedCategory(updateCategory: Int){
        _uiState.update { it.copy(
            selectedCategory = updateCategory
        ) }
    }
    fun toggleVisibility(){ // to make our offerScreen to go away in few seconds after appearing
        _isVisible.value = false
    }
    @SuppressLint("SuspiciousIndentation")
    fun getFlashItems(){
        //I.M.P-> this func is a bridge b/w getItem function and the ui get so that whenever getItem
        // function fetches data getFlashItem function displays it on the ui,because getitem func defined in the FlashApiService.kt is only
        // able to fetch data but its not able to pass the data to be displayed in our app
        internetJob=viewModelScope.launch {
            try {
                val listResult= FlashApi.retrofitService.getItems()
                itemUistate= ItemUiState.Success(listResult)

            }
            catch (_: Exception){
            itemUistate= ItemUiState.Error
                toggleVisibility() // to hide the offer screen ***Review
                screenJob.cancel() // we cancel it so that it will not run again ***Review
            }

        //I.M.P-> here we assign listresult to itemUiState,
        // In our listItem all the data is present fetched from the func getItems() in the FlashApiService file

        }
    }
    init {
       screenJob= viewModelScope.launch(Dispatchers.Default) { // this is viewModalScope in which we are launching coroutines
            delay(3000)
            toggleVisibility()
        }
        getFlashItems()
        // I.M.P-> here we call the getFlashItems func inside init block...
    // ....so that it will  automatically call whenever user opens the app
        fillCartItems()
    }
}