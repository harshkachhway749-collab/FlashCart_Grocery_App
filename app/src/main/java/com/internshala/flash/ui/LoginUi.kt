package com.internshala.flash.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.internshala.flash.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@SuppressLint("ComposableNaming")
@Composable
fun logInUi(flashViewModal: FlashViewModal) {
    val context = LocalContext.current
    val otp by flashViewModal.otp.collectAsState()
    val verificationId by flashViewModal.verificationId.collectAsState()
  val loading by flashViewModal.loading.collectAsState()
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            flashViewModal.setVerificationId(verificationId)
            Toast.makeText(context, "VERIFY OTP", Toast.LENGTH_SHORT).show()
            flashViewModal.resetTimer()
            flashViewModal.runTimer()
            flashViewModal.setLoading(false)
        }
    }



    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                painter = painterResource(R.drawable.tonalscreen),
                contentDescription = "Login Screen icon",
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 10.dp)
                    .size(100.dp)
            )
            if (verificationId.isEmpty()) {
                NumberScreen(flashViewModal = flashViewModal, callbacks)
            } else {
                OtpScreen(otp, flashViewModal = flashViewModal, callbacks = callbacks)
            }


        }
        if (verificationId.isNotEmpty())
        {
            IconButton(onClick ={
                flashViewModal.setVerificationId("")
                flashViewModal.setOtp("")
            } ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Arrow ")
            }
        }

        @Suppress("ControlFlowWithEmptyBody")
        if (loading)
        {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(color = Color(255,255,255,190)))
            {
                CircularProgressIndicator()
                Text(text = "Loading")
            }
        } else
        {

        }

    }
}