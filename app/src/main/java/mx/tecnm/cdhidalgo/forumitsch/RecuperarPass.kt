package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
var email = ""

class RecuperarPass : AppCompatActivity() {

    private lateinit var correo:TextInputLayout
    private lateinit var btnRecuperar:Button
    private lateinit var btnRegresar:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_pass)

        auth = FirebaseAuth.getInstance()

        correo = findViewById(R.id.emailPass)
        btnRecuperar = findViewById(R.id.btnRecuperar)
        btnRegresar = findViewById(R.id.btnRegresarP)

        btnRecuperar.setOnClickListener{
            email = correo?.editText?.text.toString()
            if(email.isNotEmpty()){
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correo.error = null
                    recuperarPassword()
                    Toast.makeText(this, "Correo enviado", Toast.LENGTH_SHORT).show()
                } else {
                    correo.error = "Correo invalido"
                    Toast.makeText(this, "Correo invalido", Toast.LENGTH_SHORT).show()
                }
            } else {
                correo.error = "Correo obligatorio"
                Toast.makeText(this, "Correo obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
    }

    private fun recuperarPassword() {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Instrucciones de restablecimiento de contraseña enviadas
                } else {
                    // Algo salió mal
                }
            }
    }
}