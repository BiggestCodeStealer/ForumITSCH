package mx.tecnm.cdhidalgo.forumitsch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

lateinit var auth: FirebaseAuth
var email = ""

class RecuperarPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_pass)
/*
        correo = findViewById(R.id.email_recuperar)
        btnRecuperar = findViewById(R.id.btnRecuperarPass)
        btnRegresar = findViewById(R.id.btnRegresar_recuperar)

        btnRecuperar.setOnClickListener{
            val email = correo.editText?.text.toString()
            if(email.isNotEmpty()){
                if(Patterns.EMAIL_ADDRESS.matcher(email.matches())){
                    correo.error = null
                    recuperarPassword()
                } else {
                    correo.error = "Correo invalido"
                }
            } else {
                correo.error = "Correo obligatorio"
            }
        }
    }

    private fun recuperarPassword() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener{
            Toast.makeText(this,"Se ha enviado a $email un correo de recuperacion",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)

        }
 */
    }
}