package com.example.smartfridge.android

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smartfridge.android.Hashing.passwordHash
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern

class UpdatePassword : AppCompatActivity() {
    private val minPasswordLength = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        //get password editext
        val firstPassword = findViewById<EditText>(R.id.new_mpd)
        val secondPassword = findViewById<EditText>(R.id.confirm_new_mdp)


        //get email from previous activity
        //Recover the email from the previous activity
        val emailFromForgot = intent
            .getStringExtra("Email")

        //display email

        findViewById<TextView>(R.id.textView_email).apply {
            text = emailFromForgot
        }


        //CheckBox
        val checkbox1 = findViewById<CheckBox>(R.id.checkBox_password_1)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox_password_2)

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.

        checkbox1.setOnCheckedChangeListener { _, isChecked ->

            //check if checkbox is checked or not
            if (isChecked) {

                firstPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()

                //checkbox is checked
            } else {
                //checkbox is not checked

                firstPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        checkBox2.setOnCheckedChangeListener { _, isChecked ->


            //check if checkbox is checked or not
            if (isChecked) {

                secondPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()

                //checkbox is checked
            } else {
                //checkbox is not checked

                secondPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //Button Check strenght Password
        val buttonCheckStrenght1 = findViewById<Button>(R.id.button_check_password1)
        buttonCheckStrenght1.setOnClickListener {

            isValidPassword()
        }


        val buttonCheckStrenght2 = findViewById<Button>(R.id.button_check_password2)
        buttonCheckStrenght2.setOnClickListener {

            isValidPassword()
        }


        //Check if password 1 equals password 2
        val buttonConfirm = findViewById<Button>(R.id.submit)

        //Check password 1 and 2 srenght

        buttonConfirm.setOnClickListener {

            isValidPassword()


        }

        val buttonReturn = findViewById<Button>(R.id.bouton_retour)
        buttonReturn.setOnClickListener {
            val i = Intent(this, CheckCodeMail::class.java)



            startActivity(i)

        }

    }


    /**
     *@author  : Ben-Tahri Merwane
     * FUNCTION : isValidPassword
     *
     * Checks if the password is valid as per the following password policy.
     * Password should be minimum minimum 8 characters long.
     * Password should contain at least one number.
     * Password should contain at least one capital letter.
     * Password should contain at least one small letter.
     * Password should contain at least one special character.
     * Allowed special characters: "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
     *
     * @param -password- can be EditText or String
     *
     * @return - true if the password is valid as per the password policy.
     */
    private fun isValidPassword(): Boolean {

        val passwordEdittext1 = findViewById<EditText>(R.id.new_mpd)
        val passwordEdittext2 = findViewById<EditText>(R.id.confirm_new_mdp)

        // check for pattern
        // check for pattern
        val uppercase = Pattern.compile("[A-Z]")
        val lowercase = Pattern.compile("[a-z]")
        val digit = Pattern.compile("[0-9]")

        // Password
        if (passwordEdittext1.text.toString() == "") {
            passwordEdittext1.error = "Veuillez entrer un mot de passe !"
        }
        // Same password / verification
        if (passwordEdittext2.text.toString() == "") {
            passwordEdittext2.error = "Veuillez entrer un mot de passe !"
        }

        // Checking if the password contains or not at least one lowercase
        if (!lowercase.matcher(passwordEdittext1.text.toString()).find()) {
            passwordEdittext1.error = "Doit contenir au moins une minuscule !"
            return false
        }

        // Checking if the password contains or not at least one uppercase
        if (!uppercase.matcher(passwordEdittext1.text.toString()).find()) {
            passwordEdittext1.error = "Doit contenir au moins une majuscule !"
            return false
        }

        // Checking if the password contains or not at least one lowercase
        if (!digit.matcher(passwordEdittext1.text.toString()).find()) {
            passwordEdittext1.error = "Doit contenir au moins un chiffre !"
            return false
        }

        // Checking minimum password length, in this case minimum of 8 characters
        if (passwordEdittext1.text.length < minPasswordLength) {
            passwordEdittext1.error =
                "La longueur du mot de passe doit contenir au moins $minPasswordLength charactères"
            return false
        }

        // Checking if repeat password is same
        if (passwordEdittext1.text.toString() != passwordEdittext2.text.toString()) {
            passwordEdittext2.error = "La mot de passe ne correspond pas !"
            return false
        }
        val emailFromForgot = intent
            .getStringExtra("Email")
        updatePassword(emailFromForgot, passwordEdittext2.text.toString())

        return true


    }

    /**
     * @author Ben-Tahri Merwane
     * This function update_password : update a password for a user in database with email
     * @param -email- can be EditText or String, its email user
     * @param -password- can be EditText or String, its password user
     *
     * @return - close activity when password changed
     */

    private fun updatePassword(email: String?, password: String?) {

        val code = intent
            .getStringExtra("Code")

        //hash password
        //val password_hash = passwordHash(password.toString())
        //Log.d("MainActivity", "$password_hash")
        //Log.d("MainActivity", "$password_hash")

        val putUrl = "https://smartfridge.online/api/users/update-password"
        val requestQueue = Volley.newRequestQueue(this)

        val putData = JSONObject()
        try {

            putData.put("Password", passwordHash(password.toString()))
            putData.put("Email", email)
            putData.put("Code", code)


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, putUrl, putData,
            { response -> println(response) }


        ) { error -> error.printStackTrace() }
        requestQueue.add(jsonObjectRequest)
        Toast.makeText(
            this,
            "Mot de passe mis à jour pour : $email",
            Toast.LENGTH_SHORT
        ).show()


        val i = Intent(this, Login::class.java)
        startActivity(i)


    }
}
