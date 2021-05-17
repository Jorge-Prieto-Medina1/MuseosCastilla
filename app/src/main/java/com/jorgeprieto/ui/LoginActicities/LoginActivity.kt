package com.jorgeprieto.ui.LoginActicities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.NavigationDrawerMuseoActivity
import com.jorgeprieto.museosjorgeprieto.ProviderType
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


//actividad del login

class LoginActivity : AppCompatActivity() {
    //codigo de google e instancia de la base de datos
    private  val googleSignIn = 300
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        register()
        login ()
        session()
        loginGoogle()
    }

    //función que hace al layout visible
    override fun onStart() {
        super.onStart()
        layoutLg.visibility = View.VISIBLE
    }

    //activity result del sign in de google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == googleSignIn){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {

                            db.collection("users").document(account.email.toString()).get().addOnSuccessListener {
                                if (it.get("address") != account.email.toString()){
                                    db.collection("users").document(account.email.toString()).set(
                                        hashMapOf(
                                            "username" to account.displayName.toString(),
                                            "address" to account.email.toString(),
                                            "provider" to ProviderType.GOOGLE.toString()
                                        )
                                    )

                                    goToMain(account.email ?: "", ProviderType.GOOGLE)
                                }else{

                                    goToMain(account.email ?: "", ProviderType.GOOGLE)
                                }

                            }

                        } else {
                            showAlert()
                        }
                    }

                }
            }catch (e: ApiException){
                showAlert()
            }

        }
    }


    //función que hace el login con google
    private fun loginGoogle(){
        btnGoogleLg.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, googleSignIn)
        }
    }

    //esta función vuelve el layout invisible si el usuario ya tiene le login hecho
    // la idea es que al tener guardado el login anterior
    //el usuario no tenga que ver el login al volver a entrar
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null){
            layoutLg.visibility = View.INVISIBLE
            goToMain(email, ProviderType.valueOf(provider))
        }
    }


    //esta función simplemente abre la actividad de registarte
    private fun register (){
        btnRegisterLg.setOnClickListener{
            val register = Intent (this@LoginActivity, RegisterActivity::class.java)
                startActivity(register)
                finish()
        }
    }

    //función que comprueba que los datos estén en la base de datos para realizar el login
    private fun login (){
        btnLogin.setOnClickListener{
            if (txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmail.text.toString(),txtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMain(txtEmail.text.toString(), ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

    }

    //alerta para cuando el email o la contraseña son incorrectos
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, wrong email or password")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //función que manda al usuario a la main activity junto con el email y el provider
    private fun goToMain(email: String, provider: ProviderType){
        val main = Intent (this@LoginActivity, NavigationDrawerMuseoActivity::class.java).apply {
            putExtra("email", email)
            putExtra( "provider", provider.name)
        }
        startActivity(main)
        finish()
    }
}
