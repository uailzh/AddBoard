package com.example.addboard.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.example.addboard.MainActivity
import com.example.addboard.R
import com.example.addboard.accountHelper.AccountHelper
import com.example.addboard.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {

    private val act = act
    val accHelper = AccountHelper(act)

    fun createSignDialog(index:Int) {
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)

        setDialogState(index, rootDialogElement)

        val dialog = builder.create()
        rootDialogElement.btSignUpin.setOnClickListener {

            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btforgetP.setOnClickListener {

            setOnClickResetPassword(rootDialogElement, dialog)
        }
        rootDialogElement.btGoogleSignIn.setOnClickListener {

            accHelper.signInWithGoogle()
            dialog.dismiss()
        }


        dialog.show()

    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {

        if(rootDialogElement.edSignEmail.text.isNotEmpty()) {
            act.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(act, R.string.email_reset_was_sent, Toast.LENGTH_LONG).show()

                    }
                }
            dialog?.dismiss()
        }
            else {
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }


        }


    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss()
        if(index == DialogConst.SIGN_UP_STATE)
        {
            accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(), rootDialogElement.edSignPassword.text.toString())

        } else {

            accHelper.signInWithEmail(rootDialogElement.edSignEmail.text.toString(), rootDialogElement.edSignPassword.text.toString())

        }
    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {

        if (index == DialogConst.SIGN_UP_STATE) {

            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_up)
            rootDialogElement.btSignUpin.text = act.resources.getString(R.string.sign_up_action)
        }
        else {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_in)
            rootDialogElement.btSignUpin.text = act.resources.getString(R.string.sign_in_action)
            rootDialogElement.btforgetP.visibility = View.VISIBLE

        }
    }
}