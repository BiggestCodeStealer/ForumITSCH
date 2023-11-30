package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Usuario

lateinit var usuario: Usuario

class Login : AppCompatActivity() {

    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnIngresar:MaterialButton
    private lateinit var btnRecuperar:MaterialButton

    private lateinit var correo:TextInputLayout
    private lateinit var pass:TextInputLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val baseDatos = Firebase.firestore

        //Acceso a la base de datos
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnIngresar = findViewById(R.id.btnIngresar)
        btnRecuperar = findViewById(R.id.btnOlvidar)

        correo = findViewById(R.id.email)
        pass = findViewById(R.id.password)

        btnIngresar.setOnClickListener{
            val email = correo.editText?.text
            val psw = pass.editText?.text

            if(email.toString().isNotEmpty()&&psw.toString().isNotEmpty()){
                auth.signInWithEmailAndPassword(email.toString(),psw.toString()).
                addOnCompleteListener{
                    if (it.isSuccessful){
                        baseDatos.collection("usuarios")
                            .whereEqualTo("correo",email.toString())
                            .get()
                            .addOnSuccessListener {documentos ->
                                for(documento in documentos){
                                    usuario = Usuario(
                                        "${documento.data.get("correo")}",
                                        "${documento.data.get("nombre")}",
                                        "${documento.data.get("apellidos")}",
                                        "${documento.data.get("carrera")}",
                                        "${documento.data.get("rol")}"
                                    )}
                                val intent = Intent(this,Principal::class.java)
                                startActivity(intent)
                            }
                    } else
                        notificacion()
                }
            }
        }

        btnRegistrar.setOnClickListener{
            val intent = Intent(this,Registro::class.java)
            startActivity(intent)
        }
    }

    private fun notificacion() {
        val notiDialogo = AlertDialog.Builder(this)
        notiDialogo.setTitle("Error")
        notiDialogo.setMessage("Se ha producido un Error en la AUTENTICACION del usuario")
        notiDialogo.setPositiveButton("Aceptar", null)
        notiDialogo.show()
    }
}