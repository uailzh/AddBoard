package com.example.addboard.act

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.RootElement
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.addboard.R
import com.example.addboard.databinding.ActivityEditAdsBinding
import com.example.addboard.dialogsAds.DialogSpinnerHelper
import com.example.addboard.utils.CityHelper
import com.example.addboard.utils.ImagePicker
import com.fxn.utility.PermUtil

class EditAdAct : AppCompatActivity() {

   lateinit var rootElement: ActivityEditAdsBinding
    private var dialog = DialogSpinnerHelper()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   ImagePicker.getImages(this)
                }
                else {

                    Toast.makeText(this, "Approve permission to open Pix imagePicker", Toast.LENGTH_LONG).show()

                }
                return
            }
        }
    }


    private fun init()  {



    }


    //OnClicks

    fun onClickSelectCountry(view: View) {

        val listCountry = CityHelper.getAllCountries(this)


        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)

        if (rootElement.tvCity.text.toString() != getString(R.string.select_city)) {

            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }


    fun onClickSelectCity(view: View) {

        val selectedCountry = rootElement.tvCountry.text.toString()

        if (selectedCountry != getString(R.string.select_country)) {
            val listCity = CityHelper.getAllCities(selectedCountry, this)

            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity)
        } else {

            Toast.makeText(this, "No country selected", Toast.LENGTH_LONG).show()
        }

    }


    fun onClickGetImages(view: View) {

        ImagePicker.getImages(this)


    }
}