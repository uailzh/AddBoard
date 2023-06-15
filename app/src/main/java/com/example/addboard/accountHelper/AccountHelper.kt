package com.example.addboard.accountHelper

import android.widget.Toast
import com.example.addboard.MainActivity
import com.example.addboard.R
import com.example.addboard.constants.FirebaseAuthConstants
import com.example.addboard.dialogs.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AccountHelper(act:MainActivity) {

    private val act = act

    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(email:String, password:String) {

        if(email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->

                if(task.isSuccessful) {

                    sendEmailVerification(task.result?.user!!)

                    act.uiUpdate(task.result?.user)

                }
                else
                {
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()

                    if (task.exception is FirebaseAuthUserCollisionException)
                    {
                        val exception = task.exception as FirebaseAuthUserCollisionException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {

                            linkEmailToG(email, password)
                        }
                    }

                    else if (task.exception is FirebaseAuthInvalidCredentialsException)
                    {

                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL)
                        {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        }
                        else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD)
                        {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                        }
                    }

                    else if (task.exception is FirebaseAuthWeakPasswordException)
                    {

                        val exception = task.exception as FirebaseAuthWeakPasswordException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD)
                        {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                        }

                    }
                    else if (task.exception is FirebaseAuthInvalidUserException) {

                        val exception = task.exception as FirebaseAuthInvalidUserException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD)
                        {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_USER_NOT_FOUND, Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }




    fun signInWithEmail(email:String, password:String) {

        if(email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->

                if(task.isSuccessful) {

                    act.uiUpdate(task.result?.user)

                } else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun linkEmailToG(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)

        if(act.mAuth.currentUser!=null) {


            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.link_done), Toast.LENGTH_LONG).show()
                }
            }
        }
            else {
            Toast.makeText(act, act.resources.getString(R.string.enter_to_g), Toast.LENGTH_LONG).show()
            }

        }


    private fun getSignInClient():GoogleSignInClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()

        return GoogleSignIn.getClient(act, gso)
    }


    @Suppress("DEPRECATION")
    fun signInWithGoogle() {
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signOutG() {
        getSignInClient().signOut()
    }

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener{task->
            if(task.isSuccessful) {
                Toast.makeText(act,"Sign in is done", Toast.LENGTH_LONG).show()
                act.uiUpdate(task.result?.user)
            }
        }
    }

    private fun sendEmailVerification(user:FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {task->
         if(task.isSuccessful) {
             Toast.makeText(act, act.resources.getString(R.string.send_verification), Toast.LENGTH_LONG).show()
         } else {
             Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
         }
        }
    }
}